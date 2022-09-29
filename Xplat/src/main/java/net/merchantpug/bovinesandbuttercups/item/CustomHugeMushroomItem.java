package net.merchantpug.bovinesandbuttercups.item;

import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.mixin.client.ItemRendererAccessor;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

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

    public static Optional<MushroomType> getMushroomTypeFromTag(LevelAccessor level, ItemStack stack) {
        if (stack.getTag() != null) {
            CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                MushroomType mushroomType = MushroomType.fromKey(level, ResourceLocation.tryParse(compound.getString("Type")));
                if (mushroomType != MushroomType.MISSING) {
                    return Optional.of(mushroomType);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Component getName(ItemStack stack) {
        if (((ItemStackAccess)(Object)stack).bovinesandbuttercups$getLevel() != null) {
            CompoundTag compound = stack.getOrCreateTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                MushroomType mushroomType = MushroomType.fromKey(((ItemStackAccess)(Object)stack).bovinesandbuttercups$getLevel(), ResourceLocation.tryParse(compound.getString("Type")));
                if (mushroomType.withMushroomBlocks()) {
                    return mushroomType.getOrCreateHugeNameTranslationKey();
                }
            }
        }
        return super.getName(stack);
    }


    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MushroomType.MISSING.hugeBlockItemModelLocation().get(), MushroomType.MISSING.hugeBlockItemModelVariant());

        if (CustomHugeMushroomItem.getMushroomTypeFromTag(Minecraft.getInstance().level, stack).isPresent() && CustomHugeMushroomItem.getMushroomTypeFromTag(Minecraft.getInstance().level, stack).get().hugeBlockItemModelLocation().isPresent() && CustomHugeMushroomItem.getMushroomTypeFromTag(Minecraft.getInstance().level, stack).get().withMushroomBlocks()) {
            modelResourceLocation = new ModelResourceLocation(CustomHugeMushroomItem.getMushroomTypeFromTag(Minecraft.getInstance().level, stack).get().hugeBlockItemModelLocation().get(), CustomHugeMushroomItem.getMushroomTypeFromTag(Minecraft.getInstance().level, stack).get().hugeBlockItemModelVariant());
        }

        BakedModel mushroomModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        ((ItemRendererAccessor)Minecraft.getInstance().getItemRenderer()).bovinesandbuttercups$invokeRenderModelLists(mushroomModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}
