package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.registry.condition.BovineBiomeConditionTypes;
import net.merchantpug.bovinesandbuttercups.registry.condition.BovineBlockConditionTypes;
import net.merchantpug.bovinesandbuttercups.registry.condition.BovineEntityConditionTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BovinesAndButtercups {
    public static final String MOD_ID = "bovinesandbuttercups";
    public static final String MOD_NAME = "Bovines and Buttercups";
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    public static String VERSION;

    public static void init() {
        LOG.debug("Bovines and Buttercups v" + VERSION + " is starting up. Moo!");

        BovineItems.register();
        BovineBlocks.register();
        BovineBlockEntityTypes.register();
        BovineBiomeConditionTypes.register();
        BovineBlockConditionTypes.register();
        BovineEntityConditionTypes.register();
        BovineCowTypes.register();
        BovineCriteriaTriggers.register();
        BovineEffects.register();
        BovineEntityTypes.register();
        BovineParticleTypes.register();
        BovineRecipeSerializers.register();
        BovineSoundEvents.register();
        BovineStructureTypes.register();
    }

    public static MinecraftServer getServer() {
        return Services.PLATFORM.getServer();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
