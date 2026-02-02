package net.phoenix.core.api.block;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.phoenix.core.PhoenixAPI;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public interface IFissionFuelRodType {

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

    int getBaseHeatProduction();

    @NotNull
    String getFuelKey();

    @NotNull
    String getOutputKey();

    int getDurationTicks();

    int getAmountPerCycle();

    Material getMaterial();

    int getTier();

    ResourceLocation getTexture();

    @Nullable
    default Material tryResolveFuelMaterial() {
        String key = getFuelKey();
        Material mat = GTMaterials.get(key);
        if (mat == null || mat == GTMaterials.NULL) return null;
        return mat;
    }

    Lazy<IFissionFuelRodType[]> ALL_FUEL_RODS_BY_HEAT = Lazy.of(() -> PhoenixAPI.FISSION_FUEL_RODS.keySet().stream()
            .sorted(Comparator.comparingInt(IFissionFuelRodType::getBaseHeatProduction))
            .toArray(IFissionFuelRodType[]::new));
}
