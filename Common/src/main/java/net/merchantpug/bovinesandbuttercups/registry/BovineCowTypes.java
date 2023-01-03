package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class BovineCowTypes {
    public static final CowType<FlowerCowConfiguration> FLOWER_COW_TYPE = new CowType<>(asMapCodec(FlowerCowConfiguration.CODEC));
    public static final CowType<MushroomCowConfiguration> MUSHROOM_COW_TYPE = new CowType<>(asMapCodec(MushroomCowConfiguration.CODEC));

    public static <C> MapCodec<C> asMapCodec(Codec<C> codec) {
        if (codec instanceof MapCodec.MapCodecCodec) {
            return ((MapCodec.MapCodecCodec<C>) codec).codec();
        }
        return codec.fieldOf("value");
    }
}
