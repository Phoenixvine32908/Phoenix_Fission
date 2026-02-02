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

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.phoenix.core.PhoenixAPI;
import net.phoenix.core.api.block.IFissionBlanketType;
import net.phoenix.core.api.block.IFissionFuelRodType;
import net.phoenix.core.configs.PhoenixConfigs;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@MethodsReturnNonnullByDefault
public class BreederWorkableElectricMultiblockMachine extends DynamicFissionReactorMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BreederWorkableElectricMultiblockMachine.class,
            DynamicFissionReactorMachine.MANAGED_FIELD_HOLDER);

    private transient List<IFissionBlanketType> activeBlankets = new ArrayList<>();

    @Nullable
    @Getter
    private transient IFissionBlanketType primaryBlanket = null;

    @Persisted
    private int blanketCycleTicks = 0;

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
            // noinspection unchecked
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
        // parallels still matter for scaling before base logic uses them
        if (running) {
            lastParallels = Math.max(1, computeParallels());
        }

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

        // Only fuel should determine whether the reactor can run.
        // Blankets are a side-process; they should never prevent startup.
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
        if (activeBlankets == null || activeBlankets.isEmpty()) return;

        // Use primary blanket duration as the pacing clock (simple + predictable)
        IFissionBlanketType primary = primaryBlanket != null ? primaryBlanket : activeBlankets.get(0);

        int duration = Math.max(1, primary.getDurationTicks());
        blanketCycleTicks++;

        if (blanketCycleTicks < duration) return;

        double burnMul = getBurnMultiplier();
        int p = Math.max(1, parallels);

        if (!cfg.blanketUsageAdditive) {
            // Primary-only mode
            int basePerCycle = Math.max(0, primary.getAmountPerCycle());
            int amount = (int) Math.ceil(basePerCycle * p * burnMul);
            if (amount > 0) {
                if (tryConsumeResource(primary.getInputKey(), amount)) {
                    tryOutputResource(primary.getOutputKey(), amount);
                }
            }
            blanketCycleTicks = 0;
            return;
        }

        // Additive mode: group by input->output key and sum per-cycle demand
        Map<String, Integer> basePerCycleByPair = new HashMap<>();
        Map<String, String> pairToIn = new HashMap<>();
        Map<String, String> pairToOut = new HashMap<>();

        for (var blanket : activeBlankets) {
            int base = Math.max(0, blanket.getAmountPerCycle());
            if (base <= 0) continue;

            String in = blanket.getInputKey();
            String out = blanket.getOutputKey();
            String pair = in + "->" + out;

            basePerCycleByPair.merge(pair, base, Integer::sum);
            pairToIn.putIfAbsent(pair, in);
            pairToOut.putIfAbsent(pair, out);
        }

        for (var e : basePerCycleByPair.entrySet()) {
            String pair = e.getKey();
            int base = e.getValue();
            int amount = (int) Math.ceil(base * p * burnMul);
            if (amount <= 0) continue;

            String in = pairToIn.get(pair);
            String out = pairToOut.get(pair);

            if (tryConsumeResource(in, amount)) {
                tryOutputResource(out, amount);
            }
        }

        blanketCycleTicks = 0;
    }

    private String keyToPrettyName(String key) {
        ResourceLocation rl = ResourceLocation.tryParse(key);
        if (rl != null) {
            Item it = ForgeRegistries.ITEMS.getValue(rl);
            if (it != null && it != net.minecraft.world.item.Items.AIR) {
                return new ItemStack(it, 1).getHoverName().getString();
            }
            var fl = ForgeRegistries.FLUIDS.getValue(rl);
            if (fl != null && fl != Fluids.EMPTY) {
                return Component.translatable(fl.getFluidType().getDescriptionId()).getString();
            }
        }
        return key;
    }

    private boolean canConsumeResource(String key, int amount) {
        if (amount <= 0) return true;

        FluidStack fs = resolveKeyToFluid(key, amount);
        if (!fs.isEmpty()) return canConsumeFluid(fs);

        ItemStack is = resolveKeyToItem(key, amount);
        if (!is.isEmpty()) return canConsumeItem(is);

        return false;
    }

    private boolean canConsumeFluid(FluidStack fs) {
        if (fs.isEmpty()) return true;
        GTRecipe dummy = com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.ofRaw()
                .inputFluids(fs)
                .buildRawRecipe();
        return RecipeHelper.matchRecipe(this, dummy).isSuccess();
    }

    private boolean canConsumeItem(ItemStack stack) {
        if (stack.isEmpty()) return true;
        GTRecipe dummy = com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.ofRaw()
                .inputItems(stack)
                .buildRawRecipe();
        return RecipeHelper.matchRecipe(this, dummy).isSuccess();
    }

    public static ModifierFunction recipeModifier(MetaMachine machine, GTRecipe recipe) {
        if (!(machine instanceof BreederWorkableElectricMultiblockMachine m)) {
            return com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier
                    .nullWrongType(BreederWorkableElectricMultiblockMachine.class, machine);
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

    @Nullable
    private FluidStack resolveKeyToFluid(String key, int amount) {
        if (amount <= 0) return FluidStack.EMPTY;

        Material mat = GTMaterials.get(key);
        if (mat != null && mat != GTMaterials.NULL) {
            FluidStack fs = safeGetFluid(mat, amount);
            if (!fs.isEmpty()) return fs;
        }

        ResourceLocation rl = ResourceLocation.tryParse(key);
        if (rl != null) {
            Fluid f = ForgeRegistries.FLUIDS.getValue(rl);
            if (f != null && f != Fluids.EMPTY) {
                return new FluidStack(f, amount);
            }

            Fluid f2 = ForgeRegistries.FLUIDS
                    .getValue(new ResourceLocation(rl.getNamespace(), "flowing_" + rl.getPath()));
            if (f2 != null && f2 != Fluids.EMPTY) {
                return new FluidStack(f2, amount);
            }

            // Optional fallback: if key is flowing_, try base
            if (rl.getPath().startsWith("flowing_")) {
                String base = rl.getPath().substring("flowing_".length());
                Fluid f3 = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(rl.getNamespace(), base));
                if (f3 != null && f3 != Fluids.EMPTY) {
                    return new FluidStack(f3, amount);
                }
            }
        }

        return FluidStack.EMPTY;
    }

    @Nullable
    private ItemStack resolveKeyToItem(String key, int amount) {
        if (amount <= 0) return ItemStack.EMPTY;

        ResourceLocation rl = ResourceLocation.tryParse(key);
        if (rl == null) return ItemStack.EMPTY;

        Item item = ForgeRegistries.ITEMS.getValue(rl);
        if (item == null || item == net.minecraft.world.item.Items.AIR) return ItemStack.EMPTY;

        return new ItemStack(item, amount);
    }

    // ------------------------------------------------------------------------
    // IO helpers (same as your prior breeder class)
    // ------------------------------------------------------------------------

    private boolean tryConsumeResource(String key, int amount) {
        if (amount <= 0) return true;

        FluidStack fs = resolveKeyToFluid(key, amount);
        if (!fs.isEmpty()) return tryConsumeFluid(fs);

        ItemStack is = resolveKeyToItem(key, amount);
        if (!is.isEmpty()) return tryConsumeItem(is);

        return false;
    }

    private void tryOutputResource(String key, int amount) {
        if (amount <= 0) return;

        FluidStack fs = resolveKeyToFluid(key, amount);
        if (!fs.isEmpty()) {
            GTRecipe dummy = com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.ofRaw()
                    .outputFluids(fs)
                    .buildRawRecipe();
            RecipeHelper.handleRecipeIO(this, dummy, IO.OUT, getRecipeLogic().getChanceCaches());
            return;
        }

        ItemStack is = resolveKeyToItem(key, amount);
        if (!is.isEmpty()) {
            GTRecipe dummy = com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder.ofRaw()
                    .outputItems(is)
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
                getRecipeLogic().getChanceCaches()).isSuccess();
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
                getRecipeLogic().getChanceCaches()).isSuccess();
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
