package net.merchantpug.bovinesandbuttercups.content.item;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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

    public static Optional<MushroomType> getMushroomTypeFromTag(ItemStack stack) {
        if (stack.getTag() != null) {
            CompoundTag compound = stack.getTag().getCompound("BlockEntityTag");
            if (compound.contains("Type")) {
                MushroomType mushroomType = BovineRegistryUtil.getMushroomTypeFromKey(ResourceLocation.tryParse(compound.getString("Type")));
                if (mushroomType != MushroomType.MISSING) {
                    return Optional.of(mushroomType);
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
            if (resource != null && BovineRegistryUtil.isMushroomTypeInRegistry(resource)) {
                return getOrCreateNameTranslationKey(resource);
            }
        }
        return super.getName(stack);
    }

    private static Component getOrCreateNameTranslationKey(ResourceLocation location) {
        return Component.translatable("block." + location.getNamespace() + "." + location.getPath());
    }

    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
        if ((tab == CreativeModeTab.TAB_DECORATIONS || tab == CreativeModeTab.TAB_SEARCH)) {
            for (MushroomType type : BovineRegistryUtil.mushroomTypeStream().filter(type -> !BovineRegistryUtil.getMushroomTypeKey(type).equals(BovinesAndButtercups.asResource("missing_mushroom"))).toList()) {
                ItemStack stack = new ItemStack(this);
                CompoundTag compound = new CompoundTag();
                compound.putString("Type", BovineRegistryUtil.getMushroomTypeKey(type).toString());
                stack.getOrCreateTag().put("BlockEntityTag", compound);

                list.add(stack);
            }
        }
    }
}
