package net.merchantpug.bovinesandbuttercups.util;

import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.world.item.ItemStack;

public class ItemLevelUtil {
    public static boolean isApplicableForStoringLevel(ItemStack stack) {
        return stack.is(Services.PLATFORM.getCustomFlowerItem()) || stack.is(Services.PLATFORM.getCustomMushroomItem()) || stack.is(Services.PLATFORM.getCustomHugeMushroomItem());
    }
}
