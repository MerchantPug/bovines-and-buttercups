package net.merchantpug.bovinesandbuttercups.util;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public class MushroomCowSpawnUtil {
    public static int getTotalSpawnWeight(LevelAccessor level, BlockPos pos) {
        int totalWeight = 0;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof MushroomCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof MushroomCowConfiguration configuration)) continue;

            if (configuration.getNaturalSpawnWeight() > 0 && configuration.getBiomes().isPresent()) {
                if (configuration.getBiomes().get().contains(level.getBiome(pos))) {
                    totalWeight += configuration.getNaturalSpawnWeight();
                }
            }
        }
        return totalWeight;
    }

    public static ResourceLocation getMooshroomSpawnType(LevelAccessor level, RandomSource random) {
        int totalWeight = 0;

        List<ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>> mooshroomList = new ArrayList<>();

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof MushroomCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof MushroomCowConfiguration mushroomCowConfiguration)) continue;

            if (mushroomCowConfiguration.getNaturalSpawnWeight() > 0) {
                mooshroomList.add((ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>) cowType);
            }
        }

        int index = 0;
        for (double r = random.nextDouble() * totalWeight; index < mooshroomList.size() - 1; ++index) {
            r -= mooshroomList.get(index).getConfiguration().getNaturalSpawnWeight();
            if (r <= 0.0) break;
        }
        if (!mooshroomList.isEmpty()) {
            return BovineRegistryUtil.getConfiguredCowTypeKey(level, mooshroomList.get(index));
        } else {
            return BovinesAndButtercups.asResource("missing_mooshroom");
        }
    }

    public static ResourceLocation getMooshroomSpawnTypeDependingOnBiome(LevelAccessor level, BlockPos pos, RandomSource random) {
        List<ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>> mooshroomList = new ArrayList<>();

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof MushroomCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof MushroomCowConfiguration configuration)) continue;

            if (configuration.getNaturalSpawnWeight() > 0 && configuration.getBiomes().isPresent()) {
                if (configuration.getBiomes().get().contains(level.getBiome(pos))) {
                    mooshroomList.add((ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>) cowType);
                }
            }
        }

        int index = 0;
        for (double r = random.nextDouble() * getTotalSpawnWeight(level, pos); index < mooshroomList.size() - 1; ++index) {
            r -= mooshroomList.get(index).getConfiguration().getNaturalSpawnWeight();
            if (r <= 0.0) break;
        }
        if (!mooshroomList.isEmpty()) {
            return BovineRegistryUtil.getConfiguredCowTypeKey(level, mooshroomList.get(index));
        } else {
            return BovinesAndButtercups.asResource("missing_mooshroom");
        }
    }
}
