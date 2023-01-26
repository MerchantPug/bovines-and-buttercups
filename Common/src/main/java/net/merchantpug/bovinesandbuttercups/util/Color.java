package net.merchantpug.bovinesandbuttercups.util;

import com.mojang.datafixers.util.Either;
import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;

public class Color {
    public static final Codec<Vector3f> CODEC = Codec.either(Vector3f.CODEC, Codec.STRING).xmap(vec3StringEither -> vec3StringEither.map(vector3f -> vector3f, s -> {
        String hex = s;
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        } else if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        try {
            String r = "#" + hex.substring(0, 2);
            String g = "#" + hex.substring(2, 4);
            String b = "#" + hex.substring(4, 6);
            return new Vector3f(Integer.decode(r) / 255.0F, Integer.decode(g) / 255.0F, Integer.decode(b) / 255.0F);
        } catch (Throwable e) {
            BovinesAndButtercups.LOG.warn("Could not serialize Color data type '{}'. (Skipping.) {}.", hex, e.getMessage());
        }
        return new Vector3f(1.0F, 1.0F, 1.0F);
    }), Either::left);

    public static int asInt(Vector3f rgb) {
        return ((((int)(rgb.x() * 255)) & 0x0ff) << 16) + ((((int)(rgb.y() * 255)) & 0x0ff) << 8) + ((((int)(rgb.z() * 255)) & 0x0ff));
    }

    public static Vector3f saturateForNectar(Vector3f rgb) {
        float r = rgb.x();
        float g = rgb.y();
        float b = rgb.z();

        float l = 0.3F * r + 0.6F * g + 0.1F * b;
        r = r + 0.5F * (l - r);
        g = g + 0.5F * (l - g);
        b = b + 0.5F * (l - b);

        return new Vector3f(r, g, b);
    }
}
