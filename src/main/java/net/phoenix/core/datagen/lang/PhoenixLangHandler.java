package net.phoenix.core.datagen.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class PhoenixLangHandler {

    public static void init(RegistrateLangProvider provider) {
        provider.add("phoenix.fission.not_formed", "Structure not formed!");
        provider.add("phoenix.fission.status.safe_idle", "Status: §aIDLE");
        provider.add("phoenix.fission.status.safe_working", "Status: §6ACTIVE");
        provider.add("phoenix.fission.status.danger_timer", "§cCRITICAL: Meltdown in %s seconds!");
        provider.add("phoenix.fission.status.no_coolant", "§eWARNING: Coolant Supply Exhausted");
        provider.add("config.jade.plugin_phoenix_fission.fission_machine_info", "Fission Machine Info");
        // NEW: blanket distribution tooltip header
        provider.add("phoenix.fission.blanket_outputs", "§7Possible Products:");
        provider.add("phoenix.fission.coolant_output", "Hot Coolant Produced: %s");

        // NEW: spectrum/bias stats (fuel + moderators affect blanket output distribution)
        provider.add("phoenix.fission.neutron_bias", "§7Neutron Bias: §f%s");
        provider.add("phoenix.fission.spectrum_shift", "§7Spectrum Shift: §f%s");

        provider.add("phoenix.fission.current_heat", "Core Temperature: %s HU");
        provider.add("phoenix.fission.net_heat", "Net Heat Change: %s HU/t");
        provider.add("phoenix.fission.eu_generation", "Output: %s EU/t");
        provider.add("phoenix.fission.parallels", "Parallel Processing: %sx");
        provider.add("phoenix.fission.heat_production", "Heat Production: %s");
        provider.add("phoenix.fission.nuke_radius", "Blast area: %s");

        provider.add("phoenix.fission.moderator", "Primary Moderator: %s");
        provider.add("phoenix.fission.moderator_fuel_discount", "Fuel Efficiency: +%s%%");
        provider.add("phoenix.fission.cooler", "Primary Cooling: %s");
        provider.add("phoenix.fission.coolant", "Coolant: %s");
        provider.add("phoenix.fission.coolant_rate", "Coolant Flow: %s mb/t");
        provider.add("phoenix.fission.coolant_status.ok", "§aCoolant Supply OK");
        provider.add("phoenix.fission.coolant_status.empty", "§cCoolant Supply Depleted");
        provider.add("phoenix.fission.summary", "Cooling: %s / %s HU/t");
        provider.add("phoenix.fission.blanket_input", "§7Target Material: §f%s");
        provider.add("phoenix.fission.blanket_output", "§7Breeding Product: §f%s");
        provider.add("phoenix.fission.blanket_desc", "Irradiate target materials to produce specialized isotopes.");
        provider.add("jade.phoenix_fission.blanket_input", "Blanket Fuel: %s");
        provider.add("jade.phoenix_fission.blanket_output", "Breeding Product: %s");
        provider.add("jade.phoenix_fission.blanket_amount", "Base per cycle: %s");

        provider.add("jade.phoenix_fission.heat", "§cCore Heat: %s HU");
        provider.add("jade.phoenix_fission.fission_meltdown_timer", "§6MELTDOWN: %s seconds!");
        provider.add("jade.phoenix_fission.fission_safe", "§aCore Stable");
        provider.add("jade.phoenix_fission.fission_no_coolant", "§cNO COOLANT DETECTED");
        provider.add("jade.phoenix_fission.fission_heating", "§eCORE HEATING UP");
        provider.add("phoenix.fission.fuel_cycle", "Consumes §f%s§7 units every §6%s§7 seconds");
        provider.add("phoenix.fission.depleted_fuel", "§7Depleted Fuel: §f%s");
        provider.add("phoenix.fission.blanket_cycle", "Transmutes §f%s§7 units every §6%s§7 seconds");

        provider.add("phoenix.fission.fuel_usage", "Fuel Consumption: §f%s");

        provider.add("phoenix.fission.fuel_required", "§7Requires Fuel: §f%s");

        provider.add("phoenix.fission.coolant_required", "§3Required Coolant: §f%s");
        provider.add("phoenix.fission.cooling_power", "§bCooling Capacity: §f%s HU/t");

        provider.add("block.phoenix_fission.fission_cooler.capacity", "§bCooling Capacity: §f%s HU/t");
        provider.add("block.phoenix_fission.fission_cooler.required_coolant", "§3Required Coolant: §f%s");

        provider.add("block.phoenix_fission.fission_moderator.multiplier", "§6Heat Multiplier: §f%sx");
        provider.add("block.phoenix_fission.fission_moderator.parallel", "§aParallel Bonus: §f+%s");

        provider.add("phoenix.multiblock.pattern.info.multiple_fuel_rods",
                "Requires Fuel Rods. These generate base heat and determine recipe parallels.");
        provider.add("phoenix.multiblock.pattern.info.multiple_blankets",
                "Requires Blanket Rods. These act as targets for transmutation in Breeder cycles.");
        provider.add("phoenix.multiblock.pattern.info.multiple_moderators",
                "Moderators adjust heat generation and can provide EU or Parallel bonuses.");
        provider.add("phoenix.multiblock.pattern.info.multiple_coolers",
                "Coolers remove heat based on their tier and provided coolant fluid.");

        provider.add("gtceu.recipe_type.phoenix_fission.high_performance_breeder_reactor",
                "High-Performance Breeder Reactor");
        provider.add("gtceu.recipe_type.phoenix_fission.advanced_pressurized_fission_reactor",
                "Advanced Pressurized Fission Reactor");
        provider.add("gtceu.recipe_type.phoenix_fission.pressurized_fission_reactor", "Pressurized Fission Reactor");

        provider.add("gtceu.tooltip.tier", "Tier: %s");
        provider.add("block.phoenix_fission.fission_moderator.shift",
                "Hold Shift for details");

        provider.add("block.phoenix_fission.fission_moderator.info_header",
                "Fission Moderator");

        provider.add("block.phoenix_fission.fission_moderator.boost",
                "EU Boost: %s");

        provider.add("block.phoenix_fission.fission_moderator.fuel_discount",
                "Fuel Discount: %s");

        addMaterialLang(provider, "zircon", "Zircon");
        addMaterialLang(provider, "frost", "Frost");
        addMaterialLang(provider, "boron_carbide", "Boron Carbide");
        addMaterialLang(provider, "niobium_modified_silicon_carbide", "Niobium Modified Silicon Carbide");
        addMaterialLang(provider, "impure_zirconium", "Impure Zirconium");
        addMaterialLang(provider, "impure_hafnium", "Impure Hafnium");
        addMaterialLang(provider, "zircalloy", "Zircalloy");
        addMaterialLang(provider, "exotic_fission_concentrate", "Exotic Fission Concentrate");
        addMaterialLang(provider, "exotic_fissile_materials_clump", "Exotic Fissile Materials Clump");
        addMaterialLang(provider, "fissile_ash", "Fissile Ash");
        addMaterialLang(provider, "trace_actinides", "Trace Actinides");

        // Solutions and Fluids
        addMaterialLang(provider, "cryo_graphite_binding_solution", "Cryo-Graphite Binding Solution");
        addMaterialLang(provider, "hafnium_chloride", "Hafnium Chloride");
        addMaterialLang(provider, "hot_sodium_potassium", "Hot Sodium-Potassium");
        addMaterialLang(provider, "medium_pressure_fissile_steam", "Medium Pressure Fissile Steam");
        addMaterialLang(provider, "critical_steam", "Critical Steam");
        addMaterialLang(provider, "fission_products_fluid", "Fission Products Fluid");
        addMaterialLang(provider, "radioactive_sludge", "Radioactive Sludge");
        addMaterialLang(provider, "radioactive_gas_mixture", "Radioactive Gas Mixture");
        addMaterialLang(provider, "inert_gas_waste", "Inert Gas Waste");
        addMaterialLang(provider, "rhodium_palladium_solution", "Rhodium-Palladium Solution");
        addMaterialLang(provider, "technetium_strontium_solution", "Technetium-Strontium Solution");
        addMaterialLang(provider, "gaseous_fission_byproducts", "Gaseous Fission Byproducts");
        addMaterialLang(provider, "acidic_waste", "Acidic Waste");
        addMaterialLang(provider, "trace_fission_gases", "Trace Fission Gases");
        addMaterialLang(provider, "purified_radioactive_waste_fluid", "Purified Radioactive Waste Fluid");
    }

    private static void addMaterialLang(RegistrateLangProvider provider, String id, String name) {
        provider.add("material.phoenix_fission." + id, name);
    }
}
