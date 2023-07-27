package net.merchantpug.bovinesandbuttercups.api.bovinestate;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.mojang.datafixers.util.Pair;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.*;

public class BovineStatesAssociationRegistry {
    private static final Table<ResourceLocation, StateDefinition<Block, BlockState>, ResourceLocation> BLOCK_MODEL_REGISTRY = Tables.synchronizedTable(HashBasedTable.create());
    private static final Map<ResourceLocation, ResourceLocation> ITEM_MODEL_REGISTRY = new HashMap<>();
    private static final Set<Pair<ResourceLocation, StateDefinition<Block, BlockState>>> WARNED_BLOCK_KEYS = new HashSet<>();
    private static final Set<ResourceLocation> WARNED_ITEM_KEYS = new HashSet<>();

    public static void clear() {
        BLOCK_MODEL_REGISTRY.clear();
        ITEM_MODEL_REGISTRY.clear();
    }

    public static ResourceLocation registerItem(ResourceLocation key, ResourceLocation modelLocation) {
        if (ITEM_MODEL_REGISTRY.containsKey(key)) {
            BovinesAndButtercups.LOG.warn("Attempted to register item model which already contains a key: {}.", key);
            return ITEM_MODEL_REGISTRY.get(key);
        }
        return ITEM_MODEL_REGISTRY.put(key, modelLocation);
    }

    public static ResourceLocation registerBlock(ResourceLocation resource, StateDefinition<Block, BlockState> block, ResourceLocation modelLocation) {
        if (BLOCK_MODEL_REGISTRY.contains(resource, block)) {
            return BLOCK_MODEL_REGISTRY.get(resource, block);
        }
        return BLOCK_MODEL_REGISTRY.put(resource, block, modelLocation);
    }

    public static Optional<ResourceLocation> getItem(ResourceLocation key) {
        if (!ITEM_MODEL_REGISTRY.containsKey(key)) {
            if (key != null && !WARNED_ITEM_KEYS.contains(key)) {
                BovinesAndButtercups.LOG.warn("Could not get item model resource location from key '{}'.", key);
                WARNED_ITEM_KEYS.add(key);
            }
        }
        return Optional.ofNullable(ITEM_MODEL_REGISTRY.get(key));
    }

    public static Optional<ResourceLocation> getBlock(ResourceLocation key, StateDefinition<Block, BlockState> block) {
        if (!BLOCK_MODEL_REGISTRY.contains(key, block)) {
            if (key != null && !WARNED_BLOCK_KEYS.contains(Pair.of(key, block))) {
                BovinesAndButtercups.LOG.warn("Could not get block model resource location from key '{}' and statedefinition '{}'.", key, block);
                WARNED_BLOCK_KEYS.add(Pair.of(key, block));
            }
        }
        return Optional.ofNullable(BLOCK_MODEL_REGISTRY.get(key, block));
    }
}
