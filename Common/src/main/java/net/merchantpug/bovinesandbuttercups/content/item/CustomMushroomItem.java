package net.merchantpug.bovinesandbuttercups.content.item;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
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
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class CustomMushroomItem extends BlockItem {
    public CustomMushroomItem(Block block, Properties properties) {
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
                MushroomType mushroomType = BovineRegistryUtil.getMushroomTypeFromKey(level, ResourceLocation.tryParse(compound.getString("Type")));
                if (mushroomType != MushroomType.MISSING) {
                    return Optional.of(mushroomType);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Component getName(ItemStack stack) {
        Level level = ((ItemStackAccess)(Object)stack).bovinesandbuttercups$getLevel();
        if (level != null) {
            CompoundTag compound = stack.getOrCreateTag().getCompound("BlockEntityTag");
            ResourceLocation resource = ResourceLocation.tryParse(compound.getString("Type"));
            if (compound.contains("Type")) {
                if (BovineRegistryUtil.isMushroomTypeInRegistry(level, resource)) {
                    MushroomType mushroomType = BovineRegistryUtil.getMushroomTypeFromKey(level, resource);
                    return mushroomType.getOrCreateNameTranslationKey(level);
                }
            }
        }
        return super.getName(stack);
    }

    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
        Level level = Minecraft.getInstance().level;
        if ((tab == CreativeModeTab.TAB_DECORATIONS || tab == CreativeModeTab.TAB_SEARCH) && level != null) {
            for (MushroomType type : BovineRegistryUtil.mushroomTypeStream(level).filter(type -> !BovineRegistryUtil.getMushroomTypeKey(level, type).equals(BovinesAndButtercups.asResource("missing"))).toList()) {
                ItemStack stack = new ItemStack(this);
                CompoundTag compound = new CompoundTag();
                compound.putString("Type", BovineRegistryUtil.getMushroomTypeKey(level, type).toString());
                stack.getOrCreateTag().put("BlockEntityTag", compound);
                ((ItemStackAccess)(Object)stack).bovinesandbuttercups$setLevel(level);

                list.add(stack);
            }
        }
    }

    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MushroomType.MISSING.mushroomModel(), "inventory");

        if (CustomMushroomItem.getMushroomTypeFromTag(Minecraft.getInstance().level, stack).isPresent()) {
            modelResourceLocation = new ModelResourceLocation(CustomMushroomItem.getMushroomTypeFromTag(Minecraft.getInstance().level, stack).get().mushroomModel(), "inventory");
        }

        BakedModel mushroomModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        ((ItemRendererAccessor)Minecraft.getInstance().getItemRenderer()).bovinesandbuttercups$invokeRenderModelLists(mushroomModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}
