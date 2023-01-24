package net.merchantpug.bovinesandbuttercups.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;

public class HolderUtil {
    // This method exists purely because HolderSet.Direct.contains does not work as intended.
    public static boolean containsBiomeHolder(Holder<Biome> biomeHolder, HolderSet<Biome> holderSet) {
        if (holderSet instanceof HolderSet.Direct<Biome> && holderSet.size() == 1 && biomeHolder.unwrapKey().isPresent() && holderSet.get(0).is(biomeHolder.unwrapKey().get())) {
            return true;
        }
        return holderSet.contains(biomeHolder);
    }
}
