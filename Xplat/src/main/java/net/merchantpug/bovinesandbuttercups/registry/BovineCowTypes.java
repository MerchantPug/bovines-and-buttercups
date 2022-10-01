package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class BovineCowTypes {
    public static final RegistrationProvider<CowType<?>> COW_TYPE_REGISTRY = RegistrationProvider.Factory.INSTANCE.create(BovineRegistryKeys.COW_TYPE_KEY, "bovinesandbuttercups");

    public static final RegistryObject<CowType<FlowerCowConfiguration>> FLOWER_COW_TYPE = COW_TYPE_REGISTRY.register("moobloom", () -> new CowType<>(asMapCodec(FlowerCowConfiguration.CODEC)));
    public static final RegistryObject<CowType<MushroomCowConfiguration>> MUSHROOM_COW_TYPE = COW_TYPE_REGISTRY.register("mooshroom", () -> new CowType<>(asMapCodec(MushroomCowConfiguration.CODEC)));

    public static void init() {

    }

    public static <C> MapCodec<C> asMapCodec(Codec<C> codec) {
        if (codec instanceof MapCodec.MapCodecCodec) {
            return ((MapCodec.MapCodecCodec<C>) codec).codec();
        }
        return codec.fieldOf("value");
    }
}
