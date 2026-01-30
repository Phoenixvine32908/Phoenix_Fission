package net.phoenix.core.integration.kubejs.builders;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.phoenix.core.PhoenixAPI;
import net.phoenix.core.api.block.IFissionBlanketType;
import net.phoenix.core.common.block.FissionBlanketBlock;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Accessors(chain = true, fluent = true)
public class FissionBlanketRodBlockBuilder extends BlockBuilder {

    @Setter
    public transient int tier = 1, durationTicks = 1200, amountPerCycle = 1;

    @NotNull
    public transient Supplier<Material> material = () -> GTMaterials.NULL;

    /** Base (bot_all) texture */
    @Setter
    public transient String texture = "phoenix_fission:block/fission/blanket/missing";

    /**
     * Overlay mask (top_all) texture used for tinting.
     * Should be a neutral (white/gray) mask PNG with transparency.
     */
    @Setter
    public transient String maskTexture = "phoenix_fission:block/fission/masks/blanket_mask";

    /** Required for breeder logic */
    @Setter
    public transient String inputKey = "gtceu:uranium_238_nugget";
    @Setter
    public transient String outputKey = "gtceu:plutonium_nugget";

    /**
     * Tint color (ARGB). If left at -1, it can be derived from material or tier.
     * Example: 0xFFRRGGBB
     */
    @Setter
    public transient int tintColor = -1;

    public FissionBlanketRodBlockBuilder(ResourceLocation i) {
        super(i);
        noValidSpawns(true);
        renderType("cutout_mipped");
    }

    public FissionBlanketRodBlockBuilder blanketMaterial(@NotNull Supplier<Material> material) {
        this.material = material;
        return this;
    }

    private class KjsBlanketType implements IFissionBlanketType, StringRepresentable {

        private final ResourceLocation baseTextureLocation = new ResourceLocation(texture);
        private final ResourceLocation maskTextureLocation = new ResourceLocation(maskTexture);

        @Override
        public @NotNull String getSerializedName() {
            return id.getPath();
        }

        @Override
        public @NotNull String getName() {
            return id.getPath();
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
        public @NotNull String getInputKey() {
            return inputKey;
        }

        @Override
        public @NotNull String getOutputKey() {
            return outputKey;
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
            // Keep this as the BASE texture for your datagen (bot_all)
            return baseTextureLocation;
        }

        /**
         * OPTIONAL: if you add this to IFissionBlanketType, your tint system can be unified.
         * If you don't want to edit the interface, you can still read tint via instance checks
         * in your color handler.
         */
        public int getTintColor() {
            if (tintColor != -1) return tintColor;

            // Best-effort: tint from tier (stable, no GTCEu API dependence)
            return switch (tier) {
                case 1 -> 0xFFB07CFF;
                case 2 -> 0xFFFFD27D;
                case 3 -> 0xFF7DE7FF;
                case 4 -> 0xFFFF7DAA;
                default -> 0xFFFFFFFF;
            };
        }

        /** NEW: expose mask texture for model generation (if you choose to use it) */
        public @NotNull ResourceLocation getMaskTexture() {
            return maskTextureLocation;
        }
    }

    @Override
    public Block createObject() {
        IFissionBlanketType type = new KjsBlanketType();
        FissionBlanketBlock result = new FissionBlanketBlock(this.createProperties(), type);
        PhoenixAPI.FISSION_BLANKETS.put(type, () -> result);
        return result;
    }
}
