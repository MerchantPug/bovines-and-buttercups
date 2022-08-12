package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.item.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BovineItemsForge {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final RegistryObject<CustomFlowerItem> CUSTOM_FLOWER = ITEMS.register("custom_flower", () -> new CustomFlowerItemForge(BovineBlocks.CUSTOM_FLOWER.get(), new Item.Properties()));
    public static final RegistryObject<CustomMushroomItem> CUSTOM_MUSHROOM = ITEMS.register("custom_mushroom", () -> new CustomMushroomItemForge(BovineBlocks.CUSTOM_MUSHROOM.get(), new Item.Properties()));
    public static final RegistryObject<CustomHugeMushroomItem> CUSTOM_MUSHROOM_BLOCK = ITEMS.register("custom_mushroom_block", () -> new CustomHugeMushroomItemForge(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get(), new Item.Properties()));

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
