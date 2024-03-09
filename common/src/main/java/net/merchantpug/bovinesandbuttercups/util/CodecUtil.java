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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.swing.text.html.Option;
import java.util.Optional;

public class CodecUtil {

    public static <T> MapCodec<HolderUtil.PsuedoHolder<T>> tagOrObjectCodec(ResourceKey<Registry<T>> registryKey, String fieldName) {
        return Codec.either(TagKey.hashedCodec(registryKey), ResourceLocation.CODEC.xmap(resourceLocation -> ResourceKey.create(registryKey, resourceLocation), ResourceKey::location)).fieldOf(fieldName)
                .xmap(tagKeyHolderEither -> tagKeyHolderEither.map(
                        tagKey -> new HolderUtil.PsuedoHolder<>(registryKey, Either.right(tagKey)),
                             key -> new HolderUtil.PsuedoHolder<>(registryKey, Either.left(key))),
                        psuedoHolder -> {
                            if (psuedoHolder.key().right().isPresent()) {
                                return Either.left(psuedoHolder.key().right().get());
                            }
                            if (psuedoHolder.key().left().isPresent()) {
                                return Either.right(psuedoHolder.key().left().get());
                            }
                            return Either.left(null);
                        });
    }

    public static <T> MapCodec<Optional<HolderUtil.PsuedoHolder<T>>> optionalTagOrObjectCodec(ResourceKey<Registry<T>> registryKey, String fieldName) {
        return Codec.either(TagKey.hashedCodec(registryKey), ResourceLocation.CODEC.xmap(resourceLocation -> ResourceKey.create(registryKey, resourceLocation), ResourceKey::location)).optionalFieldOf(fieldName)
                .xmap(optional ->
                    optional.map(tagKeyHolderEither -> tagKeyHolderEither.map(
                            tagKey -> new HolderUtil.PsuedoHolder<>(registryKey, Either.right(tagKey)),
                            key -> new HolderUtil.PsuedoHolder<>(registryKey, Either.left(key))
                    )),
                    optionalPsuedoHolder -> {
                        if (optionalPsuedoHolder.isPresent() && optionalPsuedoHolder.get().key().right().isPresent()) {
                            return Optional.of(Either.left(optionalPsuedoHolder.get().key().right().get()));
                        }
                        if (optionalPsuedoHolder.isPresent() && optionalPsuedoHolder.get().key().left().isPresent()) {
                            return Optional.of(Either.right(optionalPsuedoHolder.get().key().left().get()));
                        }
                        return Optional.empty();
                    });
    }

    public static RegistryAccess getRegistryAccess() {
        if (BovinesAndButtercups.getServer() != null) {
            return BovinesAndButtercups.getServer().registryAccess();
        }
        return null;
    }

}
