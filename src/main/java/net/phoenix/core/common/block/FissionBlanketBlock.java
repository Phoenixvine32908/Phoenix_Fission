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

import net.phoenix.core.api.block.IFissionBlanketType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Getter
@ParametersAreNonnullByDefault
public class FissionBlanketBlock extends ActiveBlock {

    /**
     * Needed for tinting (BlockColor/ItemColor) and general introspection.
     */
    private final IFissionBlanketType blanketType;

    public FissionBlanketBlock(Properties props, IFissionBlanketType type) {
        super(props);
        this.blanketType = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
                                List<Component> tooltip, TooltipFlag flag) {
        if (!GTUtil.isShiftDown()) {
            tooltip.add(Component.translatable("gtceu.tooltip.item_details_shift")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }

        // Pure registry display names
        Component inputName = FissionFuelRodBlock.getRegistryDisplayName(blanketType.getInputKey());
        Component outputName = FissionFuelRodBlock.getRegistryDisplayName(blanketType.getOutputKey());

        tooltip.add(Component.translatable("phoenix.fission.blanket_input", inputName)
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("phoenix.fission.blanket_output", outputName)
                .withStyle(ChatFormatting.GOLD));

        double seconds = blanketType.getDurationTicks() / 20.0;
        tooltip.add(Component.translatable(
                        "phoenix.fission.blanket_cycle",
                        Component.literal(String.valueOf(blanketType.getAmountPerCycle()))
                                .withStyle(ChatFormatting.WHITE),
                        Component.literal(String.format("%.2f", seconds))
                                .withStyle(ChatFormatting.GOLD))
                .withStyle(ChatFormatting.GRAY));
    }

    public enum BreederBlanketTypes implements StringRepresentable, IFissionBlanketType {

        PLUTONIUM_BREEDER("plutonium_breeder",
                2, 2400, 1,
                "gtceu:uranium_238_nugget",
                "gtceu:plutonium_nugget",
                0xFFB07CFF),

        THORIUM_BREEDER("thorium_breeder",
                1, 2400, 1,
                "gtceu:uranium_238_nugget",
                "gtceu:plutonium_nugget",
                0xFFFFD27D);

        @Getter @NotNull private final String name;
        @Getter private final int tier;
        @Getter private final int durationTicks;
        @Getter private final int amountPerCycle;
        @Getter @NotNull private final String inputKey;
        @Getter @NotNull private final String outputKey;
        @Getter @NotNull private final ResourceLocation texture;

        /** Case-by-case ARGB tint. Packdevs choose this. */
        @Getter private final int tintColor;

        BreederBlanketTypes(String name, int tier, int duration, int amount,
                            String in, String out, int tintColor) {
            this.name = name;
            this.tier = tier;
            this.durationTicks = duration;
            this.amountPerCycle = amount;
            this.inputKey = in;
            this.outputKey = out;
            this.texture = new ResourceLocation("phoenix_fission", "block/blanket/" + name);
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
            return null; // not used for IO anymore
        }
    }
}
