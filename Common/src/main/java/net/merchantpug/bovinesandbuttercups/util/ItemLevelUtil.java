package net.merchantpug.bovinesandbuttercups.util;

import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.world.item.ItemStack;

public class ItemLevelUtil {
    public static boolean isApplicableForStoringLevel(ItemStack stack) {
        return stack.is(BovineItems.CUSTOM_FLOWER.get()) || stack.is(BovineItems.CUSTOM_MUSHROOM.get()) || stack.is(BovineItems.CUSTOM_MUSHROOM_BLOCK.get());
    }
}
