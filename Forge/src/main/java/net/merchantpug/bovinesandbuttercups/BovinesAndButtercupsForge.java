package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.capabilities.*;
import net.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowSpawnUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Optional;

@Mod(BovinesAndButtercups.MOD_ID)
public class BovinesAndButtercupsForge {
    public BovinesAndButtercupsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BovinesAndButtercups.VERSION = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();

        BovinesAndButtercups.init();
        BovineBlockEntityTypesForge.init();
        BovineEntityTypesForge.init();
        BovineItemsForge.init();
        BovineRegistriesForge.init(eventBus);
        BiomeModifierSerializerRegistry.init(eventBus);

        this.addEventListeners();
    }

    private void addEventListeners() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener((EntityAttributeCreationEvent event) -> {
            event.put(Services.PLATFORM.getMoobloomEntity(), FlowerCow.createAttributes().build());
        });
        eventBus.addListener((FMLCommonSetupEvent event) -> {
            BovinePacketHandler.register();
            event.enqueueWork(() -> SpawnPlacements.register(Services.PLATFORM.getMoobloomEntity(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn));
        });

        MinecraftForge.EVENT_BUS.addListener((PlayerEvent.StartTracking event) -> {
            if (event.getTarget() instanceof MushroomCow cow) {
                cow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(cap -> {
                    if (cap.getMushroomCowTypeKey() == null) {
                        if (BovineRegistryUtil.configuredCowTypeStream(event.getTarget().getLevel()).filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).allMatch(cct -> cct.getConfiguration().getNaturalSpawnWeight() == 0)) {
                            if (cow.getMushroomType() == MushroomCow.MushroomType.BROWN) {
                                cap.setMushroomType(BovinesAndButtercups.asResource("brown_mushroom"));
                            } else {
                                cap.setMushroomType(BovinesAndButtercups.asResource("red_mushroom"));
                            }
                        } else if (MushroomCowSpawnUtil.getTotalSpawnWeight(event.getTarget().getLevel(), cow.blockPosition()) > 0) {
                            cap.setMushroomType(MushroomCowSpawnUtil.getMooshroomSpawnTypeDependingOnBiome(event.getTarget().getLevel(), cow.blockPosition(), cow.getRandom()));
                        } else {
                            cap.setMushroomType(MushroomCowSpawnUtil.getMooshroomSpawnType(event.getTarget().getLevel(), cow.getRandom()));
                        }
                    }
                });
                cow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(MushroomCowTypeCapabilityImpl::sync);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((LivingEvent.LivingTickEvent event) -> {
            if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
            BovineCriteriaTriggers.NEARBY_ENTITY.trigger(serverPlayer);
        });
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, MushroomCowTypeCapabilityAttacher::attach);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, LockdownEffectCapabilityAttacher::attach);
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> EffectLockdownCommand.register(event.getDispatcher()));

        MinecraftForge.EVENT_BUS.addListener((MobEffectEvent.Added event) -> {
            if (event.getEffectInstance().getEffect() instanceof LockdownEffect && event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).isPresent() && (event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).map(LockdownEffectCapabilityImpl::getLockdownMobEffects).isEmpty() || event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).map(LockdownEffectCapabilityImpl::getLockdownMobEffects).get().values().stream().allMatch(value -> value < event.getEffectInstance().getDuration()))) {
                Optional<Holder<MobEffect>> randomEffect = Registry.MOB_EFFECT.getRandom(event.getEntity().level.random);
                randomEffect.ifPresent(entry -> {
                    event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                        cap.addLockdownMobEffect(entry.value(), event.getEffectInstance().getDuration());
                        cap.sync();
                    });
                });
            }
        });
        MinecraftForge.EVENT_BUS.addListener((MobEffectEvent.Expired event) -> {
            if (!(event.getEffectInstance().getEffect() instanceof LockdownEffect)) return;
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                cap.getLockdownMobEffects().clear();
            });
        });
        MinecraftForge.EVENT_BUS.addListener((MobEffectEvent.Applicable event) -> {
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                if (cap.getLockdownMobEffects().containsKey(event.getEffectInstance().getEffect())) {
                    event.setResult(Event.Result.DENY);
                }
            });
        });
    }
}