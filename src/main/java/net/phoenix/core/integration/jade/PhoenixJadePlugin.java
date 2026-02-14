package net.phoenix.core.integration.jade;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

@WailaPlugin
public class PhoenixJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new FissionMachineProvider(), BlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new FissionMachineProvider(), Block.class);
    }
}

*/
