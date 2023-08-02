package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.capabilities.*;
import net.merchantpug.bovinesandbuttercups.client.resources.ModFilePackResources;
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
import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.util.CreativeTabHelper;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowChildTypeUtil;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

@Mod(BovinesAndButtercups.MOD_ID)
public class BovinesAndButtercupsForge {
    public BovinesAndButtercupsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BovinesAndButtercups.VERSION = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();

        BovineRegistriesForge.init(eventBus);
        BovinesAndButtercups.init();
        BovineCreativeTabs.init(eventBus);
        BovineBiomeModifierSerializers.register(eventBus);


        this.addModBusEventListeners();
        this.addForgeBusEventListeners();
    }

    private void addModBusEventListeners() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener((EntityAttributeCreationEvent event) -> event.put(BovineEntityTypes.MOOBLOOM.get(), FlowerCow.createAttributes().build()));
        eventBus.addListener((SpawnPlacementRegisterEvent event) -> {
            event.setPhase(EventPriority.HIGHEST);
            event.register(BovineEntityTypes.MOOBLOOM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(EntityType.MOOSHROOM, (entityType, levelAccessor, mobSpawnType, blockPos, randomSource) -> (levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) || !levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) && Animal.isBrightEnoughToSpawn(levelAccessor, blockPos) && (MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) > 0 || (MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) > 0 || MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) == 0 && levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && BovineRegistryUtil.configuredCowTypeStream().anyMatch(configuredCowType -> configuredCowType.configuration() instanceof MushroomCowConfiguration mushroomCowConfiguration && mushroomCowConfiguration.usesVanillaSpawningHack()) && MushroomCow.checkMushroomSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource))), SpawnPlacementRegisterEvent.Operation.REPLACE);
        });
        eventBus.addListener((FMLCommonSetupEvent event) -> {
            BovinePacketHandler.register();
            event.enqueueWork(BovinesAndButtercupsForge::registerCompostables);
            BovineCowTypes.registerDefaultConfigureds();
        });
        eventBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
            if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
                event.getEntries().putAfter(new ItemStack(Items.LILY_OF_THE_VALLEY), new ItemStack(BovineItems.FREESIA.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.getEntries().putAfter(new ItemStack(BovineItems.FREESIA.get()), new ItemStack(BovineItems.BIRD_OF_PARADISE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.getEntries().putAfter(new ItemStack(BovineItems.BIRD_OF_PARADISE.get()), new ItemStack(BovineItems.BUTTERCUP.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.getEntries().putAfter(new ItemStack(BovineItems.BUTTERCUP.get()), new ItemStack(BovineItems.LIMELIGHT.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.getEntries().putAfter(new ItemStack(BovineItems.LIMELIGHT.get()), new ItemStack(BovineItems.CHARGELILY.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.getEntries().putAfter(new ItemStack(BovineItems.CHARGELILY.get()), new ItemStack(BovineItems.TROPICAL_BLUE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.getEntries().putAfter(new ItemStack(BovineItems.TROPICAL_BLUE.get()), new ItemStack(BovineItems.HYACINTH.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.getEntries().putAfter(new ItemStack(BovineItems.HYACINTH.get()), new ItemStack(BovineItems.PINK_DAISY.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.getEntries().putAfter(new ItemStack(BovineItems.PINK_DAISY.get()), new ItemStack(BovineItems.SNOWDROP.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

                CreativeTabHelper.getCustomFlowersForCreativeTab().forEach(stack -> event.getEntries().putAfter(new ItemStack(BovineItems.SNOWDROP.get()), stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
                CreativeTabHelper.getCustomMushroomsForCreativeTab().forEach(stack -> event.getEntries().putAfter(new ItemStack(Items.RED_MUSHROOM), stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
                CreativeTabHelper.getCustomMushroomBlocksForCreativeTab().forEach(stack -> event.getEntries().putAfter(new ItemStack(Items.RED_MUSHROOM_BLOCK), stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
            } else if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
                CreativeTabHelper.getNectarBowlsForCreativeTab().forEach(stack -> event.getEntries().putAfter(new ItemStack(Items.MILK_BUCKET), stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
            } else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
                event.accept(BovineItems.MOOBLOOM_SPAWN_EGG);
            }
        });

        eventBus.addListener((AddPackFindersEvent event) -> {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                IModFileInfo modFileInfo = ModList.get().getModFileById(BovinesAndButtercups.MOD_ID);
                if (modFileInfo == null) {
                    BovinesAndButtercups.LOG.error("Could not find Bovines and Buttercups mod file info; built-in resource packs will be missing!");
                    return;
                }
                IModFile modFile = modFileInfo.getFile();
                event.addRepositorySource((consumer) -> {
                    consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("mojang").toString(), Component.translatable("resourcePack.bovinesandbuttercups.mojang.name"), false, (s) -> new ModFilePackResources("bovinesandbuttercups/mojang", modFile, "resourcepacks/mojang"), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
                    consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("no_grass").toString(), Component.translatable("resourcePack.bovinesandbuttercups.noGrass.name"), false, (s) -> new ModFilePackResources("bovinesandbuttercups/no_grass", modFile, "resourcepacks/no_grass"), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
                    consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("no_buds").toString(), Component.translatable("resourcePack.bovinesandbuttercups.noBuds.name"), false, (s) -> new ModFilePackResources("bovinesandbuttercups/no_buds", modFile, "resourcepacks/no_buds"), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
                });
            }
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

        eventBus.addListener((RegisterCommandsEvent event) -> EffectLockdownCommand.register(event.getDispatcher(), event.getBuildContext()));

        eventBus.addListener((ServerAboutToStartEvent event) -> {
            BovinesAndButtercups.setServer(event.getServer());
        });

        eventBus.addListener((OnDatapackSyncEvent event) -> {
            if (event.getPlayer() == null) return;
            HashMap<ResourceLocation, ConfiguredCowType<?, ?>> configuredCowTypeMap = new HashMap<>();
            ConfiguredCowTypeRegistry.asStream().forEach(entry -> {
                if (entry.getValue().equals(BovineRegistryUtil.getDefaultMoobloom(entry.getValue().cowType()))) return;
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

        eventBus.addListener((MobSpawnEvent event) -> {
            if (event.getEntity() instanceof MushroomCow && event.getLevel().getBiome(event.getEntity().blockPosition()).is(Biomes.MUSHROOM_FIELDS) && MushroomCowSpawnUtil.getTotalSpawnWeight(event.getLevel(), event.getEntity().blockPosition()) < 1 && BovineRegistryUtil.configuredCowTypeStream().filter(cct -> cct.configuration() instanceof MushroomCowConfiguration).anyMatch(cct -> cct.configuration().getSettings().naturalSpawnWeight() > 0)) {
                event.setResult(Event.Result.DENY);
            }
        });

        eventBus.addListener((PlayerEvent.StartTracking event) -> {
            if (event.getTarget() instanceof MushroomCow cow) {
                cow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(cap -> {
                    if (cap.getMushroomCowTypeKey() == null || cap.getMushroomCowTypeKey().equals(BovinesAndButtercups.asResource("missing_mooshroom"))) {
                        if (MushroomCowSpawnUtil.getTotalSpawnWeight(event.getTarget().level(), cow.blockPosition()) > 0) {
                            cap.setMushroomType(MushroomCowSpawnUtil.getMooshroomSpawnTypeDependingOnBiome(event.getTarget().level(), cow.blockPosition(), cow.getRandom()));
                        } else if (BovineRegistryUtil.configuredCowTypeStream().anyMatch(cct -> cct.configuration() instanceof MushroomCowConfiguration mcct && mcct.usesVanillaSpawningHack()) && cow.level().getBiome(cow.blockPosition()).is(Biomes.MUSHROOM_FIELDS)) {
                            if (cow.getVariant() == MushroomCow.MushroomType.BROWN) {
                                cap.setMushroomType(BovinesAndButtercups.asResource("brown_mushroom"));
                            } else {
                                cap.setMushroomType(BovinesAndButtercups.asResource("red_mushroom"));
                            }
                        } else {
                            cap.setMushroomType(MushroomCowSpawnUtil.getMooshroomSpawnType(cow.getRandom(), cow.getVariant()));
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
            if (event.getEntity() instanceof Bee bee && !event.getEntity().level().isClientSide() && ((BeeAccess)event.getEntity()).bovinesandbuttercups$getPollinateFlowerCowGoal() != null) {
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
                    Optional<Holder.Reference<MobEffect>> randomEffect = BuiltInRegistries.MOB_EFFECT.getRandom(entity.level().random);
                    randomEffect.ifPresent(entry -> event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                        cap.addLockdownMobEffect(entry.value(), event.getEffectInstance().getDuration());
                        cap.sync();
                    }));
                }
                if (!entity.level().isClientSide && entity instanceof ServerPlayer serverPlayer && optional.isPresent() && !optional.get().isEmpty()) {
                    optional.get().forEach((effect1, duration) -> {
                        if (!serverPlayer.hasEffect(effect1)) return;
                        BovineCriteriaTriggers.LOCK_EFFECT.trigger(serverPlayer, effect1);
                    });
                }
            }
        });
        eventBus.addListener((MobEffectEvent.Remove event) -> {
            if (event.getEffectInstance() == null || !(event.getEffectInstance().getEffect() instanceof LockdownEffect)) return;
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                cap.getLockdownMobEffects().clear();
                cap.sync();
            });
        });
        eventBus.addListener((MobEffectEvent.Expired event) -> {
            if (event.getEffectInstance() == null || !(event.getEffectInstance().getEffect() instanceof LockdownEffect)) return;
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                cap.getLockdownMobEffects().clear();
                cap.sync();
            });
        });
        eventBus.addListener((MobEffectEvent.Applicable event) -> {
            Entity entity = event.getEntity();
            entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                if (cap.getLockdownMobEffects().containsKey(event.getEffectInstance().getEffect())) {
                    if (!entity.level().isClientSide && entity instanceof ServerPlayer serverPlayer) {
                        BovineCriteriaTriggers.PREVENT_EFFECT.trigger(serverPlayer, event.getEffectInstance().getEffect());
                    }
                    event.setResult(Event.Result.DENY);
                }
            });
        });
    }
}