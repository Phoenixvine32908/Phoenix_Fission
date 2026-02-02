package net.phoenix.core.integration.kubejs.builders;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.phoenix.core.PhoenixAPI;
import net.phoenix.core.api.block.IFissionCoolerType;
import net.phoenix.core.common.block.FissionCoolerBlock;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Accessors(chain = true, fluent = true)
public class FissionCoolerBlockBuilder extends BlockBuilder {

    @Setter
    public transient int coolerTemperature = 1000;
    @Setter
    public transient int tier = 1;
    @Setter
    public transient int coolantUsagePerTick = 10;

    /** INPUT coolant fluid id */
    @Setter
    @NotNull
    public transient String requiredCoolantMaterialId = "gtceu:distilled_water";

    /** OUTPUT coolant fluid id (hot return). Defaults to same as input (no conversion). */
    @Setter
    @NotNull
    public transient String outputCoolantFluidId = ""; // empty => same as input

    /** Legacy hook only. Not used for IO/tint anymore. */
    @NotNull
    public transient Supplier<Material> material = () -> GTMaterials.NULL;

    /** Base texture (bot_all) */
    @Setter
    public transient String texture = "phoenix_fission:block/fission/cooler/missing";

    /** Overlay mask (top_all) used for tinting */
    @Setter
    public transient String maskTexture = "phoenix_fission:block/fission/masks/cooler_mask";

    /** Tint color ARGB. If -1, fall back to tier tint */
    @Setter
    public transient int tintColor = -1;

    public FissionCoolerBlockBuilder(ResourceLocation i) {
        super(i);
        noValidSpawns(true);
        renderType("cutout_mipped");
    }

    public FissionCoolerBlockBuilder coolerMaterial(@NotNull Supplier<Material> material) {
        this.material = material;
        return this;
    }

    private class KjsCoolerType implements IFissionCoolerType, StringRepresentable {

        private final ResourceLocation baseTextureLocation = safeRL(texture,
                new ResourceLocation("phoenix_fission", "block/fission/cooler/missing"));
        private final ResourceLocation maskTextureLocation = safeRL(maskTexture,
                new ResourceLocation("phoenix_fission", "block/fission/masks/cooler_mask"));

        @Override
        public @NotNull String getSerializedName() {
            return id.getPath();
        }

        @Override
        public @NotNull String getName() {
            return id.getPath();
        }

        @Override
        public int getCoolerTemperature() {
            return Math.max(0, coolerTemperature);
        }

        @Override
        public int getTier() {
            return Math.max(0, tier);
        }

        @Override
        public int getCoolantUsagePerTick() {
            return Math.max(0, coolantUsagePerTick);
        }

        /**
         * Legacy interface name: now treated as INPUT coolant fluid id.
         */
        @Override
        public @NotNull String getRequiredCoolantMaterialId() {
            return requiredCoolantMaterialId == null ? "" : requiredCoolantMaterialId;
        }

        @Override
        public @NotNull String getOutputCoolantFluidId() {
            String in = getRequiredCoolantMaterialId();
            String out = outputCoolantFluidId == null ? "" : outputCoolantFluidId;
            return out.isEmpty() ? in : out;
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

        @Override
        public int getTintColor() {
            if (tintColor != -1) return tintColor;

            return switch (getTier()) {
                case 1 -> 0xFF7DE7FF; // cyan-ish
                case 2 -> 0xFFB07CFF; // purple-ish
                case 3 -> 0xFFFFD27D; // gold-ish
                case 4 -> 0xFFFF7DAA; // pink-ish
                default -> 0xFFFFFFFF;
            };
        }

        /** Not in IFissionCoolerType. Only useful if your model/color code explicitly reads it. */
        public @NotNull ResourceLocation getMaskTexture() {
            return maskTextureLocation;
        }
    }

    @Override
    public Block createObject() {
        IFissionCoolerType type = new KjsCoolerType();
        FissionCoolerBlock result = new FissionCoolerBlock(this.createProperties(), type);
        PhoenixAPI.FISSION_COOLERS.put(type, () -> result);
        return result;
    }

    private static ResourceLocation safeRL(String s, ResourceLocation fallback) {
        if (s == null || s.isEmpty()) return fallback;
        ResourceLocation rl = ResourceLocation.tryParse(s);
        return rl != null ? rl : fallback;
    }
}
