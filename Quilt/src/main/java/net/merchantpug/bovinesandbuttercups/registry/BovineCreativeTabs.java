package net.merchantpug.bovinesandbuttercups.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.util.CreativeTabHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.stream.Stream;

public class BovineCreativeTabs {
    public static CreativeModeTab BOVINES_AND_BUTTERCUPS = FabricItemGroup.builder(BovinesAndButtercups.asResource("items"))
            .title(Component.translatable("bovinesandbuttercups.itemGroup.items"))
            .icon(() -> new ItemStack(BovineItems.BUTTERCUP.get()))
            .displayItems((featureFlagSet, output, bl) -> {
                output.accept(BovineItems.MOOBLOOM_SPAWN_EGG.get());
                output.accept(BovineItems.FREESIA.get());
                output.accept(BovineItems.BIRD_OF_PARADISE.get());
                output.accept(BovineItems.BUTTERCUP.get());
                output.accept(BovineItems.LIMELIGHT.get());
                output.accept(BovineItems.CHARGELILY.get());
                output.accept(BovineItems.TROPICAL_BLUE.get());
                output.accept(BovineItems.HYACINTH.get());
                output.accept(BovineItems.PINK_DAISY.get());
                output.accept(BovineItems.SNOWDROP.get());
                output.acceptAll(CreativeTabHelper.getCustomFlowersForCreativeTab());
                output.acceptAll(CreativeTabHelper.getCustomMushroomsForCreativeTab());
                output.acceptAll(CreativeTabHelper.getCustomMushroomBlocksForCreativeTab());
                output.acceptAll(CreativeTabHelper.getNectarBowlsForCreativeTab());
            })
            .build();

    public static void register() {
        registerCreativeTabEntries();
    }

    public static void registerCreativeTabEntries() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.LILY_OF_THE_VALLEY, Stream.of(
                    BovineItems.FREESIA.get(),
                    BovineItems.BIRD_OF_PARADISE.get(),
                    BovineItems.BUTTERCUP.get(),
                    BovineItems.LIMELIGHT.get(),
                    BovineItems.CHARGELILY.get(),
                    BovineItems.TROPICAL_BLUE.get(),
                    BovineItems.HYACINTH.get(),
                    BovineItems.PINK_DAISY.get(),
                    BovineItems.SNOWDROP.get()).map(ItemStack::new).toList());
            entries.addAfter(BovineItems.SNOWDROP.get(), CreativeTabHelper.getCustomFlowersForCreativeTab());
            entries.addAfter(Items.RED_MUSHROOM, CreativeTabHelper.getCustomMushroomsForCreativeTab());
            entries.addAfter(Items.RED_MUSHROOM_BLOCK, CreativeTabHelper.getCustomMushroomBlocksForCreativeTab());
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries -> {
            entries.addAfter(Items.MILK_BUCKET, CreativeTabHelper.getNectarBowlsForCreativeTab());
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> {
            entries.accept(BovineItems.MOOBLOOM_SPAWN_EGG.get());
        });
    }
}
