package net.merchantpug.bovinesandbuttercups.registry;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.AddCowTypeSpawnsModifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class BovineBiomeModifierSerializers {
    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<Codec<AddCowTypeSpawnsModifier>> ADD_COW_TYPE_SPAWNS_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("add_cow_type_spawns", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    BovineRegistriesForge.COW_TYPE_REGISTRY.get().getCodec().fieldOf("cow_type").forGetter(AddCowTypeSpawnsModifier::cowType),
                    Biome.LIST_CODEC.optionalFieldOf("excluded_biomes").orElseGet(Optional::empty).forGetter(AddCowTypeSpawnsModifier::excludedBiomes),
                    new ExtraCodecs.EitherCodec<>(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of),
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
                    ).fieldOf("spawners").forGetter(AddCowTypeSpawnsModifier::spawners)
            ).apply(builder, AddCowTypeSpawnsModifier::new))
    );


    public static void register(IEventBus eventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
