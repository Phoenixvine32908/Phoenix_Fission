package net.phoenix.core.common.machine.multiblock;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.phoenix.core.PhoenixAPI;
import net.phoenix.core.api.block.IFissionBlanketType;
import net.phoenix.core.api.block.IFissionFuelRodType;
import net.phoenix.core.configs.PhoenixConfigs;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@MethodsReturnNonnullByDefault
public class BreederWorkableElectricMultiblockMachine extends DynamicFissionReactorMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BreederWorkableElectricMultiblockMachine.class,
            DynamicFissionReactorMachine.MANAGED_FIELD_HOLDER
    );

    private transient List<IFissionBlanketType> activeBlankets = new ArrayList<>();

    @Nullable
    @Getter
    private transient IFissionBlanketType primaryBlanket = null;

    public BreederWorkableElectricMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        resolveBlanketsFromPersisted();
        selectPrimaryBlanket();
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();

        Object obj = getMultiblockState().getMatchContext().get("BlanketTypes");
        if (obj instanceof List<?> list) {
            //noinspection unchecked
            this.activeBlankets = (List<IFissionBlanketType>) list;
        } else {
            this.activeBlankets = new ArrayList<>();
        }

        selectPrimaryBlanket();
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        this.activeBlankets.clear();
        this.primaryBlanket = null;
    }

    /**
     * Breeder rule: fuel consumption uses the highest tier fuel rod.
     */
    @Override
    protected @Nullable IFissionFuelRodType getFuelRodForConsumption() {
        if (activeFuelRods == null || activeFuelRods.isEmpty()) return null;
        return activeFuelRods.stream()
                .max(Comparator.comparingInt(IFissionFuelRodType::getTier))
                .orElse(null);
    }

    /**
     * Let base/dynamic handle heat/cooling/power/meltdown/fuel timers.
     * Then add breeder transmutation output while running.
     */
    @Override
    protected void handleReactorLogic(boolean running) {
        super.handleReactorLogic(running);

        if (running && isFormed() && primaryBlanket != null) {
            processBreeding(PhoenixConfigs.INSTANCE.fission, lastParallels);
        }
    }

    /**
     * Breeder runs if formed + has rods + has fuel.
     * (Coolant should NOT gate running; lack of coolant just disables cooling if config requires it.)
     */
    @Override
    protected boolean shouldRunReactor() {
        if (!isFormed()) return false;
        if (activeFuelRods == null || activeFuelRods.isEmpty()) return false;
        return hasFuelAvailableForNextTick();
    }

    // ------------------------------------------------------------------------
    // Breeding mechanics
    // ------------------------------------------------------------------------

    private void selectPrimaryBlanket() {
        this.primaryBlanket = activeBlankets.stream()
                .max(Comparator.comparingInt(IFissionBlanketType::getTier))
                .orElse(null);
    }

    private void resolveBlanketsFromPersisted() {
        // persistedBlanketIDs is expected to exist in your base/dynamic (same as your prior version)
        if (this.persistedBlanketIDs == null || this.persistedBlanketIDs.isEmpty()) return;

        this.activeBlankets = new ArrayList<>();
        for (String id : this.persistedBlanketIDs) {
            IFissionBlanketType t = PhoenixAPI.FISSION_BLANKETS.keySet().stream()
                    .filter(b -> b.getName().equals(id))
                    .findFirst()
                    .orElse(null);
            if (t != null) this.activeBlankets.add(t);
        }
    }

    private void processBreeding(PhoenixConfigs.FissionConfigs cfg, int parallels) {
        // Convert "per cycle" blanket rates to per tick, sum across all blankets
        double perTick = 0.0;
        for (var blanket : activeBlankets) {
            perTick += (double) blanket.getAmountPerCycle() / Math.max(1, blanket.getDurationTicks());
        }

        // Apply parallels and the same slow-burn multiplier as power (base provides it)
        double burnMul = getBurnMultiplier();
        int amount = (int) Math.ceil(perTick * Math.max(1, parallels) * burnMul);
        if (amount <= 0) return;

        if (tryConsumeResource(primaryBlanket.getInputKey(), amount)) {
            tryOutputResource(primaryBlanket.getOutputKey(), amount);
        }
    }

    // ------------------------------------------------------------------------
    // Recipe modifier (keeps parallels + moderator boosts)
    // ------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------
    // IO helpers (same as your prior breeder class)
    // ------------------------------------------------------------------------

    private boolean tryConsumeResource(String key, int amount) {
        Material mat = GTMaterials.get(key);
        if (mat != null && mat != GTMaterials.NULL) {
            FluidStack fs = safeGetFluid(mat, amount);
            if (!fs.isEmpty()) return tryConsumeFluid(fs);
        }

        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(key));
        if (item != null) {
            return tryConsumeItem(new ItemStack(item, amount));
        }

        return false;
    }

    private void tryOutputResource(String key, int amount) {
        Material mat = GTMaterials.get(key);
        if (mat != null && mat != GTMaterials.NULL) {
            FluidStack fs = safeGetFluid(mat, amount);
            if (!fs.isEmpty()) {
                GTRecipe dummy = com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.ofRaw()
                        .outputFluids(fs)
                        .buildRawRecipe();
                RecipeHelper.handleRecipeIO(this, dummy, IO.OUT, getRecipeLogic().getChanceCaches());
                return;
            }
        }

        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(key));
        if (item != null) {
            GTRecipe dummy = com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.ofRaw()
                    .outputItems(new ItemStack(item, amount))
                    .buildRawRecipe();
            RecipeHelper.handleRecipeIO(this, dummy, IO.OUT, getRecipeLogic().getChanceCaches());
        }
    }

    private boolean tryConsumeFluid(FluidStack fs) {
        if (fs.isEmpty()) return true;

        GTRecipe dummy = com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.ofRaw()
                .inputFluids(fs)
                .buildRawRecipe();

        if (!RecipeHelper.matchRecipe(this, dummy).isSuccess()) return false;

        return RecipeHelper.handleRecipeIO(
                this,
                dummy,
                IO.IN,
                getRecipeLogic().getChanceCaches()
        ).isSuccess();
    }

    private boolean tryConsumeItem(ItemStack stack) {
        if (stack.isEmpty()) return true;

        GTRecipe dummy = com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.ofRaw()
                .inputItems(stack)
                .buildRawRecipe();

        if (!RecipeHelper.matchRecipe(this, dummy).isSuccess()) return false;

        return RecipeHelper.handleRecipeIO(
                this,
                dummy,
                IO.IN,
                getRecipeLogic().getChanceCaches()
        ).isSuccess();
    }

    private FluidStack safeGetFluid(Material mat, int amount) {
        try {
            FluidStack fs = mat.getFluid(amount);
            return fs == null ? FluidStack.EMPTY : fs;
        } catch (Throwable ignored) {
            return FluidStack.EMPTY;
        }
    }
}
