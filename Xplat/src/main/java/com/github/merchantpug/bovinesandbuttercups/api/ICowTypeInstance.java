package com.github.merchantpug.bovinesandbuttercups.api;

import net.minecraft.resources.ResourceLocation;

public interface ICowTypeInstance {
    /**
     * @return The (@link ICowType) that this instance was gained from.
     */
    ICowType getType();

    /**
     * @return The unique identifier for this cow type instance.
     */
    ResourceLocation getId();

    /**
     * @return The loading priority for this cow type instance.
     * Used for determining what will be loaded and overrides.
     */
    int getLoadingPriority();
}
