package net.phoenix.core.api.block;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.phoenix.core.PhoenixAPI;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

import static net.phoenix.core.common.data.PhoenixMaterialRegistry.getMaterial;

public interface IFissionBlanketType {

    @NotNull
    String getName();

    default int getTintColor() {
        Material m = getMaterial();
        if (m != null && m != GTMaterials.NULL) {
            try {
                return 0xFF000000 | m.getMaterialRGB();
            } catch (Throwable ignored) {}
        }
        return 0xFFFFFFFF;
    }

    int getTier();

    int getDurationTicks();

    int getAmountPerCycle();

    /**
     * * The Forge Registry ID or GT Material for the input (e.g., "gtceu:u238_ingot")
     */
    @NotNull
    String getInputKey();

    /**
     * * The Forge Registry ID or GT Material for the result (e.g., "gtceu:plutonium_nugget")
     */
    @NotNull
    String getOutputKey();

    @NotNull
    ResourceLocation getTexture();

    @Nullable
    default Material tryResolveMaterial() {
        Material mat = getMaterial();
        if (mat == null || mat == GTMaterials.NULL) return null;
        return mat;
    }

    Material getMaterial();

    Lazy<IFissionBlanketType[]> ALL_BLANKETS_BY_TIER = Lazy.of(() -> PhoenixAPI.FISSION_BLANKETS.keySet().stream()
            .sorted(Comparator.comparingInt(IFissionBlanketType::getTier))
            .toArray(IFissionBlanketType[]::new));
}
