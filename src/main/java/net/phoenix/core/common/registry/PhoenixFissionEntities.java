package net.phoenix.core.common.registry;

import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.MobCategory;
import net.phoenix.core.PhoenixFission;
import net.phoenix.core.common.block.entity.NukePrimedEntity;

import static net.phoenix.core.common.registry.PhoenixFissionRegistration.REGISTRATE;

public class PhoenixFissionEntities {
    public static void init() {}

    public static final EntityEntry<NukePrimedEntity> NUKE_PRIMED = REGISTRATE
            .entity("nuke_primed", NukePrimedEntity::new, MobCategory.MISC)
            .properties(b -> b.sized(0.98f, 0.98f).clientTrackingRange(10).updateInterval(10))
            .register();
}
