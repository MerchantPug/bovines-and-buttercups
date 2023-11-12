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
import java.util.Optional;

public class MutationTrigger extends SimpleCriterionTrigger<MutationTrigger.TriggerInstance> {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("mutation");

    @Override
    protected MutationTrigger.TriggerInstance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context) {
        List<ResourceLocation> moobloomKeys = new ArrayList<>();
        if (json.has("types")) {
            json.getAsJsonArray("types").forEach(jsonElement -> {
                moobloomKeys.add(ResourceLocation.tryParse(jsonElement.getAsString()));
            });
        }
        Optional<ContextAwarePredicate> parent = EntityPredicate.fromJson(json, "parent", context);
        Optional<ContextAwarePredicate> partner = EntityPredicate.fromJson(json, "partner", context);
        Optional<ContextAwarePredicate> child = EntityPredicate.fromJson(json, "child", context);
        return new MutationTrigger.TriggerInstance(predicate, moobloomKeys, parent, partner, child);
    }


    public void trigger(ServerPlayer serverPlayer, Animal parent, Animal partner, AgeableMob child, ResourceLocation moobloomType) {
        LootContext parentContext = EntityPredicate.createContext(serverPlayer, parent);
        LootContext partnerContext = EntityPredicate.createContext(serverPlayer, partner);
        LootContext childContext = child != null ? EntityPredicate.createContext(serverPlayer, child) : null;
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(parentContext, partnerContext, childContext, moobloomType));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final List<ResourceLocation> types;
        private final Optional<ContextAwarePredicate> parent;
        private final Optional<ContextAwarePredicate> partner;
        private final Optional<ContextAwarePredicate> child;

        public TriggerInstance(Optional<ContextAwarePredicate> player, List<ResourceLocation> types, Optional<ContextAwarePredicate> parent, Optional<ContextAwarePredicate> partner, Optional<ContextAwarePredicate> child) {
            super(player);
            this.types = types;
            this.parent = parent;
            this.partner = partner;
            this.child = child;
        }

        public boolean matches(LootContext parentContext, LootContext partnerContext, LootContext childContext, ResourceLocation type) {
            if ((types.isEmpty() || types.contains(type)) && (this.child.isEmpty() || this.child.get().matches(childContext))) {
                return (this.parent.isEmpty() || this.parent.get().matches(parentContext)) && (this.partner.isEmpty() || this.partner.get().matches(partnerContext)) || (this.parent.isEmpty() || this.parent.get().matches(partnerContext)) && (this.partner.isEmpty() || this.partner.get().matches(parentContext));
            }
            return false;
        }

        public JsonObject serializeToJson() {
            JsonObject json = super.serializeToJson();
            JsonArray array = new JsonArray();
            types.forEach(resourceLocation -> array.add(resourceLocation.toString()));
            json.add("types", array);
            this.parent.ifPresent(p -> json.add("parent", p.toJson()));
            this.partner.ifPresent(p -> json.add("partner", p.toJson()));
            this.child.ifPresent(p -> json.add("child", p.toJson()));
            return json;
        }
    }
}
