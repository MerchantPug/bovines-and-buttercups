package net.merchantpug.bovinesandbuttercups.util;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CreativeTabHelper {
    public static List<ItemStack> getCustomFlowersForCreativeTab() {
        return FlowerTypeRegistry.valueStream().filter(flowerType -> !flowerType.equals(FlowerType.MISSING)).map(flowerType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());

            ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

            CompoundTag tag = new CompoundTag();
            tag.putString("Type", flowerTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", tag);

            return stack;
        }).toList();
    }

    public static List<ItemStack> getCustomMushroomsForCreativeTab() {
        return MushroomTypeRegistry.valueStream().filter(mushroomType -> !mushroomType.equals(MushroomType.MISSING)).map(mushroomType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_MUSHROOM.get());

            ResourceLocation mushroomTypeLocation = BovineRegistryUtil.getMushroomTypeKey(mushroomType);

            CompoundTag tag = new CompoundTag();
            tag.putString("Type", mushroomTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", tag);

            return stack;
        }).toList();
    }

    public static List<ItemStack> getCustomMushroomBlocksForCreativeTab() {
        return MushroomTypeRegistry.valueStream().filter(mushroomType -> !mushroomType.equals(MushroomType.MISSING)).map(mushroomType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_MUSHROOM_BLOCK.get());

            ResourceLocation mushroomTypeLocation = BovineRegistryUtil.getMushroomTypeKey(mushroomType);

            CompoundTag tag = new CompoundTag();
            tag.putString("Type", mushroomTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", tag);

            return stack;
        }).toList();
    }

    public static List<ItemStack> getNectarBowlsForCreativeTab() {
        return ConfiguredCowTypeRegistry.asStream().filter(entry -> entry.getValue().getConfiguration() instanceof FlowerCowConfiguration flowerCowConfiguration && flowerCowConfiguration.getNectarEffectInstance().isPresent()).map(entry -> {
            FlowerCowConfiguration config = (FlowerCowConfiguration) entry.getValue().getConfiguration();
            ItemStack nectar = new ItemStack(BovineItems.NECTAR_BOWL.get());
            if (config.getNectarEffectInstance().isPresent()) {
                NectarBowlItem.saveMoobloomTypeKey(nectar, entry.getKey());
                NectarBowlItem.saveMobEffect(nectar, config.getNectarEffectInstance().get().getEffect(), config.getNectarEffectInstance().get().getDuration());
            }
            return nectar;
        }).toList();
    }
}
