package net.phoenix.core.api.pattern;

import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.network.chat.Component;
import net.phoenix.core.PhoenixAPI;
import net.phoenix.core.api.block.IFissionBlanketType;
import net.phoenix.core.api.block.IFissionCoolerType;
import net.phoenix.core.api.block.IFissionFuelRodType;
import net.phoenix.core.api.block.IFissionModeratorType;
import net.phoenix.core.common.block.FissionBlanketBlock;
import net.phoenix.core.common.block.FissionCoolerBlock;
import net.phoenix.core.common.block.FissionFuelRodBlock;
import net.phoenix.core.common.block.FissionModeratorBlock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PhoenixPredicates {

    public static TraceabilityPredicate fissionCoolers() {
        return new TraceabilityPredicate(blockWorldState -> {
            var blockState = blockWorldState.getBlockState();

            for (Map.Entry<IFissionCoolerType, Supplier<FissionCoolerBlock>> entry : PhoenixAPI.FISSION_COOLERS
                    .entrySet()) {

                if (blockState.is(entry.getValue().get())) {

                    var type = entry.getKey();

                    List<IFissionCoolerType> componentList = blockWorldState
                            .getMatchContext()
                            .getOrPut("CoolerTypes", new ArrayList<>());
                    componentList.add(type);

                    return true;
                }
            }

            return false;
        },
                () -> PhoenixAPI.FISSION_COOLERS.entrySet().stream()
                        .sorted(Comparator.comparingInt(e -> e.getKey().getTier()))
                        .map(e -> BlockInfo.fromBlockState(e.getValue().get().defaultBlockState()))
                        .toArray(BlockInfo[]::new))
                .addTooltips(Component.translatable("phoenix.multiblock.pattern.info.multiple_coolers"));
    }

    public static TraceabilityPredicate fissionBlankets() {
        return new TraceabilityPredicate(blockWorldState -> {
            var blockState = blockWorldState.getBlockState();
            for (Map.Entry<IFissionBlanketType, Supplier<FissionBlanketBlock>> entry : PhoenixAPI.FISSION_BLANKETS
                    .entrySet()) {
                if (blockState.is(entry.getValue().get())) {
                    var type = entry.getKey();
                    List<IFissionBlanketType> componentList = blockWorldState.getMatchContext().getOrPut("BlanketTypes",
                            new ArrayList<>());
                    componentList.add(type);
                    return true;
                }
            }
            return false;
        },
                () -> PhoenixAPI.FISSION_BLANKETS.entrySet().stream()
                        .sorted(Comparator.comparingInt(e -> e.getKey().getTier()))
                        .map(e -> BlockInfo.fromBlockState(e.getValue().get().defaultBlockState()))
                        .toArray(BlockInfo[]::new))
                .addTooltips(Component.translatable("phoenix.multiblock.pattern.info.multiple_blankets"));
    }

    public static TraceabilityPredicate fissionFuelRods() {
        return new TraceabilityPredicate(blockWorldState -> {
            var blockState = blockWorldState.getBlockState();

            for (Map.Entry<IFissionFuelRodType, Supplier<FissionFuelRodBlock>> entry : PhoenixAPI.FISSION_FUEL_RODS
                    .entrySet()) {

                if (blockState.is(entry.getValue().get())) {

                    var type = entry.getKey();

                    List<IFissionFuelRodType> componentList = blockWorldState
                            .getMatchContext()
                            .getOrPut("FuelRodTypes", new ArrayList<>());
                    componentList.add(type);

                    return true;
                }
            }

            return false;
        },
                () -> PhoenixAPI.FISSION_FUEL_RODS.entrySet().stream()
                        .sorted(Comparator.comparingInt(e -> e.getKey().getTier()))
                        .map(e -> BlockInfo.fromBlockState(e.getValue().get().defaultBlockState()))
                        .toArray(BlockInfo[]::new))
                .addTooltips(Component.translatable("phoenix.multiblock.pattern.info.multiple_fuel_rods"));
    }

    public static TraceabilityPredicate fissionModerators() {
        return new TraceabilityPredicate(blockWorldState -> {
            var blockState = blockWorldState.getBlockState();

            for (Map.Entry<IFissionModeratorType, Supplier<FissionModeratorBlock>> entry : PhoenixAPI.FISSION_MODERATORS
                    .entrySet()) {

                if (blockState.is(entry.getValue().get())) {

                    var type = entry.getKey();

                    List<IFissionModeratorType> componentList = blockWorldState
                            .getMatchContext()
                            .getOrPut("ModeratorTypes", new ArrayList<>());
                    componentList.add(type);

                    return true;
                }
            }

            return false;
        },
                () -> PhoenixAPI.FISSION_MODERATORS.entrySet().stream()
                        .sorted(Comparator.comparingInt(e -> e.getKey().getTier()))
                        .map(e -> BlockInfo.fromBlockState(e.getValue().get().defaultBlockState()))
                        .toArray(BlockInfo[]::new))
                .addTooltips(Component.translatable("phoenix.multiblock.pattern.info.multiple_moderators"));
    }
}
