package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class BovineItemsFabric {
    public static final CustomFlowerItem CUSTOM_FLOWER = register(Constants.resourceLocation("custom_flower"), new CustomFlowerItem(BovineBlocks.CUSTOM_FLOWER.get(), new Item.Properties()));

    public static void init() {

    }

    public static <T extends Item> T register(ResourceLocation key, T item) {
        return Registry.register(Registry.ITEM, key, item);
    }
}
