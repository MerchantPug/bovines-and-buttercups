package net.merchantpug.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class HolderUtil {
    public static boolean containsBiomeHolder(Holder<Biome> biomeHolder, PsuedoHolder<Biome> psuedoHolder) {
        return psuedoHolder.contains(CodecUtil.getRegistryAccess(), biomeHolder);
    }

    public record PsuedoHolder<T>(ResourceKey<Registry<T>> registry, Either<ResourceKey<T>, TagKey<T>> key) {
        public boolean contains(RegistryAccess registryAccess, Holder<T> holder) {
            if (key().left().isEmpty() && key().right().isEmpty()) {
                return false;
            }
            return key().map(holder::is, tagKey -> registryAccess.registry(registry()).map(registry -> registry.getTag(tagKey).map(holders -> holders.contains(holder)).orElse(false)).orElse(false));
        }
    }
}
