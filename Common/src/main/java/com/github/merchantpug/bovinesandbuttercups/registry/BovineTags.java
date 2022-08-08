package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BovineTags {
    public static final TagKey<Block> SNOWDROP_PLACEABLE = TagKey.create(Registry.BLOCK_REGISTRY, Constants.resourceLocation("snowdrop_placeable"));
}