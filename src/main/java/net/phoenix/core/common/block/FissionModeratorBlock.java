package net.phoenix.core.common.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.utils.GTUtil;

import lombok.Getter;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;

import net.phoenix.core.PhoenixFission;
import net.phoenix.core.api.block.IFissionModeratorType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Getter
@ParametersAreNonnullByDefault
public class FissionModeratorBlock extends ActiveBlock {

    /** Needed for tinting + introspection */
    private final IFissionModeratorType moderatorType;

    public FissionModeratorBlock(Properties properties, IFissionModeratorType moderatorType) {
        super(properties);
        this.moderatorType = moderatorType;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
                                List<Component> tooltip, TooltipFlag flag) {
        if (!GTUtil.isShiftDown()) {
            tooltip.add(Component.translatable("block.phoenix_fission.fission_moderator.shift")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }

        tooltip.add(Component.translatable("block.phoenix_fission.fission_moderator.info_header"));

        tooltip.add(Component.translatable("block.phoenix_fission.fission_moderator.boost",
                moderatorType.getEUBoost()));

        tooltip.add(Component.translatable("block.phoenix_fission.fission_moderator.fuel_discount",
                moderatorType.getFuelDiscount()));
    }

    public enum FissionModeratorTypes implements StringRepresentable, IFissionModeratorType {

        MODERATOR_GRAPHITE(
                "graphite_moderator",
                1, 3, 1,
                PhoenixFission.id("block/fission/graphite_moderator"),
                0xFFB07CFF);

        @Getter @NotNull private final String name;
        @Getter private final int EUBoost;
        @Getter private final int fuelDiscount;
        @Getter private final int tier;
        @Getter @NotNull private final ResourceLocation texture;

        /** Per-type tint (ARGB) */
        @Getter private final int tintColor;

        FissionModeratorTypes(String name, int EUBoost, int fuelDiscount, int tier,
                              ResourceLocation texture, int tintColor) {
            this.name = name;
            this.EUBoost = EUBoost;
            this.fuelDiscount = fuelDiscount;
            this.tier = tier;
            this.texture = texture;
            this.tintColor = tintColor;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }

        @Override
        public int getTintColor() {
            return tintColor;
        }

        @Override
        public com.gregtechceu.gtceu.api.data.chemical.material.Material getMaterial() {
            return com.gregtechceu.gtceu.common.data.GTMaterials.NULL;
        }
    }
}
