package net.merchantpug.bovinesandbuttercups.client.api;

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

    public static StateDefinition<Block, BlockState> register(ResourceLocation resourceLocation, Block block) {
        if (REGISTRY.containsKey(resourceLocation)) {
            BovinesAndButtercups.LOG.error("BovineBlockstateTypeRegistry already contains entry for key '{}'.", resourceLocation);
            return REGISTRY.get(resourceLocation);
        }
        return REGISTRY.put(resourceLocation, block.getStateDefinition());
    }

    public static StateDefinition<Block, BlockState> get(ResourceLocation resource) {
        if (!REGISTRY.containsKey(resource)) {
            throw new NullPointerException("Could not get StateDefinition from key '" + resource + "'.");
        }
        return REGISTRY.get(resource);
    }

    public static Stream<StateDefinition<Block, BlockState>> stream() {
        return REGISTRY.values().stream();
    }
}
