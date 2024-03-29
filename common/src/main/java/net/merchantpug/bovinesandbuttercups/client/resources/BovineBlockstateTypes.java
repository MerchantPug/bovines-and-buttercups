package net.merchantpug.bovinesandbuttercups.client.resources;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineBlockstateTypeRegistry;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BovineBlockstateTypes {
    public static final StateDefinition<Block, BlockState> EMPTY_STATE = (new StateDefinition.Builder<Block, BlockState>(Blocks.AIR)).create(Block::defaultBlockState, BlockState::new);

    public static final StateDefinition<Block, BlockState> GENERIC = BovineBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("generic"), EMPTY_STATE);
    public static final StateDefinition<Block, BlockState> FLOWER = BovineBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("flower"), BovineBlocks.CUSTOM_FLOWER.get().getStateDefinition());
    public static final StateDefinition<Block, BlockState> MUSHROOM = BovineBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("mushroom"), BovineBlocks.CUSTOM_MUSHROOM.get().getStateDefinition());
    public static final StateDefinition<Block, BlockState> POTTED_FLOWER = BovineBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("potted_flower"), BovineBlocks.POTTED_CUSTOM_FLOWER.get().getStateDefinition());
    public static final StateDefinition<Block, BlockState> POTTED_MUSHROOM = BovineBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("potted_mushroom"), BovineBlocks.POTTED_CUSTOM_MUSHROOM.get().getStateDefinition());
    public static final StateDefinition<Block, BlockState> MUSHROOM_BLOCK = BovineBlockstateTypeRegistry.register(BovinesAndButtercups.asResource("mushroom_block"), BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get().getStateDefinition());

    public static void init() {

    }
}
