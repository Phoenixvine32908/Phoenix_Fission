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

import java.util.List;
import java.util.function.Supplier;

@Accessors(chain = true, fluent = true)
public class FissionBlanketRodBlockBuilder extends BlockBuilder {

    @Setter
    public transient int tier = 1;
    @Setter
    public transient int durationTicks = 1200;
    @Setter
    public transient int amountPerCycle = 1;

    @NotNull
    public transient Supplier<Material> material = () -> GTMaterials.NULL;

    /** Base texture */
    @Setter
    public transient String texture = "phoenix_fission:block/fission/blanket/missing";

    /** Overlay tint mask texture */
    @Setter
    public transient String maskTexture = "phoenix_fission:block/fission/masks/blanket_mask";

    @Setter
    public transient String inputKey = "gtceu:uranium_238_nugget";
    @Setter
    public transient String outputKey = "gtceu:plutonium_nugget";

    /** Tint color ARGB. -1 = auto */
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

        private final ResourceLocation baseTextureLocation = ResourceLocation.tryParse(texture) != null ?
                new ResourceLocation(texture) :
                new ResourceLocation("phoenix_fission", "block/fission/blanket/missing");

        private final ResourceLocation maskTextureLocation = ResourceLocation.tryParse(maskTexture) != null ?
                new ResourceLocation(maskTexture) :
                new ResourceLocation("phoenix_fission", "block/fission/masks/blanket_mask");

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
            return Math.max(1, durationTicks);
        }

        @Override
        public int getAmountPerCycle() {
            return Math.max(0, amountPerCycle);
        }

        @Override
        public @NotNull String getInputKey() {
            return inputKey == null ? "" : inputKey;
        }

        @Override
        public List<BlanketOutput> getOutputs() {
            return List.of();
        }

        @Override
        public int getTier() {
            return Math.max(0, tier);
        }

        @Override
        public @NotNull ResourceLocation getTexture() {
            return baseTextureLocation;
        }

        // Expose for your color handler + model gen via reflection/instance checks
        public int getTintColor() {
            if (tintColor != -1) return tintColor;

            // Tier-based fallback (stable)
            return switch (getTier()) {
                case 1 -> 0xFFB07CFF;
                case 2 -> 0xFFFFD27D;
                case 3 -> 0xFF7DE7FF;
                case 4 -> 0xFFFF7DAA;
                default -> 0xFFFFFFFF;
            };
        }

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
