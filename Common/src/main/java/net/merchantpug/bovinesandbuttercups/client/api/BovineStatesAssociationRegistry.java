package net.merchantpug.bovinesandbuttercups.client.api;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.mojang.datafixers.util.Pair;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BovineStatesAssociationRegistry {
    private static final Table<ResourceLocation, Block, ResourceLocation> REGISTRY = Tables.synchronizedTable(HashBasedTable.create());
    private static final Set<Pair<ResourceLocation, Block>> WARNED_KEYS = new HashSet<>();

    public static void clear() {
        REGISTRY.clear();
    }

    public static ResourceLocation register(ResourceLocation resource, Block block, ResourceLocation modelLocation) {
        if (REGISTRY.contains(resource, block)) {
            return REGISTRY.get(resource, block);
        }
        return REGISTRY.put(resource, block, modelLocation);
    }

    public static Optional<ResourceLocation> get(ResourceLocation resourceLocation, Block block) {
        if (!REGISTRY.contains(resourceLocation, block)) {
            if (resourceLocation != null && !WARNED_KEYS.contains(Pair.of(resourceLocation, block))) {
                BovinesAndButtercups.LOG.warn("Could not get resource location from key '{}' and block '{}'.", resourceLocation, Registry.BLOCK.getKey(block));
                WARNED_KEYS.add(Pair.of(resourceLocation, block));
            }
        }
        return Optional.ofNullable(REGISTRY.get(resourceLocation, block));
    }
}
