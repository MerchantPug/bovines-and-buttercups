package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.item.NectarBowlItem;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class BovineItems {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM, Constants.MOD_ID);

    public static final RegistryObject<Item> NECTAR_BOWL = register("nectar_bowl", () -> new NectarBowlItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MOOBLOOM_SPAWN_EGG = register("moobloom_spawn_egg", () -> Services.PLATFORM.createSpawnEggItem(BovineEntityTypes.MOOBLOOM, 0xfad200, 0x437f34, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> BUTTERCUP = register("buttercup", () -> new BlockItem(BovineBlocks.BUTTERCUP.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> PINK_DAISY = register("pink_daisy", () -> new BlockItem(BovineBlocks.PINK_DAISY.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> LIMELIGHT = register("limelight", () -> new BlockItem(BovineBlocks.LIMELIGHT.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> BIRD_OF_PARADISE = register("bird_of_paradise", () -> new BlockItem(BovineBlocks.BIRD_OF_PARADISE.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<Item> CHARGELILY = register("chargelily", () -> new BlockItem(BovineBlocks.CHARGELILY.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    public static void init() {

    }

    public static RegistryObject<Item> register(String itemName, Supplier<Item> item) {
        return ITEMS.register(itemName, item);
    }
}
