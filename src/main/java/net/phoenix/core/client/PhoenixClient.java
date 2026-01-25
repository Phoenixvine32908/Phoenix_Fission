package net.phoenix.core.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.phoenix.core.PhoenixFission;

@Mod.EventBusSubscriber(modid = PhoenixFission.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PhoenixClient {

    private PhoenixClient() {}
}
