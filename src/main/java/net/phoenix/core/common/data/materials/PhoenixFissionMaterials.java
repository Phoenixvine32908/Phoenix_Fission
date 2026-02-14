package net.phoenix.core.common.data.materials;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialFlags;
import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.IngotProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.OreProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.phoenix.core.PhoenixFission;

import static com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.MID;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class PhoenixFissionMaterials {

    public static Material FROST;
    public static Material BORON_CARBIDE;
    public static Material NIOBIUM_MODIFIED_SILICON_CARBIDE;
    public static Material CRYO_GRAPHITE_BINDING_SOLUTION;
    public static Material HAFNIUM_CHLORIDE;
    public static Material ZIRCALLOY;
    public static Material ZIRCON;

    public static Material HOT_SODIUM_POTASSIUM;
    public static Material MEDIUM_PRESSURE_FISSILE_STEAM;
    public static Material CRITICAL_STEAM;

    public static Material FISSION_PRODUCTS_FLUID;
    public static Material RADIOACTIVE_SLUDGE;
    public static Material RADIOACTIVE_GAS_MIXTURE;
    public static Material INERT_GAS_WASTE;
    public static Material IMPURE_ZIRCONIUM;
    public static Material IMPURE_HAFNIUM;

    public static Material RHODIUM_PALLADIUM_SOLUTION;
    public static Material TECHNETIUM_STRONTIUM_SOLUTION;
    public static Material GASEOUS_FISSION_BYPRODUCTS;
    public static Material ACIDIC_WASTE;
    public static Material TRACE_FISSION_GASES;
    public static Material PURIFIED_RADIOACTIVE_WASTE_FLUID;

    public static Material EXOTIC_FISSION_CONCENTRATE;
    public static Material EXOTIC_FISSILE_MATERIALS_CLUMP;
    public static Material FISSILE_ASH;
    public static Material TRACE_ACTINIDES;

    public static void register() {
        ZIRCON = new Material.Builder(PhoenixFission.id("zircon"))
                .dust()
                .color(0xC2B280)
                .secondaryColor(0x8B7D6B)
                .iconSet(MaterialIconSet.DULL)
                .addOreByproducts(SiliconDioxide)
                .formula("ZrSiO4")
                .buildAndRegister();
        FROST = new Material.Builder(PhoenixFission.id("frost"))
                .langValue("§bFrost")
                .fluid()
                .color(0xA7D1EB)
                .secondaryColor(0x778899)
                .iconSet(MaterialIconSet.SHINY)
                .buildAndRegister();

        BORON_CARBIDE = new Material.Builder(PhoenixFission.id("boron_carbide"))
                .ingot()
                .color(0x353630)
                .iconSet(MaterialIconSet.DULL)
                .blastTemp(3600, com.gregtechceu.gtceu.api.data.chemical.material.properties.BlastProperty.GasTier.LOW,
                        500, 1500)
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_ROD, MaterialFlags.GENERATE_DENSE)
                .formula("B4C")
                .buildAndRegister();

        NIOBIUM_MODIFIED_SILICON_CARBIDE = new Material.Builder(PhoenixFission.id("niobium_modified_silicon_carbide"))
                .ingot()
                .color(0x4A4B6B)
                .secondaryColor(0x101021)
                .iconSet(MaterialIconSet.METALLIC)
                .flags(MaterialFlags.GENERATE_PLATE, MaterialFlags.GENERATE_FRAME, MaterialFlags.GENERATE_FOIL)
                .blastTemp(4500, BlastProperty.GasTier.MID,
                        2000, 1800)
                .formula("Nb(SiC)x")
                .buildAndRegister();
        IMPURE_ZIRCONIUM = new Material.Builder(PhoenixFission.id("impure_zirconium"))
                .langValue("Impure Zirconium")
                .dust()
                .color(0x7F8C8D)
                .secondaryColor(0x3A3F44)
                .iconSet(MaterialIconSet.DULL)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION)
                .buildAndRegister();

        IMPURE_HAFNIUM = new Material.Builder(PhoenixFission.id("impure_hafnium"))
                .langValue("Impure Hafnium")
                .dust()
                .color(0xA9A9A9)
                .secondaryColor(0x4B4B4B)
                .iconSet(MaterialIconSet.DULL)
                .flags(MaterialFlags.DISABLE_DECOMPOSITION)
                .buildAndRegister();

        CRYO_GRAPHITE_BINDING_SOLUTION = new Material.Builder(PhoenixFission.id("cryo_graphite_binding_solution"))
                .fluid()
                .color(0x507080)
                .secondaryColor(0x7090A0)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        HAFNIUM_CHLORIDE = new Material.Builder(PhoenixFission.id("hafnium_chloride"))
                .fluid()
                .color(0xC0C0C0)
                .iconSet(MaterialIconSet.BRIGHT)
                .buildAndRegister();

        ZIRCALLOY = new Material.Builder(PhoenixFission.id("zircalloy"))
                .ingot().dust()
                .color(0x002327)
                .secondaryColor(0x000000)
                .iconSet(MaterialIconSet.DULL)
                .flags(
                        MaterialFlags.GENERATE_PLATE,
                        MaterialFlags.GENERATE_GEAR,
                        MaterialFlags.GENERATE_SMALL_GEAR,
                        MaterialFlags.GENERATE_ROD,
                        MaterialFlags.GENERATE_DENSE,
                        MaterialFlags.GENERATE_FOIL,
                        MaterialFlags.GENERATE_SPRING,
                        MaterialFlags.GENERATE_FRAME)
                .buildAndRegister();

        HOT_SODIUM_POTASSIUM = new Material.Builder(PhoenixFission.id("hot_sodium_potassium"))
                .fluid()
                .color(0xFF4500)
                .secondaryColor(0xFFD700)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        MEDIUM_PRESSURE_FISSILE_STEAM = new Material.Builder(PhoenixFission.id("medium_pressure_fissile_steam"))
                .fluid()
                .color(0x7DA10E)
                .iconSet(MaterialIconSet.BRIGHT)
                .buildAndRegister();

        CRITICAL_STEAM = new Material.Builder(PhoenixFission.id("critical_steam"))
                .gas()
                .color(0xF0F8FF)
                .secondaryColor(0xADD8E6)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        FISSION_PRODUCTS_FLUID = new Material.Builder(PhoenixFission.id("fission_products_fluid"))
                .fluid()
                .color(0x556B2F)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        RADIOACTIVE_SLUDGE = new Material.Builder(PhoenixFission.id("radioactive_sludge"))
                .fluid()
                .color(0x2F4F4F)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        RADIOACTIVE_GAS_MIXTURE = new Material.Builder(PhoenixFission.id("radioactive_gas_mixture"))
                .gas()
                .color(0x6B8E23)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        INERT_GAS_WASTE = new Material.Builder(PhoenixFission.id("inert_gas_waste"))
                .gas()
                .color(0x808080)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        RHODIUM_PALLADIUM_SOLUTION = new Material.Builder(PhoenixFission.id("rhodium_palladium_solution"))
                .fluid()
                .color(0x9E9E9E)
                .iconSet(MaterialIconSet.SHINY)
                .buildAndRegister();

        TECHNETIUM_STRONTIUM_SOLUTION = new Material.Builder(PhoenixFission.id("technetium_strontium_solution"))
                .fluid()
                .color(0x7CFC00)
                .iconSet(MaterialIconSet.BRIGHT)
                .buildAndRegister();

        GASEOUS_FISSION_BYPRODUCTS = new Material.Builder(PhoenixFission.id("gaseous_fission_byproducts"))
                .gas()
                .color(0xB0C4DE)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        ACIDIC_WASTE = new Material.Builder(PhoenixFission.id("acidic_waste"))
                .fluid()
                .color(0x9ACD32)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        TRACE_FISSION_GASES = new Material.Builder(PhoenixFission.id("trace_fission_gases"))
                .gas()
                .color(0xC0FFEE)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        PURIFIED_RADIOACTIVE_WASTE_FLUID = new Material.Builder(PhoenixFission.id("purified_radioactive_waste_fluid"))
                .fluid()
                .color(0x3B5323)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        EXOTIC_FISSION_CONCENTRATE = new Material.Builder(PhoenixFission.id("exotic_fission_concentrate"))
                .dust()
                .color(0x800080)
                .secondaryColor(0x00FF00)
                .iconSet(MaterialIconSet.RADIOACTIVE)
                .buildAndRegister();

        EXOTIC_FISSILE_MATERIALS_CLUMP = new Material.Builder(PhoenixFission.id("exotic_fissile_materials_clump"))
                .dust()
                .color(0x556B2F)
                .secondaryColor(0x00FF00)
                .iconSet(MaterialIconSet.RADIOACTIVE)
                .buildAndRegister();

        FISSILE_ASH = new Material.Builder(PhoenixFission.id("fissile_ash"))
                .dust()
                .color(0x444444)
                .iconSet(MaterialIconSet.DULL)
                .buildAndRegister();

        TRACE_ACTINIDES = new Material.Builder(PhoenixFission.id("trace_actinides"))
                .dust()
                .color(0x2E8B57)
                .iconSet(MaterialIconSet.RADIOACTIVE)
                .buildAndRegister();

        GTMaterials.Zirconium.setProperty(PropertyKey.INGOT, new IngotProperty());
        GTMaterials.Hafnium.setProperty(PropertyKey.INGOT, new IngotProperty());
        GTMaterials.Hafnium.setProperty(PropertyKey.ORE, new OreProperty());
        GTMaterials.Zirconium.setProperty(PropertyKey.ORE, new OreProperty());
    }
}
