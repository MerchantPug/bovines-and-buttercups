package net.merchantpug.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Optional;

public class CodecUtil {

    public static <T> MapCodec<HolderSet<T>> tagOrObjectCodec(ResourceKey<Registry<T>> registryKey, String fieldName) {
        RegistryAccess registryAccess = getRegistryAccess();
        if (registryAccess == null) {
            return MapCodec.unit(HolderSet.direct());
        }
        Optional<Registry<T>> optionalRegistry = registryAccess.registry(registryKey);
        if (optionalRegistry.isEmpty()) {
            return MapCodec.unit(HolderSet.direct());
        }
        Registry<T> registry = optionalRegistry.get();
        return Codec.either(TagKey.hashedCodec(registryKey), registry.holderByNameCodec()).fieldOf(fieldName)
                .xmap(tagKeyHolderEither -> tagKeyHolderEither.map(registry::getOrCreateTag, HolderSet::direct),
                        holders -> {
                            if (holders.unwrapKey().isEmpty()) {
                                return Either.right(holders.get(0));
                            }
                            return Either.left(holders.unwrapKey().get());
                        });
    }

    public static <T> MapCodec<Optional<HolderSet<T>>> optionalTagOrObjectCodec(ResourceKey<Registry<T>> registryKey, String fieldName) {
        RegistryAccess registryAccess = getRegistryAccess();
        if (registryAccess == null) {
            return MapCodec.unit(Optional.empty());
        }
        Optional<Registry<T>> optionalRegistry = registryAccess.registry(registryKey);
        if (optionalRegistry.isEmpty()) {
            return MapCodec.unit(Optional.empty());
        }
        Registry<T> registry = optionalRegistry.get();
        return Codec.either(TagKey.hashedCodec(registryKey), registry.holderByNameCodec()).optionalFieldOf(fieldName)
                .xmap(tagKeyHolderEither -> tagKeyHolderEither.map(tagKeyHolderEither1 -> tagKeyHolderEither1.map(registry::getOrCreateTag, HolderSet::direct)),
                        holders -> holders.map(holders1 -> {
                            if (holders1.unwrapKey().isEmpty()) {
                                return Either.right(holders1.get(0));
                            }
                            return Either.left(holders1.unwrapKey().get());
                        }));
    }

    public static RegistryAccess getRegistryAccess() {
        if (!Services.PLATFORM.isClientSide()) {
            return BovinesAndButtercups.getServer().registryAccess();
        }
        return null;
    }

}
