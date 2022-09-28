package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BovineTags {
    public static final TagKey<Block> SNOWDROP_PLACEABLE = TagKey.create(Registry.BLOCK_REGISTRY, BovinesAndButtercups.asResource("snowdrop_placeable"));
}
