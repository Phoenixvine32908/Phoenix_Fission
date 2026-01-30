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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import net.phoenix.core.PhoenixFission;
import net.phoenix.core.api.block.IFissionCoolerType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@Getter
@ParametersAreNonnullByDefault
public class FissionCoolerBlock extends ActiveBlock {

    /** Needed for tinting + introspection */
    private final IFissionCoolerType coolerType;

    public FissionCoolerBlock(Properties props, IFissionCoolerType type) {
        super(props);
        this.coolerType = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level,
                                List<Component> tooltip, TooltipFlag flag) {
        if (!GTUtil.isShiftDown()) {
            tooltip.add(Component.translatable("block.phoenix_fission.fission_cooler.shift")
                    .withStyle(ChatFormatting.GRAY));
            return;
        }

        // PURE registry id coolant display
        Component coolantName = getFluidDisplayName(coolerType.getRequiredCoolantMaterialId());
        tooltip.add(Component.translatable("phoenix.fission.coolant_required", coolantName));

        tooltip.add(Component.literal("§7Coolant Usage: §f" +
                coolerType.getCoolantUsagePerTick() + " mB/t"));

        tooltip.add(Component.translatable("phoenix.fission.cooling_power",
                coolerType.getCoolerTemperature()));
    }

    /** Registry-only fluid display name (no Material/GTMaterials). */
    public static Component getFluidDisplayName(@NotNull String fluidId) {
        if (fluidId.isEmpty() || "none".equalsIgnoreCase(fluidId)) {
            return Component.literal("None").withStyle(ChatFormatting.GRAY);
        }

        ResourceLocation rl = ResourceLocation.tryParse(fluidId);
        if (rl == null) return Component.literal(fluidId).withStyle(ChatFormatting.YELLOW);

        Fluid f = ForgeRegistries.FLUIDS.getValue(rl);
        if (f != null && f != Fluids.EMPTY) {
            return Component.translatable(f.getFluidType().getDescriptionId());
        }

        return Component.literal(fluidId).withStyle(ChatFormatting.YELLOW);
    }

    public enum FissionCoolerTypes implements StringRepresentable, IFissionCoolerType {

        // If you want packdevs to choose EVERY color, set explicit tintColor values and
        // don't generate them from tier.
        COOLER_BASIC(
                "basic_cooler",
                105, 1, 100,
                "gtceu:distilled_water",
                PhoenixFission.id("block/fission/basic_cooler_block"),
                0xFF7DE7FF);

        @Getter @NotNull private final String name;
        @Getter private final int coolerTemperature;
        @Getter private final int tier;
        @Getter private final int coolantUsagePerTick;

        @Getter @NotNull private final String requiredCoolantMaterialId;
        @Getter @NotNull private final ResourceLocation texture;

        /** Per-type tint (ARGB) */
        @Getter private final int tintColor;

        FissionCoolerTypes(String name, int temp, int tier, int usage,
                           String coolantFluidId, ResourceLocation texture, int tintColor) {
            this.name = name;
            this.coolerTemperature = temp;
            this.tier = tier;
            this.coolantUsagePerTick = usage;
            this.requiredCoolantMaterialId = coolantFluidId;
            this.texture = texture;
            this.tintColor = tintColor;
        }

        @Override
        public int getCoolantUsagePerTick() {
            return this.coolantUsagePerTick;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }

        @Override
        public @NotNull String getRequiredCoolantMaterialId() {
            return this.requiredCoolantMaterialId;
        }

        @Override
        public int getTintColor() {
            return tintColor;
        }

        @Override
        public com.gregtechceu.gtceu.api.data.chemical.material.Material getMaterial() {
            // keep interface satisfied, but do not use for tint or IO
            return com.gregtechceu.gtceu.common.data.GTMaterials.NULL;
        }
    }
}
