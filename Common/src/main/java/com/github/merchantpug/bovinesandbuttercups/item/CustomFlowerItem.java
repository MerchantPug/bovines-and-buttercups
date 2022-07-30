package com.github.merchantpug.bovinesandbuttercups.item;

import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public class CustomFlowerItem extends BlockItem {
    public CustomFlowerItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putString("BlockEntityTag.Type", "bovinesandbuttercups:missing");
        return stack;
    }

    @Nullable public static FlowerType getFlowerItemFromTag(ItemStack customFlower) {
        if (customFlower.getTag() == null) return null;
        CompoundTag compoundTag = customFlower.getTag().getCompound("BlockEntityTag");
        if (!compoundTag.contains("Type")) return null;
        return FlowerType.fromKey(compoundTag.getString("Type"));
    }

    @Nullable public MobEffect getSuspiciousStewEffect(ItemStack customFlower) {
        if (customFlower.getTag() == null) return MobEffects.REGENERATION;
        CompoundTag compoundTag = customFlower.getTag().getCompound("BlockEntityTag");
        if (!compoundTag.contains("Type")) return null;
        FlowerType flowerType = FlowerType.fromKey(compoundTag.getString("Type"));
        if (flowerType.getStewEffectInstance() == null) return MobEffects.REGENERATION;

        return flowerType.getStewEffectInstance().getEffect();
    }

    public int getSuspiciousStewDuration(ItemStack customFlower) {
        if (customFlower.getTag() == null) return 0;
        CompoundTag compoundTag = customFlower.getTag().getCompound("BlockEntityTag");
        if (!compoundTag.contains("Type")) return 0;
        FlowerType flowerType = FlowerType.fromKey(compoundTag.getString("Type"));
        if (flowerType.getStewEffectInstance() == null) return 0;

        return flowerType.getStewEffectInstance().getDuration();
    }
}
