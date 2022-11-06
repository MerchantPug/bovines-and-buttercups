package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.NearbyEntitiesTrigger;
import net.merchantpug.bovinesandbuttercups.platform.Services;

public class BovineCriteriaTriggers {
    public static final NearbyEntitiesTrigger NEARBY_ENTITY = (NearbyEntitiesTrigger)Services.PLATFORM.registerCriteria(new NearbyEntitiesTrigger());

    public static void init() {

    }
}
