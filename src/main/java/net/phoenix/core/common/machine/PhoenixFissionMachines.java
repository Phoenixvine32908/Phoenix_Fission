package net.phoenix.core.common.machine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.common.data.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import net.phoenix.core.PhoenixFission;
import net.phoenix.core.api.pattern.PhoenixPredicates;
import net.phoenix.core.common.block.PhoenixFissionBlocks;
import net.phoenix.core.common.data.PhoenixFissionRecipeTypes;
import net.phoenix.core.common.machine.multiblock.BreederWorkableElectricMultiblockMachine;
import net.phoenix.core.common.machine.multiblock.DynamicFissionReactorMachine;
import net.phoenix.core.common.machine.multiblock.FissionSteamMultiblockMachine;
import net.phoenix.core.common.machine.multiblock.FissionWorkableElectricMultiblockMachine;

import java.util.Locale;
import java.util.function.BiFunction;

import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeModifiers.BATCH_MODE;
import static com.gregtechceu.gtceu.common.data.models.GTMachineModels.*;
import static net.phoenix.core.common.registry.PhoenixFissionRegistration.REGISTRATE;

@SuppressWarnings("all")
public class PhoenixFissionMachines {

    public static final String OVERLAY_PLASMA_HATCH_TEX = "overlay_plasma_hatch_input";
    public static final String OVERLAY_PLASMA_HATCH_HALF_PX_TEX = "overlay_plasma_hatch_half_px_out";
    public static MultiblockMachineDefinition DANCE = null;
    public static MachineDefinition BLAZING_CLEANING_MAINTENANCE_HATCH = null;
    public static MachineDefinition HIGH_YEILD_PHOTON_EMISSION_REGULATER = null;

    static {
        REGISTRATE.creativeModeTab(() -> PhoenixFission.PHOENIX_CREATIVE_TAB);
    }

    public static MachineDefinition[] registerTieredMachines(String name,
                                                             BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
                                                             BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
                                                             int... tiers) {
        MachineDefinition[] definitions = new MachineDefinition[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            var register = REGISTRATE
                    .machine(GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + name,
                            holder -> factory.apply(holder, tier))
                    .tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static final MultiblockMachineDefinition HIGH_PERFORMANCE_BREEDER_REACTOR = REGISTRATE
            .multiblock("high_performance_breeder_reactor", BreederWorkableElectricMultiblockMachine::new)
            .langValue("§bHigh Performance Breeder Reactor")
            .recipeType(PhoenixFissionRecipeTypes.HIGH_PERFORMANCE_BREEDER_REACTOR_RECIPES)
            .generator(true)
            .regressWhenWaiting(false)
            .recipeModifiers(BreederWorkableElectricMultiblockMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .appearanceBlock(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("BBCCCCCBB", "BBDEEEDBB", "BBDEEEDBB", "BBDEEEDBB", "BBDFFFDBB", "BBDFFFDBB", "BBCFFFCBB",
                            "BBCFFFCBB", "BBBBBBBBB")
                    .aisle("BCCCCCCCB", "BGAAAAHGB", "BGAAAAHGB", "BGAAAAHGB", "BGAAAAHGB", "BGAAAAHGB", "BCAAAAHCB",
                            "BCAAAAHCB", "BBCCCCCBB")
                    .aisle("CCCCCCCCC", "DHIAAAIHD", "DHIAAAIHD", "DHIAAAIHD", "DHIAAAIHD", "DHIJJJIHD", "CHIKKKIHC",
                            "CHIAAAIHC", "BCCCCCCCB")
                    .aisle("CCCCCCCCC", "EAALILAAE", "EAALILAAE", "EAALILAAE", "FAALILAAF", "FAJJIJJAF", "FAKKIKKAF",
                            "FAAAIAAAF", "BCCGGGCCB")
                    .aisle("CCCCCCCCC", "EAAIJIAAE", "EAAIJIAAE", "EAAIJIAAE", "FAAIJIAAF", "FAJIJIJAF", "FAKIDIKAF",
                            "FAAIDIAAF", "BCCGJGCCB")
                    .aisle("CCCCCCCCC", "EAALILAAE", "EAALILAAE", "EAALILAAE", "FAALILAAF", "FAJJIJJAF", "FAKKIKKAF",
                            "FAAAIAAAF", "BCCGGGCCB")
                    .aisle("CCCCCCCCC", "DHIAAAIHD", "DHIAAAIHD", "DHIAAAIHD", "DHIAAAIHD", "DHIJJJIHD", "CHIKKKIHC",
                            "CHIAAAIHC", "BCCCCCCCB")
                    .aisle("BCCCCCCCB", "BGHAAAHGB", "BGHAAAHGB", "BGHAAAHGB", "BGHAAAHGB", "BGHAAAHGB", "BCHAAAHCB",
                            "BCHAAAHCB", "BBCCCCCBB")
                    .aisle("BBCCMCCBB", "BBDEEEDBB", "BBDEEEDBB", "BBDEEEDBB", "BBDFFFDBB", "BBDFFFDBB", "BBCFFFCBB",
                            "BBCFFFCBB", "BBBBBBBBB")
                    .where('A', Predicates.air())
                    .where('B', Predicates.any())
                    .where("C", blocks(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING.get()).setMinGlobalLimited(10)
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(Predicates.abilities(PartAbility.SUBSTATION_OUTPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(Predicates.autoAbilities(definition.getRecipeTypes())))
                    .where('D', blocks(PhoenixFissionBlocks.FISSILE_HEAT_SAFE_CASING.get()))
                    .where('E', blocks(Blocks.TINTED_GLASS))
                    .where('F', Predicates.blocks(GCYMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()))
                    .where("G", Predicates.blocks(GCYMBlocks.HEAT_VENT.get()))
                    .where("H", Predicates.blocks(GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.get()))
                    .where('I', PhoenixPredicates.fissionModerators().or(PhoenixPredicates.fissionBlankets()))
                    .where("J", Predicates.blocks(COIL_HSSG.get()))
                    .where("K", PhoenixPredicates.fissionCoolers().or(PhoenixPredicates.fissionFuelRods()))
                    .where("L", Predicates.blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where("M", Predicates.controller(Predicates.blocks(definition.get())))
                    .build())
            .model(
                    createWorkableCasingMachineModel(
                            PhoenixFission.id("block/fission/fissile_reaction_safe_casing"),
                            GTCEu.id("block/multiblock/fusion_reactor")))
            .register();

    public static final MultiblockMachineDefinition PRESSURIZED_FISSION_REACTOR = REGISTRATE
            .multiblock("pressurized_fission_reactor", DynamicFissionReactorMachine::new)
            .langValue("§bPressurized Fission Reactor")
            .recipeType(PhoenixFissionRecipeTypes.PRESSURIZED_FISSION_REACTOR_RECIPES)
            .generator(true)
            .regressWhenWaiting(false)
            .recipeModifiers(DynamicFissionReactorMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .appearanceBlock(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("BCCCB", "CDDDC", "CDDDC", "CDDDC", "BCCCB")
                    .aisle("CEFEC", "DGGGD", "DGGGD", "DGGGD", "CDHDC")
                    .aisle("CFEFC", "DGFGD", "DGFGD", "DGFGD", "CHEHC")
                    .aisle("CEFEC", "DGGGD", "DGGGD", "DGGGD", "CDHDC")
                    .aisle("BCICB", "CDDDC", "CDDDC", "CDDDC", "BCCCB")
                    .where("A", air())
                    .where("B", any())
                    .where("C", blocks(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING.get()).setMinGlobalLimited(12) // Corrected////
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))                                // line
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1))
                            .or(Predicates.autoAbilities(definition.getRecipeTypes())))
                    .where("D", blocks(Blocks.TINTED_GLASS))
                    .where("E", blocks(COIL_KANTHAL.get()))
                    .where('F', PhoenixPredicates.fissionModerators().or(PhoenixPredicates.fissionBlankets()))
                    .where("G", PhoenixPredicates.fissionCoolers().or(PhoenixPredicates.fissionFuelRods()))
                    .where("H", blocks(PhoenixFissionBlocks.FISSILE_HEAT_SAFE_CASING.get()))
                    .where("I", Predicates.controller(Predicates.blocks(definition.get())))
                    .build())
            .model(
                    createWorkableCasingMachineModel(
                            PhoenixFission.id("block/fission/fissile_reaction_safe_casing"),
                            GTCEu.id("block/multiblock/fusion_reactor")))
            .register();
    public static final MultiblockMachineDefinition ADVANCED_PRESSURIZED_FISSION_REACTOR = REGISTRATE
            .multiblock("advanced_pressurized_fission_reactor", FissionSteamMultiblockMachine::new)
            .langValue("§bAdvanced Pressurized Fission Reactor")
            .recipeType(PhoenixFissionRecipeTypes.ADVANCED_PRESSURIZED_FISSION_REACTOR_RECIPES)
            .generator(true)
            .regressWhenWaiting(false)
            .recipeModifiers(FissionSteamMultiblockMachine::recipeModifier,
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK))
            .appearanceBlock(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("BCCCCCB", "CDDDDDC", "CDDDDDC", "CDDDDDC", "CDDDDDC", "CDDDDDC", "BCCCCCB")
                    .aisle("CCCCCCC", "DAAEAAD", "DAAEAAD", "DAAEAAD", "DAAEAAD", "DAAEAAD", "CDDFDDC")
                    .aisle("CCGEGCC", "DAHHHAD", "DAHHHAD", "DAHHHAD", "DAIIIAD", "DAEEEAD", "CDFGFDC")
                    .aisle("CCEEECC", "DEHEHED", "DEHEHED", "DEHEHED", "DEIEIED", "DEEEEED", "CFGFGFC")
                    .aisle("CCGEGCC", "DAHHHAD", "DAHHHAD", "DAHHHAD", "DAIIIAD", "DAEEEAD", "CDFGFDC")
                    .aisle("CCCCCCC", "DAAEAAD", "DAAEAAD", "DAAEAAD", "DAAEAAD", "DAAEAAD", "CDDFDDC")
                    .aisle("BCCJCCB", "CDDDDDC", "CDDDDDC", "CDDDDDC", "CDDDDDC", "CDDDDDC", "BCCCCCB")
                    .where("A", air())
                    .where("B", any())
                    .where("C", blocks(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING.get()).setMinGlobalLimited(12) // Corrected////
                            // line
                            .or(Predicates.abilities(PartAbility.IMPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_ITEMS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.IMPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.EXPORT_FLUIDS).setPreviewCount(1))
                            .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                    .where("D", blocks(Blocks.TINTED_GLASS))
                    .where("E", PhoenixPredicates.fissionModerators())
                    .where("F", blocks(PhoenixFissionBlocks.FISSILE_HEAT_SAFE_CASING.get()))
                    .where("G", Predicates.blocks(COIL_NICHROME.get()))
                    .where("H", PhoenixPredicates.fissionCoolers())
                    .where("I", blocks(Blocks.ICE))
                    .where("J", Predicates.controller(Predicates.blocks(definition.get())))
                    .build())
            .model(
                    createWorkableCasingMachineModel(
                            PhoenixFission.id("block/fission/fissile_reaction_safe_casing"),
                            GTCEu.id("block/multiblock/fusion_reactor")))
            .register();
    public static final MultiblockMachineDefinition HEAT_EXCHANGER = REGISTRATE
            .multiblock("heat_exchanger", WorkableElectricMultiblockMachine::new)
            .langValue("§bHeat Exchanger")
            .recipeType(PhoenixFissionRecipeTypes.HEAT_EXCHANGER_RECIPES)
            .recipeModifiers(
                    GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK),
                    BATCH_MODE)
            .appearanceBlock(PhoenixFissionBlocks.FISSILE_HEAT_SAFE_CASING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("BBBBBBB", "BCCCCCB", "BCCCCCB", "BCCCCCB", "BBBBBBB")
                    .aisle("BBBBBBB", "BAAAAAB", "BAAEAAB", "BAAAAAB", "BBCBCBB")
                    .aisle("BBBBBBB", "BAEAEAB", "BEEEEEB", "BAEAEAB", "BCCBCCB")
                    .aisle("BBBBBBB", "BAAAAAB", "BAAEAAB", "BAAAAAB", "BBCBCBB")
                    .aisle("BBBBBBB", "BCCCCCB", "BCCDCCB", "BCCCCCB", "BBBBBBB")
                    .where("A", air())
                    .where("B", blocks(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING.get()))
                    .where("C", blocks(PhoenixFissionBlocks.FISSILE_HEAT_SAFE_CASING.get()).setMinGlobalLimited(6)
                            .or(Predicates.autoAbilities(definition.getRecipeTypes())))
                    .where("D", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("E", blocks(PhoenixFissionBlocks.FISSILE_SAFE_GEARBOX_CASING.get()))
                    .build())
            .model(
                    createWorkableCasingMachineModel(
                            PhoenixFission.id("block/fission/fissile_heat_safe_casing"),
                            GTCEu.id("block/multiblock/fusion_reactor")))
            .register();

    public static final MultiblockMachineDefinition ALCHEMICAL_IMBUER = REGISTRATE
            .multiblock("alchemical_imbuer", WorkableElectricMultiblockMachine::new)
            .langValue("§5Alchemical Imbuer")
            .recipeTypes(PhoenixFissionRecipeTypes.SOURCE_EXTRACTION_RECIPES,
                    PhoenixFissionRecipeTypes.SOURCE_IMBUEMENT_RECIPES) // PhoenixFissionRecipeTypes.SOURCE_IMBUMENT_RECIPES)//"SOURCE_IMBUMENT_RECIPES","SOURCE_EXTRACTION_RECIPES")
            .recipeModifiers(GTRecipeModifiers.OC_NON_PERFECT_SUBTICK, BATCH_MODE)
            .appearanceBlock(GTBlocks.CASING_TITANIUM_STABLE)
            .rotationState(RotationState.NON_Y_AXIS)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("BBCCCBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBCCCBB")
                    .aisle("BCDDDCB", "BBCCCBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBCCCBB", "BCDDDCB")
                    .aisle("CDDDDDC", "BCEEECB", "BBFFFBB", "BBFFFBB", "BBFFFBB", "BCDDDCB", "CDGGGDC")
                    .aisle("CDDDDDC", "BCEEECB", "BBFEFBB", "BBFHFBB", "BBFEFBB", "BCDIDCB", "CDGJGDC")
                    .aisle("CDDDDDC", "BCEEECB", "BBFFFBB", "BBFFFBB", "BBFFFBB", "BCDDDCB", "CDGGGDC")
                    .aisle("BCDDDCB", "BBCCCBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBCCCBB", "BCDDDCB")
                    .aisle("BBCKCBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBCCCBB")
                    .where("B", Predicates.any())
                    .where("C",
                            Predicates.blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                                    .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                                    .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                    .where("D",
                            Predicates.blocks(
                                    ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse("ars_nouveau:sourcestone"))))
                    .where("E",
                            Predicates.blocks(ForgeRegistries.BLOCKS
                                    .getValue(ResourceLocation.parse("ars_nouveau:magebloom_block"))))
                    .where("F", Predicates.blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where("G",
                            Predicates.blocks(
                                    ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse("ars_nouveau:void_prism"))))
                    .where("H",
                            Predicates.blocks(ForgeRegistries.BLOCKS
                                    .getValue(ResourceLocation.parse("ars_nouveau:source_gem_block"))))
                    .where("I",
                            Predicates.blocks(
                                    ForgeRegistries.BLOCKS.getValue(ResourceLocation.parse("ars_nouveau:arcane_core"))))
                    .where("J",
                            Predicates.blocks(ForgeRegistries.BLOCKS
                                    .getValue(ResourceLocation.parse("ars_nouveau:agronomic_sourcelink"))))
                    .where("K", Predicates.controller(Predicates.blocks(definition.get())))
                    .build())
            .workableCasingModel(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                    GTCEu.id("block/multiblock/implosion_compressor"))
            .register();

    public static void init() {}
}
