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

package com.github.merchantpug.bovinesandbuttercups.data.block.flower;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class FlowerTypeRegistry {
    private static final HashMap<ResourceLocation, FlowerType> idToFlowerType = new HashMap<>();

    public static FlowerType register(FlowerType type) {
        return register(type.getResourceLocation(), type);
    }

    public static FlowerType register(ResourceLocation resourceLocation, FlowerType type) {
        if (idToFlowerType.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Duplicate moobloom type id! Tried to register: '" + resourceLocation.toString() + "'");
        }
        idToFlowerType.put(resourceLocation, type);
        return type;
    }

    protected static void update(ResourceLocation resourceLocation, FlowerType type) {
        if (idToFlowerType.containsKey(resourceLocation)) {
            idToFlowerType.replace(resourceLocation, type);
        } else {
            register(resourceLocation, type);
        }
    }

    public static HashMap<ResourceLocation, FlowerType> getIdToFlowerTypes() {
        return idToFlowerType;
    }

    public static boolean contains(ResourceLocation resourceLocation) {
        return idToFlowerType.containsKey(resourceLocation);
    }

    public static int size() {
        return idToFlowerType.size();
    }

    public static Stream<ResourceLocation> identifiers() {
        return idToFlowerType.keySet().stream();
    }

    public static Collection<Map.Entry<ResourceLocation, FlowerType>> entries() {
        return idToFlowerType.entrySet();
    }

    public static Stream<FlowerType> valueStream() {
        return idToFlowerType.values().stream();
    }

    public static Collection<FlowerType> values() {
        return idToFlowerType.values();
    }

    public static FlowerType get(ResourceLocation resourceLocation) {
        if(!idToFlowerType.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Could not get flower type from id '" + resourceLocation.toString() + "', as it was not registered!");
        }
        return idToFlowerType.get(resourceLocation);
    }

    public static FlowerType fromString(String string) {
        try {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(string);
            return get(resourceLocation);
        } catch (ResourceLocationException exception) {
            throw new ResourceLocationException("Could not parse string: '" + string + "'");
        }
    }

    public static void reset() {
        idToFlowerType.clear();
        register(FlowerType.MISSING);
    }
}
