package net.merchantpug.bovinesandbuttercups;

import com.google.common.collect.ImmutableMap;
import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.capabilities.*;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.command.EffectLockdownCommand;
import net.merchantpug.bovinesandbuttercups.content.entity.goal.MoveToFlowerCowGoal;
import net.merchantpug.bovinesandbuttercups.content.entity.goal.PollinateFlowerCowGoal;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.data.loader.ConfiguredCowTypeReloadListener;
import net.merchantpug.bovinesandbuttercups.data.loader.FlowerTypeReloadListener;
import net.merchantpug.bovinesandbuttercups.data.loader.MushroomTypeReloadListener;
import net.merchantpug.bovinesandbuttercups.mixin.forge.MobSpawnSettingsAccessor;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowChildTypeUtil;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod(BovinesAndButtercups.MOD_ID)
public class BovinesAndButtercupsForge {
    public BovinesAndButtercupsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BovinesAndButtercups.VERSION = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();

        BovineRegistriesForge.init(eventBus);
        BovinesAndButtercups.init();

        this.addModBusEventListeners();
        this.addForgeBusEventListeners();
    }

    private void addModBusEventListeners() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener((EntityAttributeCreationEvent event) -> {
            event.put(BovineEntityTypes.MOOBLOOM.get(), FlowerCow.createAttributes().build());
        });
        eventBus.addListener((FMLCommonSetupEvent event) -> {
            BovinePacketHandler.register();
            event.enqueueWork(BovinesAndButtercupsForge::registerCompostables);
            BovineCowTypes.registerDefaultConfigureds();
        });
        eventBus.addListener((SpawnPlacementRegisterEvent event) -> {
            event.register(BovineEntityTypes.MOOBLOOM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn, SpawnPlacementRegisterEvent.Operation.AND);
            event.register(EntityType.MOOSHROOM, (entityType, levelAccessor, mobSpawnType, blockPos, randomSource) -> (levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) || !levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) && Animal.isBrightEnoughToSpawn(levelAccessor, blockPos) && (MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) > 0 || BovineRegistryUtil.configuredCowTypeStream().filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).noneMatch(cct -> cct.getConfiguration().getSettings().naturalSpawnWeight() > 0)), SpawnPlacementRegisterEvent.Operation.AND);
        });
    }

    private static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(BovineItems.BUTTERCUP.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.PINK_DAISY.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.LIMELIGHT.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.BIRD_OF_PARADISE.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.CHARGELILY.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.HYACINTH.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.SNOWDROP.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.TROPICAL_BLUE.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.FREESIA.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.CUSTOM_FLOWER.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.CUSTOM_MUSHROOM.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(BovineItems.CUSTOM_MUSHROOM_BLOCK.get(), 0.85F);
    }

    public void addForgeBusEventListeners() {
        IEventBus eventBus = MinecraftForge.EVENT_BUS;

        eventBus.addListener((AddReloadListenerEvent event) -> {
            event.addListener(new ConfiguredCowTypeReloadListener());
            event.addListener(new FlowerTypeReloadListener());
            event.addListener(new MushroomTypeReloadListener());
        });

        /*
           The below is slightly cursed due to not utilising Forge's biome modifier system, but there's not much that I
           can do to avoid this as it is unable to modify after reload listeners.
        */
        // TODO. Fix this
        eventBus.addListener((ServerStartingEvent event) -> {
            var biomes = event.getServer().registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);

            biomes.holders().forEach(biome -> {
                boolean hasModified = false;
                var unfrozenList = new HashMap<>(((MobSpawnSettingsAccessor)biome.value().getMobSettings()).bovinesandbuttercups$getSpawners());
                var creatureCategory = new ArrayList<>(unfrozenList.get(MobCategory.CREATURE).unwrap());
                if (BovineRegistryUtil.configuredCowTypeStream().anyMatch(configuredCowType -> configuredCowType.getCowType() == BovineCowTypes.FLOWER_COW_TYPE.get() && configuredCowType.getConfiguration().getSettings().biomes().isPresent() && biome.is(configuredCowType.getConfiguration().getSettings().biomes().get()) && configuredCowType.getConfiguration().getSettings().naturalSpawnWeight() > 0)) {
                    creatureCategory.add(new MobSpawnSettings.SpawnerData(BovineEntityTypes.MOOBLOOM.get(), 15, 4, 4));
                    hasModified = true;
                }
                if (biomes.getResourceKey(biome.value()).isPresent() && biomes.getResourceKey(biome.value()).get() != Biomes.MUSHROOM_FIELDS && BovineRegistryUtil.configuredCowTypeStream().anyMatch(configuredCowType -> configuredCowType.getCowType() == BovineCowTypes.MUSHROOM_COW_TYPE.get() && configuredCowType.getConfiguration().getSettings().biomes().isPresent() && biome.is(configuredCowType.getConfiguration().getSettings().biomes().get()) && configuredCowType.getConfiguration().getSettings().naturalSpawnWeight() > 0)) {
                    creatureCategory.add(new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 15, 4, 4));
                    hasModified = true;
                }
                if (hasModified) {
                    unfrozenList.put(MobCategory.CREATURE, WeightedRandomList.create(creatureCategory));
                    ((MobSpawnSettingsAccessor)biome.value().getMobSettings()).bovinesandbuttercups$setSpawners(ImmutableMap.copyOf(unfrozenList));
                }
            });
        });

        eventBus.addListener((OnDatapackSyncEvent event) -> {
            HashMap<ResourceLocation, ConfiguredCowType<?, ?>> configuredCowTypeMap = new HashMap<>();
            ConfiguredCowTypeRegistry.asStream().forEach(entry -> {
                if (entry.getValue().equals(BovineRegistryUtil.getDefaultMoobloom(entry.getValue().getCowType()))) return;
                configuredCowTypeMap.put(entry.getKey(), entry.getValue());
            });

            HashMap<ResourceLocation, FlowerType> flowerTypeMap = new HashMap<>();
            FlowerTypeRegistry.asStream().forEach(entry -> {
                if (entry.getValue() == FlowerType.MISSING) return;
                flowerTypeMap.put(entry.getKey(), entry.getValue());
            });

            HashMap<ResourceLocation, MushroomType> mushroomTypeMap = new HashMap<>();
            MushroomTypeRegistry.asStream().forEach(entry -> {
                if (entry.getValue() == MushroomType.MISSING) return;
                mushroomTypeMap.put(entry.getKey(), entry.getValue());
            });

            var packet = new SyncDatapackContentsPacket(configuredCowTypeMap, flowerTypeMap, mushroomTypeMap);
            BovinePacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(event::getPlayer), packet);
        });

        eventBus.addListener((PlayerInteractEvent.RightClickBlock event) -> {
            ItemStack stack = event.getItemStack();
            Player player = event.getEntity();
            Level level = event.getLevel();
            BlockPos pos = event.getPos();

            if (!(level.getBlockState(pos).getBlock() instanceof FlowerPotBlock)) return;

            if ((player.getMainHandItem().getItem() instanceof CustomFlowerItem || player.getMainHandItem().getItem() instanceof CustomMushroomItem) && event.getHand() == InteractionHand.OFF_HAND) {
                event.setCanceled(true);
            }

            if (stack.getItem() instanceof CustomFlowerItem) {
                FlowerPotBlock block = ((FlowerPotBlock)level.getBlockState(pos).getBlock());
                if (block.getEmptyPot() == block && event.getHand() == InteractionHand.MAIN_HAND) {
                    level.setBlock(pos, BovineBlocks.POTTED_CUSTOM_FLOWER.get().defaultBlockState(), 3);
                    ((CustomFlowerPotBlockEntity)level.getBlockEntity(pos)).setFlowerTypeName(BovineRegistryUtil.getFlowerTypeKey(CustomFlowerItem.getFlowerTypeFromTag(stack).orElse(FlowerType.MISSING)).toString());
                    level.getBlockEntity(pos).setChanged();
                    level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), Block.UPDATE_ALL);
                    player.awardStat(Stats.POT_FLOWER);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    if (level.isClientSide) {
                        player.swing(event.getHand());
                    }
                } else {
                    event.setCanceled(true);
                }
            } else if (stack.getItem() instanceof CustomMushroomItem) {
                FlowerPotBlock block = ((FlowerPotBlock)level.getBlockState(pos).getBlock());
                if (block.getEmptyPot() == block) {
                    level.setBlock(pos, BovineBlocks.POTTED_CUSTOM_MUSHROOM.get().defaultBlockState(), 3);
                    ((CustomMushroomPotBlockEntity)level.getBlockEntity(pos)).setMushroomTypeName(BovineRegistryUtil.getMushroomTypeKey(CustomMushroomItem.getMushroomTypeFromTag(stack).orElse(MushroomType.MISSING)).toString());
                    level.getBlockEntity(pos).setChanged();
                    level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), Block.UPDATE_ALL);
                    player.awardStat(Stats.POT_FLOWER);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                    if (level.isClientSide) {
                        player.swing(event.getHand());
                    }
                } else {
                    event.setCanceled(true);
                }
            }
        });

        eventBus.addListener((LivingSpawnEvent.CheckSpawn event) -> {
            if (event.getEntity() instanceof MushroomCow && event.getLevel().getBiome(event.getEntity().blockPosition()).is(Biomes.MUSHROOM_FIELDS) && MushroomCowSpawnUtil.getTotalSpawnWeight(event.getLevel(), event.getEntity().blockPosition()) < 1 && BovineRegistryUtil.configuredCowTypeStream().filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).anyMatch(cct -> cct.getConfiguration().getSettings().naturalSpawnWeight() > 0) || event.getEntity() instanceof Cow && event.getLevel().getBiome(event.getEntity().blockPosition()).is(BovineTags.PREVENT_COW_SPAWNS)) {
                event.setResult(Event.Result.DENY);
            }
        });

        eventBus.addListener((PlayerEvent.StartTracking event) -> {
            if (event.getTarget() instanceof MushroomCow cow) {
                cow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(cap -> {
                    if (cap.getMushroomCowTypeKey() == null) {
                        if (BovineRegistryUtil.configuredCowTypeStream().filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).allMatch(cct -> cct.getConfiguration().getSettings().naturalSpawnWeight() == 0)) {
                            if (cow.getMushroomType() == MushroomCow.MushroomType.BROWN) {
                                cap.setMushroomType(BovinesAndButtercups.asResource("brown_mushroom"));
                            } else {
                                cap.setMushroomType(BovinesAndButtercups.asResource("red_mushroom"));
                            }
                        } else if (MushroomCowSpawnUtil.getTotalSpawnWeight(event.getTarget().getLevel(), cow.blockPosition()) > 0) {
                            cap.setMushroomType(MushroomCowSpawnUtil.getMooshroomSpawnTypeDependingOnBiome(event.getTarget().getLevel(), cow.blockPosition(), cow.getRandom()));
                        } else {
                            cap.setMushroomType(MushroomCowSpawnUtil.getMooshroomSpawnType(cow.getRandom()));
                        }
                    }
                });
                cow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(MushroomCowTypeCapabilityImpl::sync);
            } else if (event.getTarget() instanceof Bee bee && ((BeeAccess) bee).bovinesandbuttercups$getPollinateFlowerCowGoal() == null) {
                PollinateFlowerCowGoal pollinateGoal = new PollinateFlowerCowGoal(bee);
                bee.goalSelector.addGoal(4, pollinateGoal);
                bee.goalSelector.addGoal(6, new MoveToFlowerCowGoal(bee));
                ((BeeAccess) bee).bovinesandbuttercups$setPollinateFlowerCowGoal(pollinateGoal);
            }
        });

        eventBus.addListener((LivingEvent.LivingTickEvent event) -> {
            if (event.getEntity().hasEffect(BovineEffects.LOCKDOWN.get())) {
                HashMap<MobEffect, Integer> lockdownEffectsToUpdate = new HashMap<>();
                Services.COMPONENT.getLockdownMobEffects(event.getEntity()).forEach(((statusEffect, integer) -> {
                    if (integer > 0) {
                        lockdownEffectsToUpdate.put(statusEffect, --integer);
                    }
                }));
                Services.COMPONENT.setLockdownMobEffects(event.getEntity(), lockdownEffectsToUpdate);
            }
            if (event.getEntity() instanceof Bee bee && !event.getEntity().getLevel().isClientSide() && ((BeeAccess)event.getEntity()).bovinesandbuttercups$getPollinateFlowerCowGoal() != null) {
                ((BeeAccess)bee).bovinesandbuttercups$getPollinateFlowerCowGoal().tickCooldown();
            }
        });
        eventBus.addListener((LivingHurtEvent event) -> {
            if (!(event.getEntity() instanceof Bee bee) || ((BeeAccess)event.getEntity()).bovinesandbuttercups$getPollinateFlowerCowGoal() == null) return;
            ((BeeAccess)bee).bovinesandbuttercups$getPollinateFlowerCowGoal().stopPollinating();
        });

        eventBus.addGenericListener(Entity.class, MushroomCowTypeCapabilityAttacher::attach);
        eventBus.addGenericListener(Entity.class, LockdownEffectCapabilityAttacher::attach);
        eventBus.addGenericListener(Entity.class, FlowerCowTargetCapabilityAttacher::attach);

        eventBus.addListener((RegisterCommandsEvent event) -> EffectLockdownCommand.register(event.getDispatcher()));

        eventBus.addListener((BabyEntitySpawnEvent event) -> {
            Mob parentA = event.getParentA();
            Mob parentB = event.getParentB();
            AgeableMob child = event.getChild();

            if (parentA instanceof MushroomCow mushroomCowA && parentB instanceof MushroomCow mushroomCowB && child instanceof MushroomCow mushroomCowChild)
                mushroomCowChild.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(cap -> cap.setMushroomType(MushroomCowChildTypeUtil.chooseMooshroomBabyType(mushroomCowA, mushroomCowB, mushroomCowChild, event.getCausedByPlayer())));
        });

        eventBus.addListener((MobEffectEvent.Added event) -> {
            LivingEntity entity = event.getEntity();

            if (event.getEffectInstance().getEffect() instanceof LockdownEffect && entity.getCapability(LockdownEffectCapability.INSTANCE).isPresent()) {
                Optional<Map<MobEffect, Integer>> optional = entity.getCapability(LockdownEffectCapability.INSTANCE).map(LockdownEffectCapabilityImpl::getLockdownMobEffects);
                if (optional.isEmpty() || optional.get().values().stream().allMatch(value -> value < event.getEffectInstance().getDuration())) {
                    Optional<Holder<MobEffect>> randomEffect = Registry.MOB_EFFECT.getRandom(entity.level.random);
                    randomEffect.ifPresent(entry -> event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                        cap.addLockdownMobEffect(entry.value(), event.getEffectInstance().getDuration());
                        cap.sync();
                    }));
                }
                if (!entity.level.isClientSide && entity instanceof ServerPlayer serverPlayer && optional.isPresent() && !optional.get().isEmpty()) {
                    optional.get().forEach((effect1, duration) -> {
                        if (!serverPlayer.hasEffect(effect1)) return;
                        BovineCriteriaTriggers.LOCK_EFFECT.trigger(serverPlayer, effect1);
                    });
                }
            }
        });
        eventBus.addListener((MobEffectEvent.Remove event) -> {
            if (!(event.getEffectInstance().getEffect() instanceof LockdownEffect)) return;
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                cap.getLockdownMobEffects().clear();
                cap.sync();
            });
        });
        eventBus.addListener((MobEffectEvent.Expired event) -> {
            if (!(event.getEffectInstance().getEffect() instanceof LockdownEffect)) return;
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                cap.getLockdownMobEffects().clear();
                cap.sync();
            });
        });
        eventBus.addListener((MobEffectEvent.Applicable event) -> {
            Entity entity = event.getEntity();
            entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                if (cap.getLockdownMobEffects().containsKey(event.getEffectInstance().getEffect())) {
                    if (!entity.level.isClientSide && entity instanceof ServerPlayer serverPlayer) {
                        BovineCriteriaTriggers.PREVENT_EFFECT.trigger(serverPlayer, event.getEffectInstance().getEffect());
                    }
                    event.setResult(Event.Result.DENY);
                }
            });
        });
    }
}