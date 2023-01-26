package net.merchantpug.bovinesandbuttercups.content.item;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class NectarBowlItem extends Item {
    public static final String SOURCE_KEY = "Source";
    public static final String EFFECTS_KEY = "Effects";
    public static final String EFFECT_ID_KEY = "EffectId";
    public static final String EFFECT_DURATION_KEY = "EffectDuration";

    public NectarBowlItem(Properties properties) {
        super(properties);
    }

    public static void saveMobEffect(ItemStack nectar, MobEffect effect, int duration) {
        CompoundTag compoundTag = nectar.getOrCreateTag();
        ListTag listTag = compoundTag.getList(EFFECTS_KEY, 9);
        CompoundTag compoundTag2 = new CompoundTag();
        ResourceLocation effectLocation = Registry.MOB_EFFECT.getKey(effect);
        if (effectLocation != null) {
            compoundTag2.putString(EFFECT_ID_KEY, effectLocation.toString());
            compoundTag2.putInt(EFFECT_DURATION_KEY, duration);
        }
        listTag.add(compoundTag2);
        compoundTag.put(EFFECTS_KEY, listTag);
    }

    public static void saveMoobloomTypeKey(ItemStack nectar, ResourceLocation location) {
        CompoundTag tag = nectar.getOrCreateTag();
        tag.putString(SOURCE_KEY, location.toString());
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        Player player = livingEntity instanceof Player ? (Player)livingEntity : null;
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));
        }
        if (livingEntity instanceof Player && !((Player)livingEntity).getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null && compoundTag.contains(EFFECTS_KEY, 9)) {
            HashMap<MobEffect, Integer> lockedEffectHashmap = new HashMap<>();
            int duration = 0;
            ListTag nbtList = compoundTag.getList(EFFECTS_KEY, 10);
            for (int i = 0; i < nbtList.size(); ++i) {
                MobEffect statusEffect;
                CompoundTag compoundTag2 = nbtList.getCompound(i);
                if ((statusEffect = Registry.MOB_EFFECT.get(ResourceLocation.tryParse(compoundTag2.getString(EFFECT_ID_KEY)))) == null) continue;
                if (compoundTag2.contains(EFFECT_DURATION_KEY, Tag.TAG_INT)) {
                    int compoundDuration = compoundTag2.getInt(EFFECT_DURATION_KEY);
                    if (compoundDuration > duration) {
                        duration = compoundDuration;
                    }
                    if (!lockedEffectHashmap.containsKey(statusEffect) || lockedEffectHashmap.containsKey(statusEffect) && duration > lockedEffectHashmap.get(statusEffect)) {
                        lockedEffectHashmap.put(statusEffect, compoundDuration);
                    }
                }
            }
            MobEffectInstance instance = new MobEffectInstance(BovineEffects.LOCKDOWN.get(), duration);
            lockedEffectHashmap.forEach((effect, integer) -> Services.COMPONENT.addLockdownMobEffect(livingEntity, effect, integer));
            Services.COMPONENT.syncLockdownMobEffects(livingEntity);
            livingEntity.addEffect(instance);
        }
        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
        }
        if (itemStack.isEmpty()) {
            return new ItemStack(Items.BOWL);
        }
        return itemStack;
    }

    public static ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getCowTypeFromStack(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(SOURCE_KEY, Tag.TAG_STRING)) {
            return BovineRegistryUtil.getConfiguredCowTypeFromKey(ResourceLocation.tryParse(tag.getString(SOURCE_KEY)), BovineCowTypes.FLOWER_COW_TYPE.get());
        }
        return null;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 32;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }
}
