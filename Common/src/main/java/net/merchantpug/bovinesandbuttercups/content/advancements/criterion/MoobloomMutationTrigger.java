package net.merchantpug.bovinesandbuttercups.content.advancements.criterion;

import com.google.gson.JsonArray;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class MoobloomMutationTrigger extends SimpleCriterionTrigger<MoobloomMutationTrigger.TriggerInstance> {
    static final ResourceLocation ID = BovinesAndButtercups.asResource("moobloom_mutation");

    @Override
    protected MoobloomMutationTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        List<ResourceLocation> moobloomKeys = new ArrayList<>();
        if (json.has("types")) {
            json.getAsJsonArray("types").forEach(jsonElement -> {
                moobloomKeys.add(ResourceLocation.tryParse(jsonElement.getAsString()));
            });
        }
        return new MoobloomMutationTrigger.TriggerInstance(player, moobloomKeys);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }


    public void trigger(ServerPlayer serverPlayer, ResourceLocation moobloomType) {
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(moobloomType));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final List<ResourceLocation> types;

        public TriggerInstance(EntityPredicate.Composite player, List<ResourceLocation> types) {
            super(MoobloomMutationTrigger.ID, player);
            this.types = types;
        }

        public boolean matches(ResourceLocation type) {
            return types.isEmpty() || types.contains(type);
        }

        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonobject = super.serializeToJson(context);
            JsonArray array = new JsonArray();
            types.forEach(resourceLocation -> array.add(resourceLocation.toString()));
            jsonobject.add("types", array);
            return jsonobject;
        }
    }
}
