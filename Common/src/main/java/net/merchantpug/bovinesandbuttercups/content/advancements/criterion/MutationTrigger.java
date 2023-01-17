package net.merchantpug.bovinesandbuttercups.content.advancements.criterion;

import com.google.gson.JsonArray;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;

public class MutationTrigger extends SimpleCriterionTrigger<MutationTrigger.TriggerInstance> {
    static final ResourceLocation ID = BovinesAndButtercups.asResource("mutation");

    @Override
    protected MutationTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        List<ResourceLocation> moobloomKeys = new ArrayList<>();
        if (json.has("types")) {
            json.getAsJsonArray("types").forEach(jsonElement -> {
                moobloomKeys.add(ResourceLocation.tryParse(jsonElement.getAsString()));
            });
        }
        EntityPredicate.Composite parent = EntityPredicate.Composite.fromJson(json, "parent", context);
        EntityPredicate.Composite partner = EntityPredicate.Composite.fromJson(json, "partner", context);
        EntityPredicate.Composite child = EntityPredicate.Composite.fromJson(json, "child", context);
        return new MutationTrigger.TriggerInstance(player, moobloomKeys, parent, partner, child);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }


    public void trigger(ServerPlayer serverPlayer, Animal parent, Animal partner, AgeableMob child, ResourceLocation moobloomType) {
        LootContext parentContext = EntityPredicate.createContext(serverPlayer, parent);
        LootContext partnerContext = EntityPredicate.createContext(serverPlayer, partner);
        LootContext childContext = child != null ? EntityPredicate.createContext(serverPlayer, child) : null;
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(parentContext, partnerContext, childContext, moobloomType));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final List<ResourceLocation> types;
        private final EntityPredicate.Composite parent;
        private final EntityPredicate.Composite partner;
        private final EntityPredicate.Composite child;

        public TriggerInstance(EntityPredicate.Composite player, List<ResourceLocation> types, EntityPredicate.Composite parent, EntityPredicate.Composite partner, EntityPredicate.Composite child) {
            super(MutationTrigger.ID, player);
            this.types = types;
            this.parent = parent;
            this.partner = partner;
            this.child = child;
        }

        public boolean matches(LootContext parentContext, LootContext partnerContext, LootContext childContext, ResourceLocation type) {
            if ((types.isEmpty() || types.contains(type)) && this.child == EntityPredicate.Composite.ANY || childContext != null && this.child.matches(childContext)) {
                return this.parent.matches(parentContext) && this.partner.matches(partnerContext) || this.parent.matches(partnerContext) && this.partner.matches(parentContext);
            }
            return false;
        }

        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject json = super.serializeToJson(context);
            JsonArray array = new JsonArray();
            types.forEach(resourceLocation -> array.add(resourceLocation.toString()));
            json.add("types", array);
            json.add("parent", this.parent.toJson(context));
            json.add("partner", this.partner.toJson(context));
            json.add("child", this.child.toJson(context));
            return json;
        }
    }
}
