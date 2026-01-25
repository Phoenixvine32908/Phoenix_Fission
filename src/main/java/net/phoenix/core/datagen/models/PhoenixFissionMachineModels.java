package net.phoenix.core.datagen.models;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.phoenix.core.PhoenixFission;
import net.phoenix.core.api.block.IFissionCoolerType;
import net.phoenix.core.api.block.IFissionModeratorType;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

public class PhoenixFissionMachineModels {

    public static void casingTextures(BlockModelBuilder model, String casingTexturePath) {
        ResourceLocation casing = PhoenixFission.id("block/" + casingTexturePath);
        model.texture("bottom", casing);
        model.texture("top", casing);
        model.texture("side", casing);
    }

    public static <
            T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> createActiveCoolerModel(IFissionCoolerType type) {
        return (ctx, prov) -> {
            String name = ctx.getName();
            Block block = ctx.getEntry();

            var inactive = prov.models().cubeAll(name, type.getTexture());

            var active = prov.models()
                    .withExistingParent(name + "_active", GTCEu.id("block/cube_2_layer/all"))
                    .texture("bot_all", type.getTexture())
                    .texture("top_all", type.getTexture().withSuffix("_active"));

            prov.getVariantBuilder(block)
                    .partialState().with(GTBlockStateProperties.ACTIVE, false)
                    .modelForState().modelFile(inactive).addModel()

                    .partialState().with(GTBlockStateProperties.ACTIVE, true)
                    .modelForState().modelFile(active).addModel();
        };
    }

    public static <
            T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> createFissionModeratorModel(IFissionModeratorType type) {
        return (ctx, prov) -> {
            String name = ctx.getName();
            Block block = ctx.getEntry();

            var model = prov.models().cubeAll(name, type.getTexture());

            prov.simpleBlock(block, model);
        };
    }
}
