package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BovineTags {
    public static final TagKey<Item> SUSPICIOUS_STEW_RECIPE_EXCLUDED = TagKey.create(Registry.ITEM_REGISTRY, BovinesAndButtercupsCommon.resourceLocation("suspicious_stew_recipe_excluded"));
}
