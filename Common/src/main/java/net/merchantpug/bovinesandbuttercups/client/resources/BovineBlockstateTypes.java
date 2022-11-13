package net.merchantpug.bovinesandbuttercups.client.resources;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.client.api.BovineBlockstateTypeRegistry;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BovineBlockstateTypes {
    public static final StateDefinition<Block, BlockState> GENERIC = BovineBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("generic"), BovineBlocks.CUSTOM_FLOWER.get());
    public static final StateDefinition<Block, BlockState> MUSHROOM_BLOCK = BovineBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("mushroom_block"), BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get());

    public static void init() {

    }
}
