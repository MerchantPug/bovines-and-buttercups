package net.merchantpug.bovinesandbuttercups.mixin.inspecio;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.component.BovineEntityComponents;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import io.github.queerbric.inspecio.InspecioConfig;
import io.github.queerbric.inspecio.tooltip.EntityTooltipComponent;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(EntityTooltipComponent.class)
public class EntityTooltipComponentMixin {
    @Inject(method = "adjustEntity", at = @At("HEAD"))
    private static void bovinesandbuttercups$adjustCowEntities(Entity entity, CompoundTag itemTag, InspecioConfig.EntitiesConfig config, CallbackInfo ci) {
        Level level = Minecraft.getInstance().level;
        if (entity instanceof FlowerCow flowerCow) {
            List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> moobloomList = new ArrayList<>();
            int totalWeight = 0;

            for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
                if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration configuration)) continue;
                if (FlowerCow.getTotalSpawnWeight(level, Minecraft.getInstance().player.blockPosition()) > 0) {
                    Registry<Biome> registry = level.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY);

                    HolderSet<Biome> entryList = null;

                    if (configuration.getNaturalSpawnWeight() > 0 && configuration.getBiomeTagKey().isPresent()) {
                        TagKey<Biome> tag = configuration.getBiomeTagKey().get();
                        var optionalList = registry.getTag(tag);
                        if(optionalList.isPresent()) {
                            entryList = optionalList.get();
                        }
                        if (entryList != null && entryList.contains(level.getBiome(Minecraft.getInstance().player.blockPosition()))) {
                            moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType);
                            totalWeight += configuration.getNaturalSpawnWeight();
                        }
                    }
                } else {
                    if (configuration.getNaturalSpawnWeight() > 0) {
                        moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType);
                        totalWeight += configuration.getNaturalSpawnWeight();
                    }
                }
            }

            if (moobloomList.size() == 1) {
                flowerCow.setFlowerType(moobloomList.get(0), Minecraft.getInstance().level);
            } else if (!moobloomList.isEmpty()) {
                int i = (Minecraft.getInstance().player.tickCount / (160 / totalWeight) % totalWeight);
                for (ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> cct : moobloomList) {
                    i -= cct.getConfiguration().getNaturalSpawnWeight();
                    if (i < 0) {
                        flowerCow.setFlowerType(cct, Minecraft.getInstance().level);
                        break;
                    }
                }
            }
        } else if (BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.isProvidedBy(entity)) {
            List<ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>> mooshroomList = new ArrayList<>();
            int totalWeight = 0;

            for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof MushroomCowConfiguration).toList()) {
                if (!(cowType.getConfiguration() instanceof MushroomCowConfiguration configuration)) continue;
                if (FlowerCow.getTotalSpawnWeight(level, Minecraft.getInstance().player.blockPosition()) > 0) {
                    Registry<Biome> registry = level.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY);

                    HolderSet<Biome> entryList = null;

                    if (configuration.getNaturalSpawnWeight() > 0 && configuration.getBiomeTagKey().isPresent()) {
                        TagKey<Biome> tag = configuration.getBiomeTagKey().get();
                        var optionalList = registry.getTag(tag);
                        if(optionalList.isPresent()) {
                            entryList = optionalList.get();
                        }
                        if (entryList != null && entryList.contains(level.getBiome(Minecraft.getInstance().player.blockPosition()))) {
                            mooshroomList.add((ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>) cowType);
                            totalWeight += configuration.getNaturalSpawnWeight();
                        }
                    }
                } else {
                    if (configuration.getNaturalSpawnWeight() > 0) {
                        mooshroomList.add((ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>) cowType);
                        totalWeight += configuration.getNaturalSpawnWeight();
                    }
                }
            }

            if (mooshroomList.size() == 0) {
                BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(entity).setMushroomCowType(BovinesAndButtercups.asResource("red_mushroom"));
            } else if (mooshroomList.size() == 1) {
                BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(entity).setMushroomCowType(BovineRegistryUtil.getConfiguredCowTypeKey(level, mooshroomList.get(0)));
            } else {
                int i = (Minecraft.getInstance().player.tickCount / (160 / totalWeight) % totalWeight);
                for (ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> cfc : mooshroomList) {
                    i -= cfc.getConfiguration().getNaturalSpawnWeight();
                    if (i < 0) {
                        BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(entity).setMushroomCowType(BovineRegistryUtil.getConfiguredCowTypeKey(level, mooshroomList.get(0)));
                        break;
                    }
                }
            }
        }
    }
}