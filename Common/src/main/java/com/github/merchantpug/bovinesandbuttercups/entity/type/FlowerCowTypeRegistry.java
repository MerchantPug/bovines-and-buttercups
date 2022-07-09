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

package com.github.merchantpug.bovinesandbuttercups.entity.type;

import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowType;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FlowerCowTypeRegistry {
    private static final HashMap<ResourceLocation, FlowerCowType> idToMoobloomType = new HashMap<>();

    public static FlowerCowType register(FlowerCowType type) {
        return register(type.getResourceLocation(), type);
    }

    public static FlowerCowType register(ResourceLocation resourceLocation, FlowerCowType type) {
        if (idToMoobloomType.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Duplicate moobloom type id! Tried to register: '" + resourceLocation.toString() + "'");
        }
        idToMoobloomType.put(resourceLocation, type);
        return type;
    }

    protected static void update(ResourceLocation resourceLocation, FlowerCowType type) {
        if (idToMoobloomType.containsKey(resourceLocation)) {
            idToMoobloomType.replace(resourceLocation, type);
        } else {
            register(resourceLocation, type);
        }
    }

    public static HashMap<ResourceLocation, FlowerCowType> getIdToMoobloomTypes() {
        return idToMoobloomType;
    }

    public static boolean contains(ResourceLocation resourceLocation) {
        return idToMoobloomType.containsKey(resourceLocation);
    }

    public static int size() {
        return idToMoobloomType.size();
    }

    public static Stream<ResourceLocation> identifiers() {
        return idToMoobloomType.keySet().stream();
    }

    public static Iterable<Map.Entry<ResourceLocation, FlowerCowType>> entries() {
        return idToMoobloomType.entrySet();
    }

    public static Stream<FlowerCowType> valueStream() {
        return idToMoobloomType.values().stream();
    }

    public static Iterable<FlowerCowType> values() {
        return idToMoobloomType.values();
    }

    public static FlowerCowType get(ResourceLocation resourceLocation) {
        if(!idToMoobloomType.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Could not get moobloom type from id '" + resourceLocation.toString() + "', as it was not registered!");
        }
        return idToMoobloomType.get(resourceLocation);
    }

    public static FlowerCowType fromString(String string) {
        try {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(string);
            return get(resourceLocation);
        } catch (ResourceLocationException exception) {
            throw new ResourceLocationException("Could not parse string: '" + string + "'");
        }
    }

    public static void reset() {
        idToMoobloomType.clear();
        register(FlowerCowType.MISSING);
    }
}
