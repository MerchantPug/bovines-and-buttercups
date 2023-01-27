package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class BovineTags {
    public static final TagKey<Block> SNOWDROP_PLACEABLE = TagKey.create(Registries.BLOCK, BovinesAndButtercups.asResource("snowdrop_placeable"));

    public static final TagKey<Biome> PREVENT_COW_SPAWNS = TagKey.create(Registries.BIOME, BovinesAndButtercups.asResource("prevent_cow_spawns"));
}