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
        CompoundTag compound = new CompoundTag();
        compound.putString("Type", "bovinesandbuttercups:missing");
        stack.getOrCreateTag().put("BlockEntityTag", compound);
        return stack;
    }

    @Nullable public static FlowerType getFlowerItemFromTag(ItemStack customFlower) {
        if (customFlower.getTag() != null) {
            CompoundTag compound = customFlower.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                return FlowerType.fromKey(compound.getString("Type"));
            }
        }
        return null;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        CompoundTag compound = stack.getOrCreateTag().getCompound("BlockEntityTag");
        if (compound.contains("Type")) {
            FlowerType flowerType = FlowerType.fromKey(compound.getString("Type"));
            if (flowerType.isWithFlowerBlock() && flowerType.getName() != null) {
                return flowerType.getName();
            }
        }
        return this.getDescriptionId();
    }

    public static MobEffect getSuspiciousStewEffect(ItemStack customFlower) {
        if (customFlower.getTag() != null) {
            CompoundTag compound = customFlower.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                FlowerType flowerType = FlowerType.fromKey(compound.getString("Type"));
                if (flowerType.getStewEffectInstance() != null) {
                    return flowerType.getStewEffectInstance().getEffect();
                }
            }
        }
        return MobEffects.REGENERATION;
    }

    public static int getSuspiciousStewDuration(ItemStack customFlower) {
        if (customFlower.getTag() != null) {
            CompoundTag compound = customFlower.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                FlowerType flowerType = FlowerType.fromKey(compound.getString("Type"));
                if (flowerType.getStewEffectInstance() != null) {
                    return flowerType.getStewEffectInstance().getDuration();
                }
            }
        }
        return 0;
    }
}
