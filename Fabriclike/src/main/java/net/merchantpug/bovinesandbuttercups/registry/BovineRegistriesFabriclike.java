package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;

public class BovineRegistriesFabriclike {
    public static final Registry<CowType<?>> COW_TYPE;

    public static void init() {

    }

    static {
        COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<CowType<?>>castClass(CowType.class), BovineRegistryKeys.COW_TYPE_KEY.location()).buildAndRegister();
    }
}
