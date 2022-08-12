package com.github.merchantpug.bovinesandbuttercups.item;

import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.mixin.client.ItemRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

    @Nullable public static FlowerType getFlowerTypeFromTag(ItemStack stack) {
        if (stack.getTag() != null) {
            CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                return FlowerType.fromKey(compound.getString("Type"));
            }
        }
        return null;
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag compound = stack.getOrCreateTag().getCompound("BlockEntityTag");
        if (compound.contains("Type")) {
            FlowerType flowerType = FlowerType.fromKey(compound.getString("Type"));
            if (flowerType.isWithFlowerBlock()) {
                return flowerType.getOrCreateNameTranslationKey();
            }
        }
        return super.getName(stack);
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

    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FlowerType.MISSING.getItemModelLocation(), FlowerType.MISSING.getItemModelVariant());

        if (CustomFlowerItem.getFlowerTypeFromTag(stack) != null && FlowerTypeRegistry.contains(CustomFlowerItem.getFlowerTypeFromTag(stack).getResourceLocation()) && CustomFlowerItem.getFlowerTypeFromTag(stack).getItemModelLocation() != null && CustomFlowerItem.getFlowerTypeFromTag(stack).isWithFlowerBlock()) {
            modelResourceLocation = new ModelResourceLocation(CustomFlowerItem.getFlowerTypeFromTag(stack).getItemModelLocation(), CustomFlowerItem.getFlowerTypeFromTag(stack).getItemModelVariant());
        }

        BakedModel flowerModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        ((ItemRendererAccessor)Minecraft.getInstance().getItemRenderer()).bovinesandbuttercups$invokeRenderModelLists(flowerModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}
