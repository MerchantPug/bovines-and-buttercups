package net.merchantpug.bovinesandbuttercups.integration.rei;

import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class BovineReiPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BovineRegistryUtil.flowerTypeStream().forEach(flowerType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
            ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("Type", flowerTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", compoundTag);

            if (flowerType.dyeCraftResult().isPresent()) {
                registry.add(DefaultCustomShapelessDisplay.simple(List.of(EntryIngredients.of(stack)), List.of(EntryIngredients.of(flowerType.dyeCraftResult().get().copy())), Optional.of(BovinesAndButtercups.asResource("custom_flower_dye/" + flowerTypeLocation.getNamespace() + "/" + flowerTypeLocation.getPath()))));
            }
        });
    }

    @Override
    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        ItemStack defaultMissingFlower = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
        rule.hide(() -> List.of(EntryStacks.of(defaultMissingFlower)));
        BovineRegistryUtil.flowerTypeStream().forEach(flowerType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
            ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("Type", flowerTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", compoundTag);

            rule.show(() -> List.of(EntryStacks.of(stack)));
        });
        BovineRegistryUtil.mushroomTypeStream().forEach(mushroomType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_MUSHROOM.get());
            ItemStack blockStack = new ItemStack(BovineItems.CUSTOM_MUSHROOM_BLOCK.get());
            ResourceLocation mushroomTypeLocation = BovineRegistryUtil.getMushroomTypeKey(mushroomType);

            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("Type", mushroomTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", compoundTag);
            blockStack.addTagElement("BlockEntityTag", compoundTag);

            rule.show(() -> List.of(EntryStacks.of(stack)));
            rule.show(() -> List.of(EntryStacks.of(blockStack)));
        });
    }
}
