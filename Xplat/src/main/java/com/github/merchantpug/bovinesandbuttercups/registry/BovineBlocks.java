package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.github.merchantpug.bovinesandbuttercups.block.*;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class BovineBlocks {
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registry.BLOCK, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<Block> BUTTERCUP = register("buttercup", () -> new FlowerBlock(MobEffects.POISON, 12, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> PINK_DAISY = register("pink_daisy", () -> new FlowerBlock(MobEffects.MOVEMENT_SPEED, 4, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> LIMELIGHT = register("limelight", () -> new FlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> BIRD_OF_PARADISE = register("bird_of_paradise", () -> new FlowerBlock(MobEffects.NIGHT_VISION, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> CHARGELILY = register("chargelily", () -> new FlowerBlock(MobEffects.MOVEMENT_SPEED, 4, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> HYACINTH = register("hyacinth", () -> new FlowerBlock(MobEffects.DAMAGE_BOOST, 2, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> SNOWDROP = register("snowdrop", () -> new SnowdropFlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> SNAPDRAGON = register("snapdragon", () -> new FlowerBlock(MobEffects.DAMAGE_BOOST, 2, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static final RegistryObject<Block> POTTED_BUTTERCUP = register("potted_buttercup", () -> new FlowerPotBlock(BUTTERCUP.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> POTTED_PINK_DAISY = register("potted_pink_daisy", () -> new FlowerPotBlock(PINK_DAISY.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> POTTED_LIMELIGHT = register("potted_limelight", () -> new FlowerPotBlock(LIMELIGHT.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> POTTED_BIRD_OF_PARADISE = register("potted_bird_of_paradise", () -> new FlowerPotBlock(BIRD_OF_PARADISE.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> POTTED_CHARGELILY = register("potted_chargelily", () -> new FlowerPotBlock(CHARGELILY.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> POTTED_HYACINTH = register("potted_hyacinth", () -> new FlowerPotBlock(HYACINTH.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> POTTED_SNOWDROP = register("potted_snowdrop", () -> new FlowerPotBlock(SNOWDROP.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<Block> POTTED_SNAPDRAGON = register("potted_snapdragon", () -> new FlowerPotBlock(SNAPDRAGON.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

    public static final RegistryObject<Block> CUSTOM_FLOWER = register("custom_flower", () -> new CustomFlowerBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<Block> POTTED_CUSTOM_FLOWER = register("potted_custom_flower", () -> new CustomFlowerPotBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

    public static final RegistryObject<Block> CUSTOM_MUSHROOM = register("custom_mushroom", () -> new CustomMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel((value) -> 1)));
    public static final RegistryObject<Block> CUSTOM_MUSHROOM_BLOCK = register("custom_mushroom_block", () -> new CustomHugeMushroomBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.2F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> POTTED_CUSTOM_MUSHROOM = register("potted_custom_mushroom", () -> new CustomMushroomPotBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

    public static void init() {

    }

    public static RegistryObject<Block> register(String blockName, Supplier<Block> block) {
        return BLOCKS.register(blockName, block);
    }
}
