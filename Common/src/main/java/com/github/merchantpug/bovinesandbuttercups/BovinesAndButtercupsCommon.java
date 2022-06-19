package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.registry.*;
import net.minecraft.resources.ResourceLocation;

import static com.github.merchantpug.bovinesandbuttercups.Constants.MOD_ID;

public class BovinesAndButtercupsCommon {
    public static void init() {
        BovineBlocks.init();
        BovineEffects.init();
        BovineEntityTypes.init();
        BovineItems.init();
        BovineSoundEvents.init();
    }

    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}