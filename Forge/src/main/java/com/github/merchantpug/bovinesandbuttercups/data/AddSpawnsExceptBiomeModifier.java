package com.github.merchantpug.bovinesandbuttercups.data;

import com.github.merchantpug.bovinesandbuttercups.registry.BiomeModifierSerializerRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.List;
import java.util.Optional;

public record AddSpawnsExceptBiomeModifier(Optional<HolderSet<Biome>> biomes, List<MobSpawnSettings.SpawnerData> spawners) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && (biomes.isEmpty() || !biomes.get().contains(biome)))
        {
            MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            for (MobSpawnSettings.SpawnerData spawner : this.spawners)
            {
                EntityType<?> type = spawner.type;
                spawns.addSpawn(type.getCategory(), spawner);
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return BiomeModifierSerializerRegistry.ADD_SPAWNS_EXCEPT_BIOME_MODIFIER_TYPE.get();
    }
}
