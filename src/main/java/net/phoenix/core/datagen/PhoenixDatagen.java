package net.phoenix.core.datagen;

import net.phoenix.core.datagen.lang.PhoenixLangHandler;

import com.tterrag.registrate.providers.ProviderType;

import static net.phoenix.core.common.registry.PhoenixFissionRegistration.REGISTRATE;

public class PhoenixDatagen {

    public static void init() {
        REGISTRATE.addDataGenerator(ProviderType.LANG, PhoenixLangHandler::init);
    }
}
