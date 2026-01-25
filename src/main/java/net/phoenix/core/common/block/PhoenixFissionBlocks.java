package net.phoenix.core.common.block;

import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.phoenix.core.PhoenixAPI;
import net.phoenix.core.PhoenixFission;
import net.phoenix.core.api.block.IFissionCoolerType;
import net.phoenix.core.api.block.IFissionModeratorType;
import net.phoenix.core.datagen.models.PhoenixFissionMachineModels;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import org.jetbrains.annotations.NotNull;

import static net.phoenix.core.common.registry.PhoenixFissionRegistration.REGISTRATE;

@SuppressWarnings("unused")
public class PhoenixFissionBlocks {

    public static void init() {}

    private static @NotNull BlockEntry<Block> registerSimpleBlock(String name, String id, String texture,
                                                                  NonNullBiFunction<Block, Item.Properties, ? extends BlockItem> func) {
        return REGISTRATE
                .block(id, Block::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(),
                        prov.models().cubeAll(ctx.getName(), PhoenixFission.id("block/" + texture))))
                .lang(name)
                .item(func)
                .build()
                .register();
    }

    public static final BlockEntry<FissionCoolerBlock> COOLER_BASIC = createCoolerBlock(
            FissionCoolerBlock.fissionCoolerType.COOLER_BASIC);

    public static final BlockEntry<FissionModeratorBlock> MODERATOR_GRAPHITE = createModeratorBlock(
            FissionModeratorBlock.fissionModeratorType.MODERATOR_GRAPHITE);

    public static BlockEntry<Block> FISSILE_HEAT_SAFE_CASING = registerSimpleBlock(
            "§bFissile Heat Safe Casing", "fissile_heat_safe_casing",
            "fissile_heat_safe_casing", BlockItem::new);
    public static BlockEntry<Block> FISSILE_REACTION_SAFE_CASING = registerSimpleBlock(
            "§bFissile Reaction Safe Casing", "fissile_reaction_safe_casing",
            "fissile_reaction_safe_casing", BlockItem::new);
    public static BlockEntry<Block> FISSILE_SAFE_GEARBOX_CASING = registerSimpleBlock(
            "§bFissile Safe Gearbox", "fissile_safe_gearbox_casing",
            "fissile_safe_gearbox", BlockItem::new);

    private static BlockEntry<FissionModeratorBlock> createModeratorBlock(IFissionModeratorType type) {
        var moderator = REGISTRATE
                .block("%s".formatted(type.getName()),
                        p -> new FissionModeratorBlock(p, type))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate(PhoenixFissionMachineModels.createFissionModeratorModel(type))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();

        PhoenixAPI.FISSION_MODERATORS.put(type, moderator);
        return moderator;
    }

    private static BlockEntry<FissionCoolerBlock> createCoolerBlock(IFissionCoolerType type) {
        var cooler = REGISTRATE
                .block("%s".formatted(type.getName()),
                        p -> new FissionCoolerBlock(p, type))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .blockstate(PhoenixFissionMachineModels.createActiveCoolerModel(type))
                .tag(CustomTags.MINEABLE_WITH_CONFIG_VALID_PICKAXE_WRENCH)
                .item(BlockItem::new)
                .build()
                .register();

        PhoenixAPI.FISSION_COOLERS.put(type, cooler);
        return cooler;
    }
}
