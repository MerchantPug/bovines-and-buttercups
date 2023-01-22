package net.merchantpug.bovinesandbuttercups.mixin.forge;

import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MobSpawnSettings.class)
public interface MobSpawnSettingsAccessor {
    @Accessor("spawners")
    Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> bovinesandbuttercups$getSpawners();

    @Accessor("spawners")
    void bovinesandbuttercups$setSpawners(Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> value);
}
