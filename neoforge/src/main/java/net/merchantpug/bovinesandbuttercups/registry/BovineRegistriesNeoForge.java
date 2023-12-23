package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static net.merchantpug.bovinesandbuttercups.registry.condition.BovineBlockConditionTypes.BLOCK_CONDITION_TYPE;

public class BovineRegistriesNeoForge {
    public static final Registry<CowType<?>> COW_TYPE = new RegistryBuilder(BovineRegistryKeys.COW_TYPE_KEY).create();

    public static final Registry<EntityConditionType<?>> ENTITY_CONDITION_TYPE = new RegistryBuilder(BovineRegistryKeys.ENTITY_CONDITION_TYPE_KEY).create();

    public static final Registry<BlockConditionType<?>> BLOCK_CONDITION_TYPE = new RegistryBuilder(BovineRegistryKeys.BLOCK_CONDITION_TYPE_KEY).create();

    public static final Registry<BiomeConditionType<?>> BIOME_CONDITION_TYPE = new RegistryBuilder(BovineRegistryKeys.BIOME_CONDITION_TYPE_KEY).create();
}