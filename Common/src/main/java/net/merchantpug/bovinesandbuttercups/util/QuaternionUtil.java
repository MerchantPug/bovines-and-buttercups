package net.merchantpug.bovinesandbuttercups.util;

import com.mojang.math.Quaternion;

public class QuaternionUtil {
    public static Quaternion inverse(Quaternion original) {
        return new Quaternion(-original.i(), -original.j(), -original.k(), original.r());
    }
}
