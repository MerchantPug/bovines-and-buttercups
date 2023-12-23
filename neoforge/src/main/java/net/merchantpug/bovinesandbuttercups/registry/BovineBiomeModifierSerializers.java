package net.merchantpug.bovinesandbuttercups.registry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.AddCowTypeSpawnsModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class BovineBiomeModifierSerializers {
    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, BovinesAndButtercups.MOD_ID);

    public static final Supplier<Codec<AddCowTypeSpawnsModifier>> ADD_COW_TYPE_SPAWNS_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("add_cow_type_spawns", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BovineRegistriesNeoForge.COW_TYPE.byNameCodec().fieldOf("cow_type").forGetter(AddCowTypeSpawnsModifier::cowType),
                    Biome.LIST_CODEC.optionalFieldOf("excluded_biomes").forGetter(AddCowTypeSpawnsModifier::excludedBiomes),
                    Codec.either(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of),
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
                    ).fieldOf("spawners").forGetter(AddCowTypeSpawnsModifier::spawners)
            ).apply(builder, AddCowTypeSpawnsModifier::new))
    );


    public static void register(IEventBus eventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
