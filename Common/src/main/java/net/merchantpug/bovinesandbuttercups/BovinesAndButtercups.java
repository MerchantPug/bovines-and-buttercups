package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.registry.*;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BovinesAndButtercups {
    public static final String MOD_ID = "bovinesandbuttercups";
    public static final String MOD_NAME = "Bovines and Buttercups";
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    public static String VERSION;

    public static void init() {
        LOG.info("Bovines and Buttercups v" + VERSION + " is starting up. Moo!");

        BovineItems.init();
        BovineBlocks.init();
        BovineCriteriaTriggers.init();
        BovineEffects.init();
        BovineParticleTypes.init();
        BovineRecipeSerializers.init();
        BovineSoundEvents.init();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
