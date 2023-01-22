package net.merchantpug.bovinesandbuttercups.content.item;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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

    public static Optional<FlowerType> getFlowerTypeFromTag(ItemStack stack) {
        if (stack.getTag() != null) {
            CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                FlowerType flowerType = BovineRegistryUtil.getFlowerTypeFromKey(ResourceLocation.tryParse(compound.getString("Type")));
                if (!flowerType.equals(FlowerType.MISSING)) {
                    return Optional.of(flowerType);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag compound = stack.getOrCreateTag().getCompound("BlockEntityTag");
        if (compound.contains("Type")) {
            ResourceLocation resource = ResourceLocation.tryParse(compound.getString("Type"));
            if (resource != null && BovineRegistryUtil.isFlowerTypeInRegistry(resource)) {
                return getOrCreateNameTranslationKey(resource);
            }
        }
        return super.getName(stack);
    }

    private static Component getOrCreateNameTranslationKey(ResourceLocation location) {
        return Component.translatable("block." + location.getNamespace() + "." + location.getPath());
    }

    public static MobEffect getSuspiciousStewEffect(ItemStack customFlower) {
        if (customFlower.getTag() != null) {
            CompoundTag compound = customFlower.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                FlowerType flowerType = BovineRegistryUtil.getFlowerTypeFromKey(ResourceLocation.tryParse(compound.getString("Type")));
                if (flowerType.stewEffectInstance().isPresent()) {
                    return flowerType.stewEffectInstance().get().getEffect();
                }
            }
        }
        return MobEffects.REGENERATION;
    }

    public static int getSuspiciousStewDuration(ItemStack customFlower) {
        if (customFlower.getTag() != null) {
            CompoundTag compound = customFlower.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                FlowerType flowerType = BovineRegistryUtil.getFlowerTypeFromKey(ResourceLocation.tryParse(compound.getString("Type")));
                if (flowerType.stewEffectInstance().isPresent()) {
                    return flowerType.stewEffectInstance().get().getDuration();
                }
            }
        }
        return 0;
    }

    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
        if ((tab == CreativeModeTab.TAB_DECORATIONS || tab == CreativeModeTab.TAB_SEARCH)) {
            for (FlowerType type : BovineRegistryUtil.flowerTypeStream().filter(type -> !BovineRegistryUtil.getFlowerTypeKey(type).equals(BovinesAndButtercups.asResource("missing_flower"))).toList()) {
                ItemStack stack = new ItemStack(this);
                CompoundTag compound = new CompoundTag();
                compound.putString("Type", BovineRegistryUtil.getFlowerTypeKey(type).toString());
                stack.getOrCreateTag().put("BlockEntityTag", compound);
                list.add(stack);
            }
        }
    }
}
