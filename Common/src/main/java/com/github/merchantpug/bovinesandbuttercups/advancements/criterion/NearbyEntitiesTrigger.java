package com.github.merchantpug.bovinesandbuttercups.advancements.criterion;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class NearbyEntitiesTrigger extends SimpleCriterionTrigger<NearbyEntitiesTrigger.TriggerInstance> {
    static final ResourceLocation ID = Constants.resourceLocation("nearby_entities");

    @Override
    protected NearbyEntitiesTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        double radius = json.getAsJsonPrimitive("radius").getAsDouble();
        EntityPredicate.Composite[] entity = EntityPredicate.Composite.fromJsonArray(json, "entities", context);
        return new NearbyEntitiesTrigger.TriggerInstance(player, radius, entity);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }


    public void trigger(ServerPlayer serverPlayer) {
        List<LootContext> contexts = Lists.newArrayList();

        this.trigger(serverPlayer, (triggerInstance) -> {
            for (LivingEntity entity : serverPlayer.level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(), serverPlayer, serverPlayer.getBoundingBox().inflate(triggerInstance.radius))) {
                contexts.add(EntityPredicate.createContext(serverPlayer, entity));
            }
            return triggerInstance.matches(contexts);
        });
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public final double radius;
        private final EntityPredicate.Composite[] entities;

        public TriggerInstance(EntityPredicate.Composite player, double radius, EntityPredicate.Composite[] entities) {
            super(NearbyEntitiesTrigger.ID, player);
            this.entities = entities;
            this.radius = radius;
        }

        public boolean matches(Collection<LootContext> contexts) {
            if (this.entities.length > 0) {
                List<LootContext> list = Lists.newArrayList(contexts);

                for(EntityPredicate.Composite entity : this.entities) {
                    boolean bool = false;
                    Iterator<LootContext> iterator = list.iterator();

                    while(iterator.hasNext()) {
                        LootContext context = iterator.next();
                        if (entity.matches(context)) {
                            iterator.remove();
                            bool = true;
                            break;
                        }
                    }

                    if (!bool) {
                        return false;
                    }
                }
            }
            return true;
        }

        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonobject = super.serializeToJson(context);
            jsonobject.addProperty("radius", this.radius);
            jsonobject.add("entities", EntityPredicate.Composite.toJson(this.entities, context));
            return jsonobject;
        }
    }
}
