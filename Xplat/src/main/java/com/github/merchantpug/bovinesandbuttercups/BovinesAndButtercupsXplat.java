package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.registry.*;

public class BovinesAndButtercupsXplat {
    public static String VERSION;

    public static void init() {
        Constants.LOG.info("Bovines and Buttercups v" + VERSION + " is starting up. Moo!");

        BovineBlocks.init();
        BovineCriteriaTriggers.init();
        BovineEffects.init();
        BovineItems.init();
        BovineParticleTypes.init();
        BovineSoundEvents.init();
    }
}