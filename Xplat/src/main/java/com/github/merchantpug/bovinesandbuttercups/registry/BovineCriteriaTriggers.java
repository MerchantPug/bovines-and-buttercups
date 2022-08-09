package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.advancements.criterion.NearbyEntitiesTrigger;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;

public class BovineCriteriaTriggers {
    public static final NearbyEntitiesTrigger NEARBY_ENTITY = (NearbyEntitiesTrigger)Services.PLATFORM.registerCriteria(new NearbyEntitiesTrigger());

    public static void init() {

    }
}
