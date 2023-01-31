package net.merchantpug.bovinesandbuttercups.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.minecraft.core.Registry;

public class BovineRegistriesQuilt {
    public static final Registry<CowType<?>> COW_TYPE;

    public static void init() {

    }

    static {
        COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<CowType<?>>castClass(CowType.class), BovineRegistryKeys.COW_TYPE_KEY.location()).buildAndRegister();
    }
}
