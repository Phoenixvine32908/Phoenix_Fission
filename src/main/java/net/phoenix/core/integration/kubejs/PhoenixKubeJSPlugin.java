package net.phoenix.core.integration.kubejs;

import com.gregtechceu.gtceu.api.registry.GTRegistries;

import net.phoenix.core.PhoenixFission;
import net.phoenix.core.common.block.PhoenixFissionBlocks;
import net.phoenix.core.common.data.PhoenixFissionRecipeTypes;
import net.phoenix.core.common.data.item.PhoenixFissionItems;
import net.phoenix.core.common.data.materials.PhoenixFissionElements;
import net.phoenix.core.common.data.materials.PhoenixFissionMaterials;
import net.phoenix.core.common.machine.PhoenixFissionMachines;
import net.phoenix.core.common.machine.multiblock.*;
import net.phoenix.core.configs.PhoenixConfigs;
import net.phoenix.core.integration.kubejs.builders.FissionBlanketRodBlockBuilder;
import net.phoenix.core.integration.kubejs.builders.FissionCoolerBlockBuilder;
import net.phoenix.core.integration.kubejs.builders.FissionFuelRodBlockBuilder;
import net.phoenix.core.integration.kubejs.builders.FissionModeratorBlockBuilder;
import net.phoenix.core.integration.kubejs.recipe.PhoenixRecipeSchema;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class PhoenixKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void initStartup() {
        super.initStartup();
    }

    @Override
    public void init() {
        super.init();
        RegistryInfo.BLOCK.addType(
                PhoenixFission.MOD_ID + ":fission_cooler",
                FissionCoolerBlockBuilder.class,
                FissionCoolerBlockBuilder::new);

        RegistryInfo.BLOCK.addType(
                PhoenixFission.MOD_ID + ":fission_moderator",
                FissionModeratorBlockBuilder.class,
                FissionModeratorBlockBuilder::new);
        RegistryInfo.BLOCK.addType(
                PhoenixFission.MOD_ID + ":fission_fuel_rod",
                FissionFuelRodBlockBuilder.class,
                FissionFuelRodBlockBuilder::new);
        RegistryInfo.BLOCK.addType(
                PhoenixFission.MOD_ID + ":fission_blanket_rod",
                FissionBlanketRodBlockBuilder.class,
                FissionBlanketRodBlockBuilder::new);
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        super.registerClasses(type, filter);
        filter.allow("net.phoenix.core");
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        for (var entry : GTRegistries.RECIPE_TYPES.entries()) {
            event.register(entry.getKey(), PhoenixRecipeSchema.SCHEMA);
        }
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        super.registerBindings(event);
        event.add("PhoenixFissionMaterials", PhoenixFissionMaterials.class);
        event.add("PhoenixConfigs", PhoenixConfigs.class);
        event.add("PhoenixFissionElements", PhoenixFissionElements.class);
        event.add("PhoenixFissionBlocks", PhoenixFissionBlocks.class);
        event.add("PhoenixFissionMachines", PhoenixFissionMachines.class);
        event.add("PhoenixResearchMachines", PhoenixFissionMachines.class);
        event.add("PhoenixFissionItems", PhoenixFissionItems.class);
        event.add("PhoenixFissionRecipeTypes", PhoenixFissionRecipeTypes.class);
        event.add("FissionWorkableElectricMultiblockMachine", FissionWorkableElectricMultiblockMachine.class);
        event.add("BreederWorkableElectricMultiblockMachine", BreederWorkableElectricMultiblockMachine.class);
        event.add("DynamicFissionReactorMachine", DynamicFissionReactorMachine.class);
        event.add("PhoenixFission", PhoenixFission.class);
    }
}
