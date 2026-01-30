package net.phoenix.core.common.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.phoenix.core.api.block.IFissionModeratorType;
import net.phoenix.core.configs.PhoenixConfigs;

import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class FissionSteamMultiblockMachine extends DynamicFissionReactorMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            FissionSteamMultiblockMachine.class,
            FissionWorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER
    );

    public FissionSteamMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    protected void handleReactorLogic(boolean running) {
        // reset per-tick fields
        lastHeatGainedPerTick = 0.0;
        lastHeatRemovedPerTick = 0.0;
        lastGeneratedEUt = 0;

        if (!running) {
            if (cfg().passiveCooling) {
                heat -= cfg().idleHeatLoss;
                clampHeat();
            }
            tickMeltdown();
            return;
        }

        if (activeFuelRods.isEmpty()) {
            tickMeltdown();
            return;
        }

        // Parallels while running
        lastParallels = Math.max(1, computeParallels());
        applyParallelsToRecipeLogic(lastParallels);

        // Heat production
        double heatProduced = computeHeatProducedPerTick(lastParallels);
        heat += heatProduced;
        lastHeatGainedPerTick = heatProduced;

        // Cooling capacity
        lastProvidedCooling = activeCoolers.stream()
                .mapToInt(c -> c.getCoolerTemperature())
                .sum();

        // ✅ Coolant (machine-driven)
        lastHasCoolant = consumeCoolantForThisTickMachineDriven();

        // Apply cooling to heat
        double removed = 0.0;
        if (!cfg().coolingRequiresCoolant || lastHasCoolant) {
            removed = Math.min(heat, (double) lastProvidedCooling);
            heat -= removed;
        }
        lastHeatRemovedPerTick = removed;

        clampHeat();

        // ✅ Fuel consumption (machine-driven)
        tickFuelConsumptionMachineDriven(lastParallels);

        // Meltdown based on heat
        tickMeltdown();
    }

    /**
     * Steam-specific recipe modifier:
     * - Apply moderator fuel discount as duration multiplier
     * - Apply parallels scaling for IO
     * - Ignore EU boosts entirely
     */
    public static ModifierFunction recipeModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        if (!(machine instanceof FissionSteamMultiblockMachine m) || !m.isFormed()) {
            return ModifierFunction.IDENTITY;
        }

        var cfg = PhoenixConfigs.INSTANCE.fission;

        int parallels = Math.max(1, m.computeParallels());

        int discount = m.getActiveModerators().stream()
                .mapToInt(IFissionModeratorType::getFuelDiscount)
                .sum();
        int clamped = Math.min(cfg.maxFuelDiscountPercent, discount);

        double durationMult = Math.max(0.01, 1.0 - (clamped / 100.0));

        var b = ModifierFunction.builder();

        if (parallels > 1) {
            b.inputModifier(com.gregtechceu.gtceu.api.recipe.content.ContentModifier.multiplier(parallels))
                    .outputModifier(com.gregtechceu.gtceu.api.recipe.content.ContentModifier.multiplier(parallels))
                    .parallels(parallels);
        }

        return b.durationMultiplier(durationMult).build();
    }
}
