package net.phoenix.core.common.machine.multiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.phoenix.core.api.block.IFissionCoolerType;
import net.phoenix.core.api.block.IFissionFuelRodType;
import net.phoenix.core.api.block.IFissionModeratorType;
import net.phoenix.core.configs.PhoenixConfigs;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@MethodsReturnNonnullByDefault
public class DynamicFissionReactorMachine extends FissionWorkableElectricMultiblockMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            DynamicFissionReactorMachine.class,
            FissionWorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
    );

    @Getter
    @Persisted
    private double currentHeatMirror = 0.0;

    public DynamicFissionReactorMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected void handleReactorLogic(boolean running) {
        // Keep mirror in sync for HUDs / other machines
        currentHeatMirror = this.heat;

        // Delegate full behavior (heat, coolant drain, cooling, power while cooling, meltdown) to base
        super.handleReactorLogic(running);

        currentHeatMirror = this.heat;
    }


    /**
     * ✅ Put parallels back for ALL your machines:
     * - IO scaled by parallels
     * - EU boost affects eut multiplier
     * - Fuel discount affects duration multiplier
     */
    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof FissionWorkableElectricMultiblockMachine m)) {
            return com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier
                    .nullWrongType(FissionWorkableElectricMultiblockMachine.class, machine);
        }
        if (!m.isFormed()) return ModifierFunction.IDENTITY;

        int parallels = Math.max(1, m.computeParallels());

        int euBoost = m.getModeratorEUBoostClamped();
        int fuelDiscount = m.getModeratorFuelDiscountClamped();

        double eutMultiplier = 1.0 + (euBoost / 100.0);
        double durationMultiplier = Math.max(0.01, 1.0 - (fuelDiscount / 100.0));

        var b = ModifierFunction.builder()
                .eutMultiplier(eutMultiplier)
                .durationMultiplier(durationMultiplier);

        if (parallels > 1) {
            var mult = ContentModifier.multiplier(parallels);
            b.inputModifier(mult)
                    .outputModifier(mult)
                    .parallels(parallels);
        }

        return b.build();
    }

    @Override
    protected @Nullable IFissionFuelRodType getFuelRodForConsumption() {
        if (activeFuelRods == null || activeFuelRods.isEmpty()) return null;
        return activeFuelRods.stream()
                .max(Comparator.comparingInt(IFissionFuelRodType::getTier))
                .orElse(null);
    }

    // -----------------------------------------------------------------------
    // Machine-driven coolant (still uses cooler's coolant material id -> fluid)
    // -----------------------------------------------------------------------

    protected boolean canConsumeCoolantForThisTickMachineDriven() {
        var cfg = PhoenixConfigs.INSTANCE.fission;
        if (!cfg.coolingRequiresCoolant) return true;
        if (activeCoolers.isEmpty()) return true;

        // additive behavior matches your base config
        if (!cfg.coolantUsageAdditive) {
            IFissionCoolerType primary = primaryCoolerType;
            if (primary == null) return true;

            int mb = Math.max(0, primary.getCoolantPerTick());
            return canConsumeMaterialFluid(primary.getRequiredCoolantMaterial(), mb);
        }

        Map<com.gregtechceu.gtceu.api.data.chemical.material.Material, Integer> required = new HashMap<>();
        for (IFissionCoolerType c : activeCoolers) {
            int mb = Math.max(0, c.getCoolantPerTick());
            if (mb <= 0) continue;

            var mat = c.getRequiredCoolantMaterial();
            if (mat == null) continue;

            required.merge(mat, mb, Integer::sum);
        }

        for (var e : required.entrySet()) {
            if (!canConsumeMaterialFluid(e.getKey(), e.getValue())) return false;
        }
        return true;
    }

    protected boolean consumeCoolantForThisTickMachineDriven() {
        var cfg = PhoenixConfigs.INSTANCE.fission;
        if (!cfg.coolingRequiresCoolant) return true;
        if (activeCoolers.isEmpty()) return true;

        if (!cfg.coolantUsageAdditive) {
            IFissionCoolerType primary = primaryCoolerType;
            if (primary == null) return true;

            int mb = Math.max(0, primary.getCoolantPerTick());
            return tryConsumeMaterialFluid(primary.getRequiredCoolantMaterial(), mb);
        }

        Map<com.gregtechceu.gtceu.api.data.chemical.material.Material, Integer> required = new HashMap<>();
        for (IFissionCoolerType c : activeCoolers) {
            int mb = Math.max(0, c.getCoolantPerTick());
            if (mb <= 0) continue;

            var mat = c.getRequiredCoolantMaterial();
            if (mat == null) continue;

            required.merge(mat, mb, Integer::sum);
        }

        for (var e : required.entrySet()) {
            if (!tryConsumeMaterialFluid(e.getKey(), e.getValue())) return false;
        }
        return true;
    }

    // -----------------------------------------------------------------------
    // Machine-driven fuel (Forge item ids ONLY)
    // -----------------------------------------------------------------------

    protected void tickFuelConsumptionMachineDriven(int parallels) {
        var cfg = PhoenixConfigs.INSTANCE.fission;

        IFissionFuelRodType fuelType = getFuelRodForConsumption();
        if (fuelType == null) return;

        int rodCount = activeFuelRods.size();
        int duration = Math.max(1, fuelType.getDurationTicks());
        int amountPerCycle = Math.max(0, fuelType.getAmountPerCycle());

        // total per cycle (config scaling)
        double totalPerCycle = amountPerCycle;

        if (cfg.fuelUsageScalesWithRodCount) {
            totalPerCycle *= rodCount;
        }
        if (cfg.fuelUsageScalesWithParallels) {
            totalPerCycle *= Math.max(1, parallels);
        }

        // moderator discount
        int discountPct = getModeratorFuelDiscountClamped();
        double mult = 1.0 - (discountPct / 100.0);
        if (mult < 0.0) mult = 0.0;
        totalPerCycle *= mult;

        // remainder accumulator (persisted in base: fuelRemainder)
        double perTick = totalPerCycle / duration;
        fuelRemainder += perTick;

        int toConsumeNow = (int) Math.floor(fuelRemainder);
        if (toConsumeNow <= 0) return;

        // IMPORTANT: only subtract remainder if we actually consume successfully
        String itemId = fuelType.getFuelKey();

        // gate first
        if (!canConsumeItemKey(itemId, toConsumeNow)) {
            // out of fuel at the consumption boundary -> push unsafe so meltdown engages
            heat = Math.max(heat, cfg.maxSafeHeat + 1.0);
            return;
        }

        if (!tryConsumeItemKey(itemId, toConsumeNow)) {
            heat = Math.max(heat, cfg.maxSafeHeat + 1.0);
            return;
        }

        fuelRemainder -= toConsumeNow;
    }

    // -----------------------------------------------------------------------
    // Local item-gated IO helpers (Forge registry)
    // -----------------------------------------------------------------------

    protected boolean canConsumeItemKey(String itemId, int count) {
        if (count <= 0) return true;

        ResourceLocation rl = ResourceLocation.tryParse(itemId);
        if (rl == null) return false;

        Item item = ForgeRegistries.ITEMS.getValue(rl);
        if (item == null || item == net.minecraft.world.item.Items.AIR) return false;

        ItemStack stack = new ItemStack(item, count);

        GTRecipe dummy = GTRecipeBuilder.ofRaw()
                .inputItems(stack)
                .buildRawRecipe();

        return RecipeHelper.matchRecipe(this, dummy).isSuccess();
    }

    protected boolean tryConsumeItemKey(String itemId, int count) {
        if (count <= 0) return true;

        ResourceLocation rl = ResourceLocation.tryParse(itemId);
        if (rl == null) return false;

        Item item = ForgeRegistries.ITEMS.getValue(rl);
        if (item == null || item == net.minecraft.world.item.Items.AIR) return false;

        ItemStack stack = new ItemStack(item, count);

        GTRecipe dummy = GTRecipeBuilder.ofRaw()
                .inputItems(stack)
                .buildRawRecipe();

        if (!RecipeHelper.matchRecipe(this, dummy).isSuccess()) return false;

        return RecipeHelper.handleRecipeIO(this, dummy, IO.IN, getRecipeLogic().getChanceCaches()).isSuccess();
    }
}
