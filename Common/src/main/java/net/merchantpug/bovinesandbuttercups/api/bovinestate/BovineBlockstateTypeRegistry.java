package net.merchantpug.bovinesandbuttercups.api.bovinestate;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BovineBlockstateTypeRegistry {
    private static final Map<ResourceLocation, StateDefinition<Block, BlockState>> REGISTRY = new HashMap<>();

    public static StateDefinition<Block, BlockState> register(ResourceLocation resource, Block block) {
        if (REGISTRY.containsKey(resource)) {
            BovinesAndButtercups.LOG.error("BovineBlockstateTypeRegistry already contains entry for key '{}'.", resource);
            return REGISTRY.get(resource);
        }
        return REGISTRY.put(resource, block.getStateDefinition());
    }

    public static StateDefinition<Block, BlockState> get(ResourceLocation resource) {
        if (!REGISTRY.containsKey(resource)) {
            BovinesAndButtercups.LOG.error("Could not get StateDefinition from key '{}'.", resource);
            return null;
        }
        return REGISTRY.get(resource);
    }

    public static Stream<StateDefinition<Block, BlockState>> stream() {
        return REGISTRY.values().stream();
    }
}
