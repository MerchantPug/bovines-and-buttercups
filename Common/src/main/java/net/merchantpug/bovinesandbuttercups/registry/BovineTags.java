package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class BovineTags {
    public static final TagKey<Block> SNOWDROP_PLACEABLE = TagKey.create(Registry.BLOCK_REGISTRY, BovinesAndButtercups.asResource("snowdrop_placeable"));
    public static final TagKey<Biome> HIGHER_CHANCE_MOOBLOOMS = TagKey.create(Registry.BIOME_REGISTRY, BovinesAndButtercups.asResource("lower_chance_mooblooms"));
}