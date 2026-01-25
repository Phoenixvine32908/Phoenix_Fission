package net.phoenix.core.common.data.item;

import static net.phoenix.core.PhoenixFission.PHOENIX_CREATIVE_TAB;
import static net.phoenix.core.common.registry.PhoenixFissionRegistration.REGISTRATE;

public class PhoenixFissionItems {

    static {
        REGISTRATE.creativeModeTab(() -> PHOENIX_CREATIVE_TAB);
    }

    public static void init() {}
}
