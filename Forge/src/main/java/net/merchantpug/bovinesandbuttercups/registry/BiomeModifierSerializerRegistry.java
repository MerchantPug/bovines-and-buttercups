package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.AddSpawnsExceptBiomeModifier;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;

public class BiomeModifierSerializerRegistry {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<Codec<AddSpawnsExceptBiomeModifier>> ADD_SPAWNS_EXCEPT = BIOME_MODIFIER_SERIALIZERS.register("add_spawns_except", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.optionalFieldOf("biomes").forGetter(AddSpawnsExceptBiomeModifier::biomes),
                    new ExtraCodecs.EitherCodec<>(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of),
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
                    ).fieldOf("spawners").forGetter(AddSpawnsExceptBiomeModifier::spawners)
            ).apply(builder, AddSpawnsExceptBiomeModifier::new))
    );

    public static void init(IEventBus eventBus) {
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
