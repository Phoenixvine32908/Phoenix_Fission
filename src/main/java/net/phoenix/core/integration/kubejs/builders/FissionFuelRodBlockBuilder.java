package net.phoenix.core.integration.kubejs.builders;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.phoenix.core.PhoenixAPI;
import net.phoenix.core.api.block.IFissionFuelRodType;
import net.phoenix.core.common.block.FissionFuelRodBlock;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Accessors(chain = true, fluent = true)
public class FissionFuelRodBlockBuilder extends BlockBuilder {

    @Setter
    public transient int baseHeatProduction = 50;
    @Setter
    public transient int tier = 1;
    @Setter
    public transient int durationTicks = 20;
    @Setter
    public transient int amountPerCycle = 1;

    @Setter
    @NotNull
    public transient String fuelKey = "gtceu:uranium";

    @NotNull
    public transient Supplier<Material> material = () -> GTMaterials.NULL;

    /** Base texture (bot_all) */
    @Setter
    public transient String texture = "phoenix_fission:block/fission/fuel_rod/missing";

    /** Overlay mask (top_all) used for tinting */
    @Setter
    public transient String maskTexture = "phoenix_fission:block/fission/masks/fuel_rod_mask";

    /** Optional active overlay mask (top_all) when ACTIVE=true (emissive/glow if you wire it) */
    @Setter
    public transient String activeMaskTexture = "phoenix_fission:block/fission/masks/fuel_rod_mask_active";

    /** Tint color ARGB. If -1, fall back to tier tint */
    @Setter
    public transient int tintColor = -1;

    public FissionFuelRodBlockBuilder(ResourceLocation i) {
        super(i);
        noValidSpawns(true);
        renderType("cutout_mipped");
    }

    public FissionFuelRodBlockBuilder rodMaterial(@NotNull Supplier<Material> material) {
        this.material = material;
        return this;
    }

    private class KjsFuelRodType implements IFissionFuelRodType, StringRepresentable {

        private final ResourceLocation baseTextureLocation = new ResourceLocation(texture);
        private final ResourceLocation maskTextureLocation = new ResourceLocation(maskTexture);
        private final ResourceLocation activeMaskTextureLocation = new ResourceLocation(activeMaskTexture);

        @Override
        public @NotNull String getSerializedName() {
            return id.getPath();
        }

        @Override
        public @NotNull String getName() {
            return id.getPath();
        }

        @Override
        public int getBaseHeatProduction() {
            return baseHeatProduction;
        }

        @Override
        public @NotNull String getFuelKey() {
            return fuelKey;
        }

        @Override
        public int getDurationTicks() {
            return durationTicks;
        }

        @Override
        public int getAmountPerCycle() {
            return amountPerCycle;
        }

        @Override
        public int getTier() {
            return tier;
        }

        @Override
        public @NotNull Material getMaterial() {
            return material.get();
        }

        @Override
        public @NotNull ResourceLocation getTexture() {
            // Base texture for bot_all
            return baseTextureLocation;
        }

        /** OPTIONAL: if you add this to IFissionFuelRodType, tint becomes unified */
        public int getTintColor() {
            if (tintColor != -1) return tintColor;

            return switch (tier) {
                case 1 -> 0xFF7DE7FF; // cyan-ish
                case 2 -> 0xFFB07CFF; // purple-ish
                case 3 -> 0xFFFFD27D; // gold-ish
                case 4 -> 0xFFFF7DAA; // pink-ish
                default -> 0xFFFFFFFF;
            };
        }

        /** NEW: overlay mask for tinted model (inactive) */
        public @NotNull ResourceLocation getMaskTexture() {
            return maskTextureLocation;
        }

        /** NEW: overlay mask for tinted model (active) */
        public @NotNull ResourceLocation getActiveMaskTexture() {
            return activeMaskTextureLocation;
        }
    }

    @Override
    public Block createObject() {
        IFissionFuelRodType type = new KjsFuelRodType();
        FissionFuelRodBlock result = new FissionFuelRodBlock(this.createProperties(), type);
        PhoenixAPI.FISSION_FUEL_RODS.put(type, () -> result);
        return result;
    }
}
