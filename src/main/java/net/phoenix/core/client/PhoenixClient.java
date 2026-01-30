package net.phoenix.core.client;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.phoenix.core.PhoenixFission;
import net.phoenix.core.client.render.NukePrimedRenderer;
import net.phoenix.core.common.registry.PhoenixFissionEntities;

@Mod.EventBusSubscriber(modid = PhoenixFission.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PhoenixClient {

    private PhoenixClient() {}
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(PhoenixFissionEntities.NUKE_PRIMED.get(), NukePrimedRenderer::new);
        });
    }
}
