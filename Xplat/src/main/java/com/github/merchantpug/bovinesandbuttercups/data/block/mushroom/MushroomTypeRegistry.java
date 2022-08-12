/*
MIT License

Copyright (c) 2020 apace100

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.github.merchantpug.bovinesandbuttercups.data.block.mushroom;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MushroomTypeRegistry {
    private static final HashMap<ResourceLocation, MushroomType> idToMushroomType = new HashMap<>();

    public static MushroomType register(MushroomType type) {
        return register(type.getResourceLocation(), type);
    }

    public static MushroomType register(ResourceLocation resourceLocation, MushroomType type) {
        if (idToMushroomType.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Duplicate mushroom type id! Tried to register: '" + resourceLocation.toString() + "'");
        }
        idToMushroomType.put(resourceLocation, type);
        return type;
    }

    public static void update(ResourceLocation resourceLocation, MushroomType type) {
        if (idToMushroomType.containsKey(resourceLocation)) {
            idToMushroomType.replace(resourceLocation, type);
        } else {
            register(resourceLocation, type);
        }
    }

    public static HashMap<ResourceLocation, MushroomType> getIdToMushroomTypes() {
        return idToMushroomType;
    }

    public static boolean contains(ResourceLocation resourceLocation) {
        return idToMushroomType.containsKey(resourceLocation);
    }

    public static int size() {
        return idToMushroomType.size();
    }

    public static Stream<ResourceLocation> identifiers() {
        return idToMushroomType.keySet().stream();
    }

    public static Iterable<Map.Entry<ResourceLocation, MushroomType>> entries() {
        return idToMushroomType.entrySet();
    }

    public static Stream<MushroomType> valueStream() {
        return idToMushroomType.values().stream();
    }

    public static Iterable<MushroomType> values() {
        return idToMushroomType.values();
    }

    public static MushroomType get(ResourceLocation resourceLocation) {
        if(!idToMushroomType.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Could not get mushroom type from id '" + resourceLocation.toString() + "', as it was not registered!");
        }
        return idToMushroomType.get(resourceLocation);
    }

    public static MushroomType fromString(String string) {
        try {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(string);
            return get(resourceLocation);
        } catch (ResourceLocationException exception) {
            throw new ResourceLocationException("Could not parse string: '" + string + "'");
        }
    }

    public static void reset() {
        idToMushroomType.clear();
        register(MushroomType.MISSING);
    }
}
