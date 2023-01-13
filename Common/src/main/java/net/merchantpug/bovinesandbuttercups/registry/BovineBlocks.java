package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.block.*;
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
    private static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registry.BLOCK, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<MoobloomFlowerBlock> BUTTERCUP = register("buttercup", () -> new MoobloomFlowerBlock(MobEffects.POISON, 12, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<MoobloomFlowerBlock> PINK_DAISY = register("pink_daisy", () -> new MoobloomFlowerBlock(MobEffects.MOVEMENT_SPEED, 4, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<MoobloomFlowerBlock> LIMELIGHT = register("limelight", () -> new MoobloomFlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<MoobloomFlowerBlock> BIRD_OF_PARADISE = register("bird_of_paradise", () -> new MoobloomFlowerBlock(MobEffects.NIGHT_VISION, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<MoobloomFlowerBlock> CHARGELILY = register("chargelily", () -> new MoobloomFlowerBlock(MobEffects.MOVEMENT_SPEED, 4, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<MoobloomFlowerBlock> HYACINTH = register("hyacinth", () -> new MoobloomFlowerBlock(MobEffects.DAMAGE_BOOST, 2, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<SnowdropFlowerBlock> SNOWDROP = register("snowdrop", () -> new SnowdropFlowerBlock(MobEffects.MOVEMENT_SLOWDOWN, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<MoobloomFlowerBlock> TROPICAL_BLUE = register("tropical_blue", () -> new MoobloomFlowerBlock(MobEffects.DAMAGE_BOOST, 2, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<MoobloomFlowerBlock> FREESIA = register("freesia", () -> new MoobloomFlowerBlock(MobEffects.WATER_BREATHING, 8, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static final RegistryObject<FlowerPotBlock> POTTED_BUTTERCUP = register("potted_buttercup", () -> new FlowerPotBlock(BUTTERCUP.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<FlowerPotBlock> POTTED_PINK_DAISY = register("potted_pink_daisy", () -> new FlowerPotBlock(PINK_DAISY.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<FlowerPotBlock> POTTED_LIMELIGHT = register("potted_limelight", () -> new FlowerPotBlock(LIMELIGHT.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<FlowerPotBlock> POTTED_BIRD_OF_PARADISE = register("potted_bird_of_paradise", () -> new FlowerPotBlock(BIRD_OF_PARADISE.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<FlowerPotBlock> POTTED_CHARGELILY = register("potted_chargelily", () -> new FlowerPotBlock(CHARGELILY.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<FlowerPotBlock> POTTED_HYACINTH = register("potted_hyacinth", () -> new FlowerPotBlock(HYACINTH.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<FlowerPotBlock> POTTED_SNOWDROP = register("potted_snowdrop", () -> new FlowerPotBlock(SNOWDROP.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<FlowerPotBlock> POTTED_TROPICAL_BLUE = register("potted_tropical_blue", () -> new FlowerPotBlock(TROPICAL_BLUE.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static final RegistryObject<FlowerPotBlock> POTTED_FREESIA = register("potted_freesia", () -> new FlowerPotBlock(FREESIA.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

    public static final RegistryObject<CustomFlowerBlock> CUSTOM_FLOWER = register("custom_flower", () -> new CustomFlowerBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final RegistryObject<CustomFlowerPotBlock> POTTED_CUSTOM_FLOWER = register("potted_custom_flower", () -> new CustomFlowerPotBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

    public static final RegistryObject<CustomMushroomBlock> CUSTOM_MUSHROOM = register("custom_mushroom", () -> new CustomMushroomBlock(BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel((value) -> 1)));
    public static final RegistryObject<CustomHugeMushroomBlock> CUSTOM_MUSHROOM_BLOCK = register("custom_mushroom_block", () -> new CustomHugeMushroomBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.2F).sound(SoundType.WOOD)));
    public static final RegistryObject<CustomMushroomPotBlock> POTTED_CUSTOM_MUSHROOM = register("potted_custom_mushroom", () -> new CustomMushroomPotBlock(BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

    public static void register() {

    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }
}
