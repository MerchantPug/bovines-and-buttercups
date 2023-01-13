package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class BovineItems {
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<NectarBowlItem> NECTAR_BOWL = register("nectar_bowl", () -> Services.REGISTRY.createNectarBowlItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> MOOBLOOM_SPAWN_EGG = register("moobloom_spawn_egg", () -> Services.REGISTRY.createSpawnEggItem(BovineEntityTypes.MOOBLOOM, 0xfad200, 0x437f34, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<BlockItem> BUTTERCUP = register("buttercup", () -> new BlockItem(BovineBlocks.BUTTERCUP.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<BlockItem> PINK_DAISY = register("pink_daisy", () -> new BlockItem(BovineBlocks.PINK_DAISY.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<BlockItem> LIMELIGHT = register("limelight", () -> new BlockItem(BovineBlocks.LIMELIGHT.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<BlockItem> BIRD_OF_PARADISE = register("bird_of_paradise", () -> new BlockItem(BovineBlocks.BIRD_OF_PARADISE.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<BlockItem> CHARGELILY = register("chargelily", () -> new BlockItem(BovineBlocks.CHARGELILY.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<BlockItem> HYACINTH = register("hyacinth", () -> new BlockItem(BovineBlocks.HYACINTH.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<BlockItem> SNOWDROP = register("snowdrop", () -> new BlockItem(BovineBlocks.SNOWDROP.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<BlockItem> TROPICAL_BLUE = register("tropical_blue", () -> new BlockItem(BovineBlocks.TROPICAL_BLUE.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistryObject<BlockItem> FREESIA = register("freesia", () -> new BlockItem(BovineBlocks.FREESIA.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    public static final RegistryObject<CustomFlowerItem> CUSTOM_FLOWER = register("custom_flower", Services.REGISTRY.createCustomFlowerItem());
    public static final RegistryObject<CustomMushroomItem> CUSTOM_MUSHROOM = register("custom_mushroom", Services.REGISTRY.createCustomMushroomItem());
    public static final RegistryObject<CustomHugeMushroomItem> CUSTOM_MUSHROOM_BLOCK = register("custom_mushroom_block", Services.REGISTRY.createCustomHugeMushroomItem());

    public static void register() {

    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}
