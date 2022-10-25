package net.merchantpug.bovinesandbuttercups.item;

import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.mixin.client.ItemRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

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

    public static Optional<FlowerType> getFlowerTypeFromTag(LevelAccessor level, ItemStack stack) {
        if (stack.getTag() != null) {
            CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                FlowerType flowerType = BovineRegistryUtil.getFlowerTypeFromKey(level, ResourceLocation.tryParse(compound.getString("Type")));
                if (flowerType != FlowerType.MISSING) {
                    return Optional.of(flowerType);
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
                if (BovineRegistryUtil.isFlowerTypeInRegistry(level, resource)) {
                    FlowerType flowerType = BovineRegistryUtil.getFlowerTypeFromKey(level, resource);
                    return flowerType.getOrCreateNameTranslationKey(level);
                }
            }
        }
        return super.getName(stack);
    }

    public static MobEffect getSuspiciousStewEffect(LevelAccessor level, ItemStack customFlower) {
        if (customFlower.getTag() != null) {
            CompoundTag compound = customFlower.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                FlowerType flowerType = BovineRegistryUtil.getFlowerTypeFromKey(level, ResourceLocation.tryParse(compound.getString("Type")));
                if (flowerType.stewEffectInstance().isPresent()) {
                    return flowerType.stewEffectInstance().get().getEffect();
                }
            }
        }
        return MobEffects.REGENERATION;
    }

    public static int getSuspiciousStewDuration(LevelAccessor level, ItemStack customFlower) {
        if (customFlower.getTag() != null) {
            CompoundTag compound = customFlower.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                FlowerType flowerType = BovineRegistryUtil.getFlowerTypeFromKey(level, ResourceLocation.tryParse(compound.getString("Type")));
                if (flowerType.stewEffectInstance().isPresent()) {
                    return flowerType.stewEffectInstance().get().getDuration();
                }
            }
        }
        return 0;
    }

    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FlowerType.MISSING.itemModelLocation(), FlowerType.MISSING.itemModelVariant());

        if (CustomFlowerItem.getFlowerTypeFromTag(Minecraft.getInstance().level, stack).isPresent()) {
            modelResourceLocation = new ModelResourceLocation(CustomFlowerItem.getFlowerTypeFromTag(Minecraft.getInstance().level, stack).get().itemModelLocation(), CustomFlowerItem.getFlowerTypeFromTag(Minecraft.getInstance().level, stack).get().itemModelVariant());
        }

        BakedModel flowerModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        ((ItemRendererAccessor)Minecraft.getInstance().getItemRenderer()).bovinesandbuttercups$invokeRenderModelLists(flowerModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}