package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.api.CowType;
import com.github.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BovineRegistriesFabriQuilt {
    public static final Registry<CowType<?>> COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<CowType<?>>castClass(CowType.class), Constants.resourceLocation("cow_type")).buildAndRegister();

    public static final Registry<ConfiguredCowType<?, ?>> CONFIGURED_COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<ConfiguredCowType<?, ?>>castClass(ConfiguredCowType.class), BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY.location()).buildAndRegister();
}
