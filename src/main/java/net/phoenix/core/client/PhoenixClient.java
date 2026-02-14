package net.phoenix.core.client;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.phoenix.core.PhoenixFission;

@Mod.EventBusSubscriber(modid = PhoenixFission.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PhoenixClient {

    private PhoenixClient() {}

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(PhoenixFissionEntities.NUKE_PRIMED.get(), NukePrimedRenderer::new);
        });
    }

*
/
