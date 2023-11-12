package net.merchantpug.bovinesandbuttercups.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.neoforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PottedBlockMapUtil {
    private static final Map<Block, Block> POTTED_CONTENT_MAP = new HashMap<>();

    public static Map<Block, Block> getPottedContentMap() {
        for (Map.Entry<ResourceLocation, Supplier<? extends Block>> forgeEntry : ((FlowerPotBlock) Blocks.FLOWER_POT).getFullPotsView().entrySet()) {
            POTTED_CONTENT_MAP.computeIfAbsent(ForgeRegistries.BLOCKS.getValue(forgeEntry.getKey()), key -> forgeEntry.getValue().get());
        }
        return POTTED_CONTENT_MAP;
    }
}
