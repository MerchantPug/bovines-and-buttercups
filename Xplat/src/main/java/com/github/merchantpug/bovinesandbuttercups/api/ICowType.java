package com.github.merchantpug.bovinesandbuttercups.api;

import com.github.merchantpug.bovinesandbuttercups.data.CowTypeRegistry;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public interface ICowType<T extends ICowTypeInstance> {
    /**
     * @return The unique identifier for this Cow Type. Used within registry to
     * make sure the instance can be linked to the correct cow type.
     */
    ResourceLocation getId();

    /**
     * @return The cow type instance to use when a cow type instance is missing
     * from the registry.
     */
    ICowTypeInstance getMissingCow();

    /**
     * @param instance The cow type instance to write to the buf.
     * @param buf The buf that the cow type instance is to be written to.
     */
    void write(T instance, FriendlyByteBuf buf);

    /**
     * @param buf The buf to read a cow type instance from.
     * @return The cow type instance that is read from the buf.
     */
    T read(FriendlyByteBuf buf);

    /**
     * @param resourceLocation The current resource location from the (@link CowLoader)
     * @param json The current JsonObject from the (@link CowLoader)
     * @return The cow type instance gained from the json object.
     */
    T fromJson(ResourceLocation resourceLocation, JsonObject json);

    static void register(ICowType<?> cowType, ResourceLocation id, JsonObject je) {
        ICowTypeInstance cowTypeInstance = cowType.fromJson(id, je.getAsJsonObject());
        if (!CowTypeRegistry.contains(id, null)) {
            CowTypeRegistry.register(id, cowTypeInstance);
        } else {
            if (CowTypeRegistry.get(id, null).getLoadingPriority() < cowTypeInstance.getLoadingPriority()) {
                CowTypeRegistry.update(id, cowTypeInstance);
            }
        }
    }
}
