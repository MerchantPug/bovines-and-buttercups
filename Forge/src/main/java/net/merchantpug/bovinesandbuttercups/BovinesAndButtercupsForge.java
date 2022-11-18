package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.capabilities.*;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.command.EffectLockdownCommand;
import net.merchantpug.bovinesandbuttercups.content.entity.goal.MoveToFlowerCowGoal;
import net.merchantpug.bovinesandbuttercups.content.entity.goal.PollinateFlowerCowGoal;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.util.ItemLevelUtil;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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

        this.addModBusEventListeners();
        this.addForgeBusEventListeners();
    }

    private void addModBusEventListeners() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener((EntityAttributeCreationEvent event) -> {
            event.put(Services.PLATFORM.getMoobloomEntity(), FlowerCow.createAttributes().build());
        });
        eventBus.addListener((FMLCommonSetupEvent event) -> {
            BovinePacketHandler.register();
            event.enqueueWork(() -> SpawnPlacements.register(Services.PLATFORM.getMoobloomEntity(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn));
        });
    }

    public void addForgeBusEventListeners() {
        IEventBus eventBus = MinecraftForge.EVENT_BUS;

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
                    ((CustomFlowerPotBlockEntity)level.getBlockEntity(pos)).setFlowerTypeName(BovineRegistryUtil.getFlowerTypeKey(level, CustomFlowerItem.getFlowerTypeFromTag(level, stack).orElse(FlowerType.MISSING)).toString());
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
                    ((CustomMushroomPotBlockEntity)level.getBlockEntity(pos)).setMushroomTypeName(BovineRegistryUtil.getMushroomTypeKey(level, CustomMushroomItem.getMushroomTypeFromTag(level, stack).orElse(MushroomType.MISSING)).toString());
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

        /*
        I have had to put the ALLOW implementation of this within a Common mixin because I'm unable to allow Mooshroom spawns because of 'checkMushroomSpawnRules'.
        I should probably write an issue for this...
        */
        eventBus.addListener((LivingSpawnEvent.CheckSpawn event) -> {
            if (!(event.getEntity() instanceof MushroomCow)) return;

            if (event.getLevel().getBiome(event.getEntity().blockPosition()).is(Biomes.MUSHROOM_FIELDS) && MushroomCowSpawnUtil.getTotalSpawnWeight(event.getLevel(), event.getEntity().blockPosition()) < 1 && BovineRegistryUtil.configuredCowTypeStream(event.getLevel()).filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).anyMatch(cct -> cct.getConfiguration().getNaturalSpawnWeight() > 0)) {
                event.setResult(Event.Result.DENY);
            }
        });

        eventBus.addListener((PlayerEvent.StartTracking event) -> {
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
            } else if (event.getTarget() instanceof Bee bee && ((BeeAccess) bee).bovinesandbuttercups$getPollinateFlowerCowGoal() == null) {
                PollinateFlowerCowGoal pollinateGoal = new PollinateFlowerCowGoal(bee);
                bee.goalSelector.addGoal(4, pollinateGoal);
                bee.goalSelector.addGoal(6, new MoveToFlowerCowGoal(bee));
                ((BeeAccess) bee).bovinesandbuttercups$setPollinateFlowerCowGoal(pollinateGoal);
            }
        });

        eventBus.addListener((LivingEvent.LivingTickEvent event) -> {
            if (event.getEntity() instanceof Bee bee && !event.getEntity().getLevel().isClientSide() && ((BeeAccess)event.getEntity()).bovinesandbuttercups$getPollinateFlowerCowGoal() != null) {
                ((BeeAccess)bee).bovinesandbuttercups$getPollinateFlowerCowGoal().tickCooldown();
            }
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                BovineCriteriaTriggers.NEARBY_ENTITY.trigger(serverPlayer);
            }
            if (event.getEntity() instanceof Player player) {
                for (int i = 0; i < player.containerMenu.slots.size(); ++i) {
                    ItemStack stack = player.containerMenu.slots.get(i).getItem();
                    if (!ItemLevelUtil.isApplicableForStoringLevel(stack) || ((ItemStackAccess)(Object)stack).bovinesandbuttercups$getLevel() != null) continue;
                    ((ItemStackAccess)(Object)stack).bovinesandbuttercups$setLevel(player.getLevel());
                }
                for (int i = 0; i < player.inventoryMenu.slots.size(); ++i) {
                    ItemStack stack = player.inventoryMenu.getSlot(i).getItem();
                    if (!ItemLevelUtil.isApplicableForStoringLevel(stack) || ((ItemStackAccess)(Object)stack).bovinesandbuttercups$getLevel() != null) continue;
                    ((ItemStackAccess)(Object)stack).bovinesandbuttercups$setLevel(player.getLevel());
                }
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

        eventBus.addListener((MobEffectEvent.Added event) -> {
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
        eventBus.addListener((MobEffectEvent.Expired event) -> {
            if (!(event.getEffectInstance().getEffect() instanceof LockdownEffect)) return;
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                cap.getLockdownMobEffects().clear();
            });
        });
        eventBus.addListener((MobEffectEvent.Applicable event) -> {
            event.getEntity().getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
                if (cap.getLockdownMobEffects().containsKey(event.getEffectInstance().getEffect())) {
                    event.setResult(Event.Result.DENY);
                }
            });
        });
    }
}