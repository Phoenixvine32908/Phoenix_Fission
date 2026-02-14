package net.phoenix.core.common.data;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static net.phoenix.core.common.data.materials.PhoenixFissionMaterials.*;

public class PhoenixFissionMachineRecipes {

    /*
     * -----------------------------
     * Tiny helpers (greenhouse-style)
     * -----------------------------
     */
    /*
     * 
     * private static int halfTier(int tier) {
     * return VA[tier] / 2;
     * }
     * 
     * private static void assembler(Consumer<FinishedRecipe> out, String recipeId,
     * Consumer<GTRecipeBuilder> spec,
     * int duration, int eut) {
     * GTRecipeBuilder b = GTRecipeTypes.ASSEMBLER_RECIPES.recipeBuilder(id(recipeId));
     * spec.accept(b);
     * b.duration(duration).EUt(eut).save(out);
     * }
     * 
     * private static void centrifuge(Consumer<FinishedRecipe> out, String recipeId,
     * Consumer<GTRecipeBuilder> spec,
     * int duration, int eut) {
     * GTRecipeBuilder b = GTRecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(id(recipeId));
     * spec.accept(b);
     * b.duration(duration).EUt(eut).save(out);
     * }
     * 
     * public static void init(Consumer<FinishedRecipe> out) {
     * assembler(out, "fissile_heat_safe_casing", b -> b
     * .inputItems(TagPrefix.plate, ZIRCALLOY, 6)
     * .inputItems(TagPrefix.frameGt, StainlessSteel, 1)
     * .inputItems(TagPrefix.pipeLargeFluid, Aluminium, 2)
     * .circuitMeta(2)
     * .inputFluids(FROST.getFluid(100))
     * .outputItems(PhoenixFissionBlocks.FISSILE_HEAT_SAFE_CASING.asItem(), 2),
     * 100, halfTier(LV));
     * 
     * assembler(out, "fissile_reaction_safe_casing", b -> b
     * .inputItems(TagPrefix.plate, ZIRCALLOY, 3)
     * .inputItems(TagPrefix.plate, StainlessSteel, 2)
     * .inputItems(TagPrefix.frameGt, ZIRCALLOY, 1)
     * .circuitMeta(6)
     * .inputFluids(StainlessSteel.getFluid(250))
     * .outputItems(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING.asItem(), 2),
     * 100, halfTier(HV));
     * 
     * assembler(out, "fissile_safe_gearbox_casing", b -> b
     * .inputItems(TagPrefix.plate, ZIRCALLOY, 4)
     * .inputItems(TagPrefix.gear, ZIRCALLOY, 2)
     * .inputItems(TagPrefix.frameGt, ZIRCALLOY, 1)
     * .circuitMeta(4)
     * .inputFluids(Gold.getFluid(1000))
     * .outputItems(PhoenixFissionBlocks.FISSILE_SAFE_GEARBOX_CASING.asItem(), 2),
     * 100, halfTier(LV));
     * 
     * assembler(out, "pressurized_fission_reactor", b -> b
     * .inputItems(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING.asItem(), 4)
     * .inputItems(TagPrefix.pipeLargeFluid, StainlessSteel, 8)
     * .inputItems(TagPrefix.frameGt, ZIRCALLOY, 4)
     * .inputItems(CustomTags.HV_CIRCUITS)
     * .inputFluids(SolderingAlloy.getFluid(613))
     * .outputItems(PhoenixFissionMachines.PRESSURIZED_FISSION_REACTOR.asStack(), 1),
     * 150, halfTier(HV));
     * 
     * assembler(out, "heat_exchanger", b -> b
     * .inputItems(PhoenixFissionBlocks.FISSILE_REACTION_SAFE_CASING.asItem(), 2)
     * .inputItems(PhoenixFissionBlocks.FISSILE_HEAT_SAFE_CASING.asItem(), 8)
     * .inputItems(TagPrefix.plateDense, ZIRCALLOY, 2)
     * .inputItems(CustomTags.EV_CIRCUITS)
     * .inputItems(TagPrefix.pipeLargeFluid, Titanium, 2)
     * .inputFluids(SolderingAlloy.getFluid(613))
     * .outputItems(PhoenixFissionMachines.HEAT_EXCHANGER.asStack(), 1),
     * 150, halfTier(EV));
     * 
     * assembler(out, "high_performance_breeder_reactor", b -> b
     * .inputItems(PhoenixFissionMachines.PRESSURIZED_FISSION_REACTOR.asStack(), 1)
     * .inputItems(TagPrefix.rotor, BORON_CARBIDE, 2)
     * .inputItems(CustomTags.IV_CIRCUITS)
     * .inputItems(TagPrefix.gear, ZIRCALLOY, 2)
     * .inputItems(GTMachines.HULL[IV])
     * .inputItems(TagPrefix.rod, Uranium238, 16)
     * .inputFluids(CRYO_GRAPHITE_BINDING_SOLUTION.getFluid(6000))
     * .outputItems(PhoenixFissionMachines.HIGH_PERFORMANCE_BREEDER_REACTOR.asStack(), 1),
     * 800, VA[IV]);
     * 
     * assembler(out, "graphite_moderator", b -> b
     * .inputItems(TagPrefix.plate, Steel, 4)
     * .inputItems(TagPrefix.dust, Graphite, 16)
     * .inputItems(TagPrefix.frameGt, Steel, 1)
     * .inputItems(GTItems.VOLTAGE_COIL_MV, 1)
     * .inputItems(CustomTags.MV_CIRCUITS)
     * .inputFluids(Steel.getFluid(576))
     * .outputItems(PhoenixFissionBlocks.MODERATOR_GRAPHITE.asItem(), 1),
     * 450, VA[IV]);
     * 
     * assembler(out, "basic_fission_cooler", b -> b
     * .inputItems(TagPrefix.frameGt, Steel, 2)
     * .inputItems(TagPrefix.rodLong, Steel, 3)
     * .inputItems(TagPrefix.rotor, Steel, 1)
     * .inputItems(CustomTags.HV_CIRCUITS)
     * .inputFluids(DistilledWater.getFluid(576))
     * .outputItems(PhoenixFissionBlocks.COOLER_BASIC.asItem(), 1),
     * 450, VA[HV]);
     * 
     * GTRecipeTypes.CHEMICAL_BATH_RECIPES.recipeBuilder(id("zirconium_dust_to_hafnium_chloride"))
     * .inputItems(TagPrefix.dust, Zirconium, 4)
     * .inputFluids(HydrochloricAcid.getFluid(1000))
     * .outputFluids(HAFNIUM_CHLORIDE.getFluid(2000))
     * .duration(200)
     * .EUt(halfTier(HV))
     * .save(out);
     * 
     * GTRecipeTypes.CENTRIFUGE_RECIPES.recipeBuilder(id("zircon_dust_processing"))
     * .inputItems(TagPrefix.dust, ZIRCON, 20)
     * .outputItems(TagPrefix.dust, IMPURE_ZIRCONIUM, 8)
     * .outputItems(TagPrefix.dust, IMPURE_HAFNIUM, 4)
     * .duration(400)
     * .EUt(halfTier(HV))
     * .save(out);
     * 
     * GTRecipeTypes.ELECTROLYZER_RECIPES.recipeBuilder(id("hafnium_chloride"))
     * .inputFluids(HAFNIUM_CHLORIDE.getFluid(500))
     * .outputFluids(HydrochloricAcid.getFluid(200))
     * .outputItems(TagPrefix.dust, Hafnium, 1)
     * .duration(100)
     * .EUt(halfTier(HV))
     * .save(out);
     * 
     * GTRecipeTypes.MIXER_RECIPES.recipeBuilder(id("zircalloy_dust"))
     * .inputItems(TagPrefix.dust, Zirconium, 5)
     * .inputItems(TagPrefix.dustSmall, Bismuth, 1) // ✅ dustSmall exists
     * .inputItems(TagPrefix.dust, Hafnium, 2)
     * .outputItems(TagPrefix.dust, ZIRCALLOY, 7)
     * .circuitMeta(15)
     * .duration(100)
     * .EUt(halfTier(HV))
     * .save(out);
     */
}
