package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class BovineItems {
    private static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, BovinesAndButtercups.MOD_ID);

    public static final Supplier<NectarBowlItem> NECTAR_BOWL = register("nectar_bowl", () -> Services.REGISTRY.createNectarBowlItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> MOOBLOOM_SPAWN_EGG = register("moobloom_spawn_egg", () -> Services.REGISTRY.createSpawnEggItem(BovineEntityTypes.MOOBLOOM, 0xfad200, 0x437f34, new Item.Properties()));

    public static final Supplier<BlockItem> BUTTERCUP = register("buttercup", () -> new BlockItem(BovineBlocks.BUTTERCUP.get(), new Item.Properties()));
    public static final Supplier<BlockItem> PINK_DAISY = register("pink_daisy", () -> new BlockItem(BovineBlocks.PINK_DAISY.get(), new Item.Properties()));
    public static final Supplier<BlockItem> LIMELIGHT = register("limelight", () -> new BlockItem(BovineBlocks.LIMELIGHT.get(), new Item.Properties()));
    public static final Supplier<BlockItem> BIRD_OF_PARADISE = register("bird_of_paradise", () -> new BlockItem(BovineBlocks.BIRD_OF_PARADISE.get(), new Item.Properties()));
    public static final Supplier<BlockItem> CHARGELILY = register("chargelily", () -> new BlockItem(BovineBlocks.CHARGELILY.get(), new Item.Properties()));
    public static final Supplier<BlockItem> HYACINTH = register("hyacinth", () -> new BlockItem(BovineBlocks.HYACINTH.get(), new Item.Properties()));
    public static final Supplier<BlockItem> SNOWDROP = register("snowdrop", () -> new BlockItem(BovineBlocks.SNOWDROP.get(), new Item.Properties()));
    public static final Supplier<BlockItem> TROPICAL_BLUE = register("tropical_blue", () -> new BlockItem(BovineBlocks.TROPICAL_BLUE.get(), new Item.Properties()));
    public static final Supplier<BlockItem> FREESIA = register("freesia", () -> new BlockItem(BovineBlocks.FREESIA.get(), new Item.Properties()));

    public static final Supplier<CustomFlowerItem> CUSTOM_FLOWER = register("custom_flower", Services.REGISTRY.createCustomFlowerItem());
    public static final Supplier<CustomMushroomItem> CUSTOM_MUSHROOM = register("custom_mushroom", Services.REGISTRY.createCustomMushroomItem());
    public static final Supplier<CustomHugeMushroomItem> CUSTOM_MUSHROOM_BLOCK = register("custom_mushroom_block", Services.REGISTRY.createCustomHugeMushroomItem());

    public static void register() {

    }

    private static <T extends Item> Supplier<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}
