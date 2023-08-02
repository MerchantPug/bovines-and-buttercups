package net.merchantpug.bovinesandbuttercups.client.integration.rei;

import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class BovineReiClientPlugin implements REIClientPlugin {
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
        BovineRegistryUtil.configuredCowTypeStream().filter(configuredCowType -> configuredCowType.configuration() instanceof FlowerCowConfiguration && configuredCowType != configuredCowType.cowType().getDefaultCowType().getSecond()).forEach(configuredCowType -> {
            ItemStack stack = new ItemStack(BovineItems.NECTAR_BOWL.get());
            ResourceLocation cowLocation = BovineRegistryUtil.getConfiguredCowTypeKey(configuredCowType);

            FlowerCowConfiguration fcc = ((FlowerCowConfiguration)configuredCowType.configuration());
            if (fcc.getNectarEffectInstance().isEmpty()) return;
            NectarBowlItem.saveMoobloomTypeKey(stack, cowLocation);
            NectarBowlItem.saveMobEffect(stack, fcc.getNectarEffectInstance().get().getEffect(), fcc.getNectarEffectInstance().get().getDuration());

            rule.show(() -> List.of(EntryStacks.of(stack)));
        });
        ItemStack defaultMissingFlower = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
        rule.hide(() -> List.of(EntryStacks.of(defaultMissingFlower)));
        BovineRegistryUtil.flowerTypeStream().filter(flowerType -> flowerType != FlowerType.MISSING).forEach(flowerType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
            ResourceLocation flowerTypeLocation = BovineRegistryUtil.getFlowerTypeKey(flowerType);

            CompoundTag tag = new CompoundTag();
            tag.putString("Type", flowerTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", tag);

            rule.show(() -> List.of(EntryStacks.of(stack)));
        });
        ItemStack defaultMissingMushroom = new ItemStack(BovineItems.CUSTOM_MUSHROOM.get());
        ItemStack defaultMissingMushroomBlock = new ItemStack(BovineItems.CUSTOM_MUSHROOM_BLOCK.get());
        rule.hide(() -> List.of(EntryStacks.of(defaultMissingMushroom)));
        rule.hide(() -> List.of(EntryStacks.of(defaultMissingMushroomBlock)));
        BovineRegistryUtil.mushroomTypeStream().filter(mushroomType -> mushroomType != MushroomType.MISSING).forEach(mushroomType -> {
            ItemStack stack = new ItemStack(BovineItems.CUSTOM_MUSHROOM.get());
            ItemStack blockStack = new ItemStack(BovineItems.CUSTOM_MUSHROOM_BLOCK.get());
            ResourceLocation mushroomTypeLocation = BovineRegistryUtil.getMushroomTypeKey(mushroomType);

            CompoundTag tag = new CompoundTag();
            tag.putString("Type", mushroomTypeLocation.toString());
            stack.addTagElement("BlockEntityTag", tag);
            blockStack.addTagElement("BlockEntityTag", tag);

            rule.show(() -> List.of(EntryStacks.of(stack)));
            rule.show(() -> List.of(EntryStacks.of(blockStack)));
        });
    }
}
