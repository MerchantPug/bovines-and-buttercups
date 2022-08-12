package com.github.merchantpug.bovinesandbuttercups.item;

import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomType;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomTypeRegistry;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public class CustomHugeMushroomItem extends BlockItem {
    public CustomHugeMushroomItem(Block block, Properties properties) {
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

    @Nullable public static MushroomType getMushroomTypeFromTag(ItemStack stack) {
        if (stack.getTag() != null) {
            CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                return MushroomType.fromKey(compound.getString("Type"));
            }
        }
        return null;
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag compound = stack.getOrCreateTag().getCompound("BlockEntityTag");
        if (compound.contains("Type")) {
            MushroomType mushroomType = MushroomType.fromKey(compound.getString("Type"));
            if (mushroomType.isWithMushroomBlocks()) {
                return mushroomType.getOrCreateHugeBlockNameTranslationKey();
            }
        }
        return super.getName(stack);
    }


    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MushroomType.MISSING.getHugeBlockItemModelLocation(), MushroomType.MISSING.getHugeBlockItemModelVariant());

        if (CustomHugeMushroomItem.getMushroomTypeFromTag(stack) != null && MushroomTypeRegistry.contains(CustomHugeMushroomItem.getMushroomTypeFromTag(stack).getResourceLocation()) && CustomHugeMushroomItem.getMushroomTypeFromTag(stack).getHugeBlockItemModelLocation() != null && CustomHugeMushroomItem.getMushroomTypeFromTag(stack).isWithMushroomBlocks()) {
            modelResourceLocation = new ModelResourceLocation(CustomHugeMushroomItem.getMushroomTypeFromTag(stack).getHugeBlockItemModelLocation(), CustomHugeMushroomItem.getMushroomTypeFromTag(stack).getHugeBlockItemModelVariant());
        }

        BakedModel mushroomModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        ((ItemRendererAccessor)Minecraft.getInstance().getItemRenderer()).bovinesandbuttercups$invokeRenderModelLists(mushroomModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}
