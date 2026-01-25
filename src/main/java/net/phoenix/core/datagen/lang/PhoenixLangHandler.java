package net.phoenix.core.datagen.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class PhoenixLangHandler {

    public static void init(RegistrateLangProvider provider) {
        provider.add("emi_info.phoenix_fission.required_shield", "Required Shield: %s");
        provider.add("shield.phoenix_fission.type.normal", "Normal");
        provider.add("shield.phoenix_fission.type.inactive", "Inactive");
        provider.add("shield.phoenix_fission.type.decayed", "Decayed");
        provider.add("emi_info.phoenix_fission.shield_heal", "Shield Health Restored: +%s");
        provider.add("emi_info.phoenix_fission.shield_damage", "Shield Damage Applied: -%s");
        provider.add("phoenix.fission.coolant_required", "§7Required Coolant: §f%s");
        provider.add("phoenix.fission.not_formed", "Structure not formed!");
        provider.add("phoenix.fission.moderator", "Moderator: %s");
        provider.add("phoenix.fission.moderator_boost", "EU Boost: %s%%");
        provider.add("phoenix.fission.moderator_fuel_discount", "Fuel Discount: %s%%");
        provider.add("phoenix.fission.cooler", "Cooler: %s");
        provider.add("phoenix.fission.coolant", "Coolant: %s");
        provider.add("phoenix.fission.cooling_power", "§7Cooling Power: %s");
        provider.add("phoenix.fission.coolant_rate", "Coolant Rate: %s mB/t");
        provider.add("phoenix.fission.meltdown_in", "MELTDOWN in: %s seconds");
        provider.add("phoenix.fission.safe", "Status: SAFE");
        provider.add("phoenix.fission.summary", "Cooling Provided: %s / %s");
        provider.add("emi_info.phoenix_fission.required_cooling", "Required Cooling: %s");

        provider.add("jade.phoenix_fission.fission_safe", "Status: SAFE");
        provider.add("jade.phoenix_fission.fission_meltdown_timer", "Meltdown in %s seconds");
        provider.add("jade.phoenix_fission.fission_no_coolant", "Coolant Tanks EMPTY");
        provider.add("jade.phoenix_fission.fission_low_cooling", "Insufficient Cooling Power");
        provider.add("config.jade.plugin_phoenix_fission.fission_machine_info", "Fission Machine Info");

        provider.add("phoenix.fission.status.safe_idle", "SAFE (Idle)");
        provider.add("phoenix.fission.status.safe_working", "SAFE (Working)");
        provider.add("phoenix.fission.status.danger_timer", "DANGER: Meltdown in %s seconds!");
        provider.add("phoenix.fission.status.no_coolant", "Coolant Tanks EMPTY");
        provider.add("phoenix.fission.status.low_cooling", "Insufficient Cooling Power");
        provider.add("phoenix.fission.coolant_status.ok", "Coolant Status: OK");
        provider.add("phoenix.fission.coolant_status.empty", "Coolant Status: EMPTY");
        provider.add("tooltip.phoenix_fission.crystal_rose.generic", "A crystalline flower of immense power.");
        provider.add("tooltip.phoenix_fission.crystal_rose.made_from", "Forged from %s.");
        provider.add("tooltip.phoenix_fission.nanites.generic", "Microscopic machines swarming with potential.");
        provider.add("tooltip.phoenix_fission.nanites.made_from", "Constructed from %s.");

        provider.add("block.phoenix_fission.fission_cooler.shift", "Hold §eShift§r for cooler details");
        provider.add("block.phoenix_fission.fission_cooler.info_header", "§7--- Cooler Information ---");
        provider.add("block.phoenix_fission.fission_cooler.temperature", "§cOperating Temperature: §f%s K");
        provider.add("block.phoenix_fission.fission_cooler.required_coolant", "§bRequired Coolant: §f%s");

        provider.add("block.phoenix_fission.fission_moderator.shift", "Hold §eShift§r for moderator details");
        provider.add("block.phoenix_fission.fission_moderator.info_header", "§7--- Moderator Information ---");
        provider.add("block.phoenix_fission.fission_moderator.boost", "§aEU Output Boost: §f%s%%");
        provider.add("block.phoenix_fission.fission_moderator.fuel_discount", "§bFuel Usage Reduction: §f%s%%");

        provider.add("phoenix.multiblock.pattern.info.multiple_coolers",
                "Accepts multiple Fission Cooler Blocks. Cooling power is additive.");
        provider.add("phoenix.multiblock.pattern.info.multiple_moderators",
                "Accepts multiple Fission Moderator Blocks. EU Boost/Fuel Discount is additive.");
        provider.add("phoenix_fission.tooltip.requires_fluid", "Needs: %s");
        provider.add("material.phoenix_fission.boron_carbide", "§5Boron Carbide");
        provider.add("material.phoenix_fission.niobium_modified_silicon_carbide", "§5Niobium Modified Silicon Carbide");
        provider.add("material.phoenix_fission.frost", "§bFrost");
        provider.add("material.phoenix_fission.wax_melting_catalyst", "Wax Melting Catalyst");
        provider.add("material.phoenix_fission.sugar_water", "Sugar Water");
        provider.add("material.phoenix_fission.eighty_five_percent_pure_nevvonian_steel",
                "§6Eighty Five Percent Pure Nevvonian Steel");
        provider.add("material.phoenix_fission.phoenix_enriched_naquadah",
                "§6Phoenix Enriched Naquadah");
        provider.add("material.phoenix_fission.ignisium", "§4Ignisium");
        provider.add("material.phoenix_fission.crystallized_fluxstone", "§dCrystallized Fluxstone");
        provider.add("material.phoenix_fission.nevvonian_iron", "§7Nevvonian Iron");
        provider.add("material.phoenix_fission.fluorite", "§aFluorite");
        provider.add("material.phoenix_fission.polarity_flipped_bismuthite", "§bPolarity Flipped Bismuthite");
        provider.add("material.phoenix_fission.voidglass_shard", "§5Voidglass Shard");
        provider.add("phoenix_fission.tooltip.hyper_machine_1", "Each Coolant provides a boost:");
        provider.add("gtceu.recipe_type.phoenix_fission.high_performance_breeder_reactor",
                "High-Performance Breeder Reactor");
        provider.add("gtceu.recipe_type.phoenix_fission.advanced_pressurized_fission_reactor",
                "Advanced Pressurized Fission Reactor");
        provider.add("gtceu.recipe_type.phoenix_fission.pressurized_fission_reactor", "Pressurized Fission Reactor");
        provider.add("gtceu.recipe_type.phoenix_fission.heat_exchanging", "Heat Exchanger");
        provider.add("gtceu.recipe_type.phoenix_fission.honey_chamber", "Honey Chamber");
        provider.add("gtceu.recipe_type.phoenix_fission.please", "Please Multiblock");
        provider.add("gtceu.recipe_type.phoenix_fission.simulated_colony", "Simulated Colony");
        provider.add("gtceu.recipe_type.phoenix_fission.comb_decanting", "Comb Decanter");
        provider.add("gtceu.recipe_type.phoenix_fission.swarm_nurturing", "Swarm Nurturing Chamber");
        provider.add("gtceu.recipe_type.phoenix_fission.apis_progenitor", "Apis Progenitor");
        provider.add("block.monilabs.tesla_battery.tooltip_empty", "§7A hollow casing. Provides no storage.");
        provider.add("block.monilabs.tesla_battery.tooltip_filled", "§aCapacity: §f%s EU");
        provider.add("tooltip.phoenix_fission.tesla_hatch.input",
                "§bWireless Transmitter§r: Siphons energy into the Tesla Cloud.");
        provider.add("tooltip.phoenix_fission.tesla_hatch.output",
                "§bWireless Receiver§r: Broadcasts energy from the Tesla Cloud.");

        provider.add("block.phoenix_fission.tesla_battery.tooltip_empty", "§7A hollow casing. Provides no storage.");
        provider.add("block.phoenix_fission.tesla_battery.tooltip_filled", "§aCapacity: §f%s EU");

        provider.add("tooltip.phoenix_fission.tesla_hatch.lore", "§6Nevvonian Core Tech: Frequency Locked.");

        // Tower UI Component
        provider.add("gtceu.multiblock.tesla.stored", "Network Power: %s / %s EU");
        provider.add("shield.phoenix_fission.current_shield", "Shield Status: %s");
        provider.add("shield.phoenix_fission.health", "Health: %s");
        provider.add("shield.phoenix_fission.cooldown", "Cooldown: %s seconds");
        provider.add("jade.phoenix_fission.shield_state", "Shield State: %s");
        provider.add("jade.phoenix_fission.shield_health", "Health: %s");
        provider.add("config.jade.plugin_phoenix_fission.plasma_furnace_info", "High-Pressure Plasma Arc Furnace Info");
        provider.add("jade.phoenix_fission.plasma_boost_active", "Plasma Boost: %s Active");
        provider.add("jade.phoenix_fission.plasma_boost_duration", "Duration Multiplier: %s");
        provider.add("jade.phoenix_fission.no_plasma_boost", "No Plasma Catalyst");
        // Tesla Network Jade Keys
        provider.add("jade.phoenix_fission.tesla_stored", "Stored: ");
        provider.add("config.jade.plugin_phoenix_fission.tesla_network_info", "Tesla Network Information");

        // Binder UI & Additional Keys
        provider.add("item.phoenix_fission.tesla_binder.linked", "§aLinked to: §f%s");
        provider.add("item.phoenix_fission.tesla_binder.unlinked", "§cNot Linked");
        provider.add("item.phoenix_fission.tesla_binder.frequency", "§7Frequency: §b%s");
        provider.add("jade.phoenix_fission.tesla_team", "Network: %s");
        provider.add("jade.phoenix_fission.tesla_receiving", "Receiving: %s EU/t");
        provider.add("jade.phoenix_fission.tesla_providing", "Providing: %s EU/t");
        provider.add("jade.phoenix_fission.tesla_active_connections", "Active Connections: %s");
        multiLang(provider, "tooltip.phoenix_fission.shield_stability_hatch",
                "Outputs shield stability",
                "as a redstone signal.");
        multiLang(provider, "gtceu.placeholder_info.shieldStability",
                "Returns the stability of the shield.",
                "Note that not having a shield projected may result in nonsense values of integrity.",
                "Usage:",
                "  {shieldStability} -> shield integrity: (integrity, in percent)");
    }

    protected static void multiLang(RegistrateLangProvider provider, String key, String... values) {
        for (var i = 0; i < values.length; i++) {
            provider.add(getSubKey(key, i), values[i]);
        }
    }

    protected static String getSubKey(String key, int index) {
        return key + "." + index;
    }
}
