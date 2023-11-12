package net.merchantpug.bovinesandbuttercups.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class ClassUtil {
    public static <T> Class<T> castClass(Class<?> aClass) {
        return (Class<T>)aClass;
    }

    public static <C> MapCodec<C> asMapCodec(Codec<C> codec) {
        if (codec instanceof MapCodec.MapCodecCodec) {
            return ((MapCodec.MapCodecCodec<C>) codec).codec();
        }
        return codec.fieldOf("value");
    }
}
