package net.phoenix.core.common.data;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;

import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import net.phoenix.core.api.gui.PhoenixFissionGuiTextures;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;

public class PhoenixFissionRecipeTypes {

    public static GTRecipeType HIGH_PERFORMANCE_BREEDER_REACTOR_RECIPES;
    public static GTRecipeType ADVANCED_PRESSURIZED_FISSION_REACTOR_RECIPES;
    public static GTRecipeType PRESSURIZED_FISSION_REACTOR_RECIPES;
    public static GTRecipeType HEAT_EXCHANGER_RECIPES;
    public static GTRecipeType SOURCE_IMBUEMENT_RECIPES;
    public static GTRecipeType SOURCE_EXTRACTION_RECIPES;

    public static void init() {
        HIGH_PERFORMANCE_BREEDER_REACTOR_RECIPES = register("high_performance_breeder_reactor", MULTIBLOCK)
                .setMaxIOSize(2, 2, 2, 2)
                .setSlotOverlay(false, false, GuiTextures.BOX_OVERLAY)
                .setProgressBar(PhoenixFissionGuiTextures.PROGRESS_BAR_FISSION,
                        ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.CHEMICAL)
                .setEUIO(IO.OUT)
                .addDataInfo(data -> {
                    int cooling = data.getInt("required_cooling");
                    if (cooling > 0) {
                        return LocalizationUtils.format("emi_info.phoenix_fission.required_cooling", cooling);
                    }
                    return "";
                });
        PRESSURIZED_FISSION_REACTOR_RECIPES = register("pressurized_fission_reactor", MULTIBLOCK)
                .setMaxIOSize(1, 1, 0, 0)
                .setSlotOverlay(false, false, GuiTextures.BOX_OVERLAY)
                .setProgressBar(PhoenixFissionGuiTextures.PROGRESS_BAR_FISSION,
                        ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.CHEMICAL)
                .setEUIO(IO.OUT)
                .addDataInfo(data -> {
                    int cooling = data.getInt("required_cooling");
                    if (cooling > 0) {
                        return LocalizationUtils.format("emi_info.phoenix_fission.required_cooling", cooling);
                    }
                    return "";
                });
        ADVANCED_PRESSURIZED_FISSION_REACTOR_RECIPES = register("advanced_pressurized_fission_reactor", MULTIBLOCK)
                .setMaxIOSize(1, 1, 1, 1)
                .setSlotOverlay(false, false, GuiTextures.BOX_OVERLAY)
                .setProgressBar(PhoenixFissionGuiTextures.PROGRESS_BAR_FISSION,
                        ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.CHEMICAL)
                .addDataInfo(data -> {
                    int cooling = data.getInt("required_cooling");
                    if (cooling > 0) {
                        return LocalizationUtils.format("emi_info.phoenix_fission.required_cooling", cooling);
                    }
                    return "";
                });
        HEAT_EXCHANGER_RECIPES = register("heat_exchanging", MULTIBLOCK)
                .setMaxIOSize(0, 0, 1, 1)
                .setSlotOverlay(false, true, GuiTextures.BOX_OVERLAY)
                .setProgressBar(GuiTextures.PROGRESS_BAR_EXTRACT, ProgressTexture.FillDirection.LEFT_TO_RIGHT)
                .setSound(GTSoundEntries.MIXER)
                .setEUIO(IO.IN);
    }
}
