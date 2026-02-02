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

    /**
     * Fuel item registry id (Forge item id).
     * Example: "gtceu:uranium_235_nugget"
     *
     * (Legacy name kept: getFuelKey()).
     */
    @Setter
    @NotNull
    public transient String fuelKey = "gtceu:uranium_235_nugget";

    /** Legacy hook only. Don’t rely on it for IO/tint anymore. */
    @NotNull
    public transient Supplier<Material> material = () -> GTMaterials.NULL;

    /** Base texture (bot_all) */
    @Setter
    public transient String texture = "phoenix_fission:block/fission/fuel_rod/missing";

    /** Overlay mask (top_all) used for tinting */
    @Setter
    public transient String maskTexture = "phoenix_fission:block/fission/masks/fuel_rod_mask";

    /** Optional active overlay mask (top_all) when ACTIVE=true */
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

        private final ResourceLocation baseTextureLocation = safeRL(texture,
                new ResourceLocation("phoenix_fission", "block/fission/fuel_rod/missing"));
        private final ResourceLocation maskTextureLocation = safeRL(maskTexture,
                new ResourceLocation("phoenix_fission", "block/fission/masks/fuel_rod_mask"));
        private final ResourceLocation activeMaskTextureLocation = safeRL(activeMaskTexture,
                new ResourceLocation("phoenix_fission", "block/fission/masks/fuel_rod_mask_active"));

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
            return Math.max(0, baseHeatProduction);
        }

        /**
         * Legacy method name – but now treated as an ITEM registry id.
         */
        @Override
        public @NotNull String getFuelKey() {
            return fuelKey == null ? "" : fuelKey;
        }

        @Override
        public @NotNull String getOutputKey() {
            return "";
        }

        @Override
        public int getDurationTicks() {
            return Math.max(1, durationTicks);
        }

        @Override
        public int getAmountPerCycle() {
            return Math.max(0, amountPerCycle);
        }

        @Override
        public int getTier() {
            return Math.max(0, tier);
        }

        @Override
        public @NotNull Material getMaterial() {
            // keep interface satisfied, but don’t rely on it for IO/tint
            return GTMaterials.NULL;
        }

        @Override
        public @NotNull ResourceLocation getTexture() {
            return baseTextureLocation;
        }

        /**
         * If IFissionFuelRodType has getTintColor(), add @Override.
         * If it doesn’t, your color handler must explicitly read it (instance check/reflection),
         * or you should add it to the interface like coolers.
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

        /** Not in IFissionFuelRodType unless you add it. */
        public @NotNull ResourceLocation getMaskTexture() {
            return maskTextureLocation;
        }

        /** Not in IFissionFuelRodType unless you add it. */
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

    private static ResourceLocation safeRL(String s, ResourceLocation fallback) {
        if (s == null || s.isEmpty()) return fallback;
        ResourceLocation rl = ResourceLocation.tryParse(s);
        return rl != null ? rl : fallback;
    }
}
