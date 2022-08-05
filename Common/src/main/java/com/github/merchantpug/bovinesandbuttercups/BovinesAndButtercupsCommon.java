package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.registry.*;
import net.minecraft.resources.ResourceLocation;

import static com.github.merchantpug.bovinesandbuttercups.Constants.MOD_ID;

public class BovinesAndButtercupsCommon {
    public static String VERSION;

    public static void init() {
        Constants.LOG.info("Bovines and Buttercups v" + VERSION + " is starting up. Moo!");

        BovineBlocks.init();
        BovineCriteriaTriggers.init();
        BovineEffects.init();
        BovineEntityTypes.init();
        BovineItems.init();
        BovineParticleTypes.init();
        BovineSoundEvents.init();
    }
}