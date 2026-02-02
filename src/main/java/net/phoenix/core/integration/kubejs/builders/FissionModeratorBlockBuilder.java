package net.phoenix.core.integration.kubejs.builders;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.phoenix.core.PhoenixAPI;
import net.phoenix.core.api.block.IFissionModeratorType;
import net.phoenix.core.common.block.FissionModeratorBlock;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Accessors(chain = true, fluent = true)
public class FissionModeratorBlockBuilder extends BlockBuilder {

    @Setter
    public transient int euBoost = 1;
    @Setter
    public transient int fuelDiscount = 1;
    @Setter
    public transient int tier = 1;

    /** Legacy hook only. Don’t rely on it for tint/IO. */
    @NotNull
    public transient Supplier<Material> material = () -> GTMaterials.NULL;

    /** Base texture (bot_all) */
    @Setter
    public transient String texture = "phoenix_fission:block/fission/moderator/missing";

    /** Overlay mask (top_all) used for tinting */
    @Setter
    public transient String maskTexture = "phoenix_fission:block/fission/masks/moderator_mask";

    /** Tint color ARGB. If -1, fall back to tier tint */
    @Setter
    public transient int tintColor = -1;

    public FissionModeratorBlockBuilder(ResourceLocation i) {
        super(i);
        noValidSpawns(true);
        renderType("cutout_mipped");
    }

    public FissionModeratorBlockBuilder moderatorMaterial(@NotNull Supplier<Material> material) {
        this.material = material;
        return this;
    }

    private class KjsModeratorType implements IFissionModeratorType, StringRepresentable {

        private final ResourceLocation baseTextureLocation = safeRL(
                texture,
                new ResourceLocation("phoenix_fission", "block/fission/moderator/missing"));
        private final ResourceLocation maskTextureLocation = safeRL(
                maskTexture,
                new ResourceLocation("phoenix_fission", "block/fission/masks/moderator_mask"));

        @Override
        public @NotNull String getSerializedName() {
            return id.getPath();
        }

        @Override
        public @NotNull String getName() {
            return id.getPath();
        }

        @Override
        public int getEUBoost() {
            // negative boost makes no sense
            return Math.max(0, euBoost);
        }

        @Override
        public int getFuelDiscount() {
            // clamp to [0, 100] by default; your machine code also clamps with config max
            return clamp(fuelDiscount, 0, 100);
        }

        @Override
        public int getTier() {
            return Math.max(0, tier);
        }

        @Override
        public @NotNull Material getMaterial() {
            // keep interface satisfied; avoid depending on material-based tinting
            return GTMaterials.NULL;
        }

        @Override
        public @NotNull ResourceLocation getTexture() {
            // Base texture for bot_all
            return baseTextureLocation;
        }

        /**
         * If IFissionModeratorType has getTintColor(), add @Override.
         * Otherwise your color handler must explicitly read it.
         */
        public int getTintColor() {
            if (tintColor != -1) return tintColor;

            return switch (getTier()) {
                case 1 -> 0xFF7DE7FF;
                case 2 -> 0xFFB07CFF;
                case 3 -> 0xFFFFD27D;
                case 4 -> 0xFFFF7DAA;
                default -> 0xFFFFFFFF;
            };
        }

        /** Not in IFissionModeratorType unless you add it. */
        public @NotNull ResourceLocation getMaskTexture() {
            return maskTextureLocation;
        }
    }

    @Override
    public Block createObject() {
        IFissionModeratorType type = new KjsModeratorType();
        FissionModeratorBlock result = new FissionModeratorBlock(this.createProperties(), type);
        PhoenixAPI.FISSION_MODERATORS.put(type, () -> result);
        return result;
    }

    private static ResourceLocation safeRL(String s, ResourceLocation fallback) {
        if (s == null || s.isEmpty()) return fallback;
        ResourceLocation rl = ResourceLocation.tryParse(s);
        return rl != null ? rl : fallback;
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
