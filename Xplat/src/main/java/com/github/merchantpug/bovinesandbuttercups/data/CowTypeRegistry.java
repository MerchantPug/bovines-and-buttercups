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

package com.github.merchantpug.bovinesandbuttercups.data;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.api.ICowTypeInstance;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class CowTypeRegistry {
    private static final List<ICowType> cowTypes = new ArrayList<>();
    private static final HashMap<ResourceLocation, ICowTypeInstance> idToCowType = new HashMap<>();

    public static ICowType<?> registerType(ICowType<?> type) {
        cowTypes.add(type);
        return type;
    }

    public static List<ICowType> getCowTypes() {
        return cowTypes;
    }

    @Nullable public static ICowType getTypeFromId(ResourceLocation resourceLocation) {
        Optional<ICowType> cowType = cowTypes.stream().filter(filteredCowType -> filteredCowType.getId().equals(resourceLocation)).findFirst();
        return cowType.isEmpty() ? null : cowType.get();
    }

    public static ICowTypeInstance register(ICowTypeInstance type) {
        return register(type.getId(), type);
    }

    public static ICowTypeInstance register(ResourceLocation resourceLocation, ICowTypeInstance type) {
        if (idToCowType.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Duplicate cow type id! Tried to register: '" + resourceLocation.toString() + "'");
        }
        idToCowType.put(resourceLocation, type);
        return type;
    }

    public static void update(ResourceLocation resourceLocation, ICowTypeInstance type) {
        if (idToCowType.containsKey(resourceLocation)) {
            idToCowType.replace(resourceLocation, type);
        } else {
            register(resourceLocation, type);
        }
    }

    public static boolean contains(ResourceLocation resourceLocation, ICowType filterType) {
        if (filterType == null || idToCowType.containsKey(resourceLocation) && idToCowType.get(resourceLocation).getType() == filterType) {
            return idToCowType.containsKey(resourceLocation);
        }
        return false;
    }

    public static int size() {
        return idToCowType.size();
    }

    public static Stream<ResourceLocation> identifiers() {
        return idToCowType.keySet().stream();
    }

    public static Iterable<Map.Entry<ResourceLocation, ICowTypeInstance>> entries() {
        return idToCowType.entrySet();
    }

    public static Stream<ICowTypeInstance> valueStream() {
        return idToCowType.values().stream();
    }

    public static Iterable<ICowTypeInstance> values() {
        return idToCowType.values();
    }

    public static ICowTypeInstance get(ResourceLocation resourceLocation) {
        return get(resourceLocation, null);
    }

    public static ICowTypeInstance get(ResourceLocation resourceLocation, @Nullable ICowType filterType) {
        if(!idToCowType.containsKey(resourceLocation)) {
            throw new IllegalArgumentException("Could not get moobloom type from id '" + resourceLocation.toString() + "', as it was not registered!");
        }
        if (filterType == null || idToCowType.get(resourceLocation).getType().getClass() == filterType.getClass()) {
            return idToCowType.get(resourceLocation);
        }

        return filterType.getMissingCow();
    }

    public static ICowTypeInstance fromName(String name, @Nullable ICowType filterType) {
        try {
            ResourceLocation id = ResourceLocation.tryParse(name);
            return CowTypeRegistry.get(id, filterType);
        } catch (Exception exception) {
            Constants.LOG.warn("Could not get Moobloom type from name '" + name + "'.", exception.getMessage());
        }
        if (filterType != null) {
            return filterType.getMissingCow();
        }
        return null;
    }

    public static void reset() {
        idToCowType.clear();
        cowTypes.forEach(cowType -> {
            if (cowType.getMissingCow() == null) return;
            register(cowType.getMissingCow());
        });
    }
}
