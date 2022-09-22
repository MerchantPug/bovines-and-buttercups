package com.github.merchantpug.bovinesandbuttercups.mixin.inspecio;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import com.github.merchantpug.bovinesandbuttercups.util.ConfiguredCowTypeRegistryUtil;
import io.github.queerbric.inspecio.InspecioConfig;
import io.github.queerbric.inspecio.tooltip.EntityTooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(EntityTooltipComponent.class)
public class EntityTooltipComponentMixin {
    @Inject(method = "adjustEntity", at = @At("HEAD"))
    private static void bovinesandbuttercups$adjustCowEntities(Entity entity, CompoundTag itemTag, InspecioConfig.EntitiesConfig config, CallbackInfo ci) {
        if (entity instanceof FlowerCow flowerCow) {
            List<ConfiguredCowType<FlowerCowConfiguration, ?>> moobloomList = new ArrayList<>();
            int totalWeight = 0;

            for (ConfiguredCowType<?, ?> cowType : ConfiguredCowTypeRegistryUtil.configuredCowTypeStream(Minecraft.getInstance().level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
                if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration flowerCowConfiguration)) continue;

                if (flowerCowConfiguration.naturalSpawnWeight() > 0) {
                    moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, ?>) cowType);
                    totalWeight += flowerCowConfiguration.naturalSpawnWeight();
                }
            }

            if (moobloomList.size() == 1) {
                flowerCow.setFlowerCowType(moobloomList.get(0), Minecraft.getInstance().level);
            } else if (!moobloomList.isEmpty()) {
                int i = (Minecraft.getInstance().player.tickCount / (160 / totalWeight) % totalWeight);
                for (ConfiguredCowType<FlowerCowConfiguration, ?> cfc : moobloomList) {
                    i -= cfc.getConfiguration().naturalSpawnWeight();
                    if (i < 0) {
                        flowerCow.setFlowerCowType(cfc, Minecraft.getInstance().level);
                        break;
                    }
                }
            }
        }
    }
}
