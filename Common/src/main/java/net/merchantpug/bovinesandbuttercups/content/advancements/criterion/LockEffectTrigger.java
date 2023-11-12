package net.merchantpug.bovinesandbuttercups.content.advancements.criterion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class LockEffectTrigger extends SimpleCriterionTrigger<LockEffectTrigger.TriggerInstance> {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("lock_effect");

    @Override
    protected LockEffectTrigger.TriggerInstance createInstance(JsonObject json, Optional<ContextAwarePredicate> predicate, DeserializationContext context) {
        Optional<MobEffect> effect = Optional.empty();
        if (json.has("effect")) {
            effect = BuiltInRegistries.MOB_EFFECT.getOptional(ResourceLocation.tryParse(json.getAsJsonPrimitive("effect").getAsString()));
        }
        return new LockEffectTrigger.TriggerInstance(predicate, effect);
    }

    public void trigger(ServerPlayer serverPlayer, MobEffect effect) {
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(effect));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final Optional<MobEffect> effect;

        public TriggerInstance(Optional<ContextAwarePredicate> predicate, Optional<MobEffect> effect) {
            super(predicate);
            this.effect = effect;
        }

        public boolean matches(MobEffect value) {
            return effect.isEmpty() || effect.get() == value;
        }

        public @NotNull JsonObject serializeToJson() {
            JsonObject json = super.serializeToJson();
            Optional<MobEffect> effect = BuiltInRegistries.MOB_EFFECT.getOptional(ResourceLocation.tryParse(json.getAsJsonPrimitive("effect").getAsString()));
            if (effect.isPresent()) {
                JsonElement element = JsonParser.parseString(json.getAsJsonPrimitive("effect").getAsString());
                json.add("effect", element);
            }
            return json;
        }
    }
}
