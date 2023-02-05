package net.merchantpug.bovinesandbuttercups.util;

import org.joml.Quaternionf;

public class QuaternionUtil {
    public static Quaternionf inverse(Quaternionf original) {
        return new Quaternionf(-original.x(), -original.y(), -original.z(), original.w());
    }
}
