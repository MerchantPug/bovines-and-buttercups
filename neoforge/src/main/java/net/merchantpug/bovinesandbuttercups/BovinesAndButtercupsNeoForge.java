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
import net.minecraft.server.packs.repository.BuiltInPackSource;
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
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.locating.IModFile;

import java.util.*;

@Mod(BovinesAndButtercups.MOD_ID)
public class BovinesAndButtercupsNeoForge {
    public BovinesAndButtercupsNeoForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BovinesAndButtercups.VERSION = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();

        BovinesAndButtercups.init();
        BovineCreativeTabs.init(eventBus);
        BovineBiomeModifierSerializers.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = BovinesAndButtercups.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusListeners {

        @SubscribeEvent
        public static void newRegistries(NewRegistryEvent event) {
            event.register(BovineRegistriesNeoForge.COW_TYPE);
            event.register(BovineRegistriesNeoForge.ENTITY_CONDITION_TYPE);
            event.register(BovineRegistriesNeoForge.BLOCK_CONDITION_TYPE);
            event.register(BovineRegistriesNeoForge.BIOME_CONDITION_TYPE);
        }

        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            BovinePacketHandler.register();
            event.enqueueWork(BovinesAndButtercupsNeoForge::registerCompostables);
            BovineCowTypes.registerDefaultConfigureds();
        }

        @SubscribeEvent
        public static void createEntityAttributes(EntityAttributeCreationEvent event) {
            event.put(BovineEntityTypes.MOOBLOOM.get(), FlowerCow.createAttributes().build());
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
            event.register(BovineEntityTypes.MOOBLOOM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
            event.register(EntityType.MOOSHROOM, (entityType, levelAccessor, mobSpawnType, blockPos, randomSource) -> (levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) || !levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) && Animal.isBrightEnoughToSpawn(levelAccessor, blockPos) && (MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) > 0 || (MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) > 0 || MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) == 0 && levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && BovineRegistryUtil.configuredCowTypeStream().anyMatch(configuredCowType -> configuredCowType.configuration() instanceof MushroomCowConfiguration mushroomCowConfiguration && mushroomCowConfiguration.usesVanillaSpawningHack()) && MushroomCow.checkMushroomSpawnRules(entityType, levelAccessor, mobSpawnType, blockPos, randomSource))), SpawnPlacementRegisterEvent.Operation.REPLACE);
        }

        @SubscribeEvent
        public static void buildCreativeModeTabs(BuildCreativeModeTabContentsEvent event) {
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
                event.accept(BovineItems.MOOBLOOM_SPAWN_EGG.get());
            }
        }

        @SubscribeEvent
        public static void addPackFinders(AddPackFindersEvent event) {
            if (event.getPackType() == PackType.CLIENT_RESOURCES) {
                IModFileInfo modFileInfo = ModList.get().getModFileById(BovinesAndButtercups.MOD_ID);
                if (modFileInfo == null) {
                    BovinesAndButtercups.LOG.error("Could not find Bovines and Buttercups mod file info; built-in resource packs will be missing!");
                    return;
                }
                IModFile modFile = modFileInfo.getFile();
                event.addRepositorySource((consumer) -> {
                    consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("mojang").toString(), Component.translatable("resourcePack.bovinesandbuttercups.mojang.name"), false, BuiltInPackSource.fixedResources(new ModFilePackResources("bovinesandbuttercups/mojang", modFile, "resourcepacks/mojang")), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
                    consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("no_grass").toString(), Component.translatable("resourcePack.bovinesandbuttercups.noGrass.name"), false, BuiltInPackSource.fixedResources(new ModFilePackResources("bovinesandbuttercups/no_grass", modFile, "resourcepacks/no_grass")), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
                    consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("no_buds").toString(), Component.translatable("resourcePack.bovinesandbuttercups.noBuds.name"), false, BuiltInPackSource.fixedResources(new ModFilePackResources("bovinesandbuttercups/no_buds", modFile, "resourcepacks/no_buds")), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
                });
            }
        }
    }

    @Mod.EventBusSubscriber(modid = BovinesAndButtercups.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class NeoEventBusListeners {

        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof LivingEntity living) {
                event.addCapability(LockdownEffectCapability.ID, new LockdownEffectCapabilityImpl(living));
            }
            if (event.getObject() instanceof MushroomCow mushroomCow) {
                event.addCapability(MushroomCowTypeCapability.ID, new MushroomCowTypeCapabilityImpl(mushroomCow));
            } else if (event.getObject() instanceof Bee bee) {
                event.addCapability(FlowerCowTargetCapability.ID, new FlowerCowTargetCapabilityImpl(bee));
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(LockdownEffectCapabilityImpl::sync);
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerChangedDimensionEvent event) {
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(LockdownEffectCapabilityImpl::sync);
        }

        @SubscribeEvent
        public static void onServerAboutToStart(ServerAboutToStartEvent event) {
            BovinesAndButtercups.setServer(event.getServer());
        }

        @SubscribeEvent
        public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
            if (!event.getItemStack().is(Items.SHEARS)) return;
            if (event.getTarget() instanceof FlowerCow cow && !cow.shouldAllowShearing()) {
                event.setCanceled(true);
            }
            event.getTarget().getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(cap -> {
                if (!cap.shouldAllowShearing()) {
                    event.setCanceled(true);
                }
            });
        }

        @SubscribeEvent
        public static void addReloadListeners(AddReloadListenerEvent event) {
            event.addListener(new ConfiguredCowTypeReloadListener());
            event.addListener(new FlowerTypeReloadListener());
            event.addListener(new MushroomTypeReloadListener());
        }

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            EffectLockdownCommand.register(event.getDispatcher(), event.getBuildContext());
        }

        @SubscribeEvent
        public static void onDatapackSync(OnDatapackSyncEvent event) {
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
        }

        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
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
        }

        @SubscribeEvent
        public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
            if (event.getEntity() instanceof MushroomCow && event.getLevel().getBiome(event.getEntity().blockPosition()).is(Biomes.MUSHROOM_FIELDS) && MushroomCowSpawnUtil.getTotalSpawnWeight(event.getLevel(), event.getEntity().blockPosition()) < 1 && BovineRegistryUtil.configuredCowTypeStream().filter(cct -> cct.configuration() instanceof MushroomCowConfiguration).anyMatch(cct -> cct.configuration().getSettings().naturalSpawnWeight() > 0)) {
                event.setSpawnCancelled(true);
            }
        }

        @SubscribeEvent
        public static void onStartTracking(PlayerEvent.StartTracking event) {
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
        }

        @SubscribeEvent
        public static void onLivingTick(LivingEvent.LivingTickEvent event) {
            if (event.getEntity().hasEffect(BovineEffects.LOCKDOWN.get())) {
                HashMap<MobEffect, Integer> lockdownEffectsToUpdate = new HashMap<>();
                Services.COMPONENT.getLockdownMobEffects(event.getEntity()).forEach(((statusEffect, integer) -> {
                    if (integer > 0) {
                        lockdownEffectsToUpdate.put(statusEffect, --integer);
                    } else if (integer == -1) {
                        lockdownEffectsToUpdate.put(statusEffect, -1);
                    }
                }));
                Services.COMPONENT.setLockdownMobEffects(event.getEntity(), lockdownEffectsToUpdate);
                Services.COMPONENT.syncLockdownMobEffects(event.getEntity());
            }
            if (event.getEntity() instanceof Bee bee && !event.getEntity().level().isClientSide() && ((BeeAccess)event.getEntity()).bovinesandbuttercups$getPollinateFlowerCowGoal() != null) {
                ((BeeAccess)bee).bovinesandbuttercups$getPollinateFlowerCowGoal().tickCooldown();
            }
        }

        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent event) {
            if (!(event.getEntity() instanceof Bee bee) || ((BeeAccess)event.getEntity()).bovinesandbuttercups$getPollinateFlowerCowGoal() == null) return;
            ((BeeAccess)bee).bovinesandbuttercups$getPollinateFlowerCowGoal().stopPollinating();
        }

        @SubscribeEvent
        public static void onBabyEntitySpawn(BabyEntitySpawnEvent event) {
            Mob parentA = event.getParentA();
            Mob parentB = event.getParentB();
            AgeableMob child = event.getChild();

            if (parentA instanceof MushroomCow mushroomCowA && parentB instanceof MushroomCow mushroomCowB && child instanceof MushroomCow mushroomCowChild)
                mushroomCowChild.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(cap -> cap.setMushroomType(MushroomCowChildTypeUtil.chooseMooshroomBabyType(mushroomCowA, mushroomCowB, mushroomCowChild, event.getCausedByPlayer())));
        }

        @SubscribeEvent
        public static void onMobEffectAdded(MobEffectEvent.Added event) {
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
            }

            if (!entity.level().isClientSide && entity instanceof ServerPlayer serverPlayer && event.getEffectInstance().getEffect() instanceof LockdownEffect && entity.getCapability(LockdownEffectCapability.INSTANCE).isPresent() && entity.getCapability(LockdownEffectCapability.INSTANCE).map(cap -> !cap.getLockdownMobEffects().isEmpty()).orElse(false)) {
                entity.getCapability(LockdownEffectCapability.INSTANCE).map(LockdownEffectCapability::getLockdownMobEffects).orElse(Map.of()).forEach((effect1, duration) -> {
                    if (!entity.hasEffect(effect1)) return;
                    BovineCriteriaTriggers.LOCK_EFFECT.trigger(serverPlayer, effect1);
                });
            }
        }
        @SubscribeEvent
        public static void onMobEffectRemoved(MobEffectEvent.Remove event) {
            if (event.getEffectInstance() == null || !(event.getEffectInstance().getEffect() instanceof LockdownEffect)) return;
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                cap.getLockdownMobEffects().clear();
                cap.sync();
            });
        }
        @SubscribeEvent
        public static void onMobEffectExpired(MobEffectEvent.Expired event) {
            if (event.getEffectInstance() == null || !(event.getEffectInstance().getEffect() instanceof LockdownEffect)) return;
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                cap.getLockdownMobEffects().clear();
                cap.sync();
            });
        }
        @SubscribeEvent
        public static void changeMobEffectApplicability(MobEffectEvent.Applicable event) {
            Entity entity = event.getEntity();
            entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                if (cap.getLockdownMobEffects().containsKey(event.getEffectInstance().getEffect())) {
                    if (!entity.level().isClientSide && entity instanceof ServerPlayer serverPlayer) {
                        BovineCriteriaTriggers.PREVENT_EFFECT.trigger(serverPlayer, event.getEffectInstance().getEffect());
                    }
                    event.setResult(Event.Result.DENY);
                }
            });
        }
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
}