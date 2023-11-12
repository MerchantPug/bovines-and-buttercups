package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BovineRegistryKeys {
    public static final ResourceKey<Registry<CowType<?>>> COW_TYPE_KEY = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("cow_type"));

    public static final ResourceKey<Registry<EntityConditionType<?>>> ENTITY_CONDITION_TYPE_KEY = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("entity_condition_type"));
    public static final ResourceKey<Registry<BlockConditionType<?>>> BLOCK_CONDITION_TYPE_KEY = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("block_condition_type"));
    public static final ResourceKey<Registry<BiomeConditionType<?>>> BIOME_CONDITION_TYPE_KEY = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("biome_condition_type"));
}
