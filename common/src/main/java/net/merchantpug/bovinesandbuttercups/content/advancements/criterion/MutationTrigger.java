package net.merchantpug.bovinesandbuttercups.content.advancements.criterion;

import com.google.gson.JsonArray;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
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
    public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                    ResourceLocation.CODEC.listOf().optionalFieldOf("types", List.of()).forGetter(TriggerInstance::types),
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("parent").forGetter(TriggerInstance::player),
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("partner").forGetter(TriggerInstance::player),
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("child").forGetter(TriggerInstance::player)
            ).apply(inst, TriggerInstance::new)
    );


    public void trigger(ServerPlayer serverPlayer, Animal parent, Animal partner, AgeableMob child, ResourceLocation moobloomType) {
        LootContext parentContext = EntityPredicate.createContext(serverPlayer, parent);
        LootContext partnerContext = EntityPredicate.createContext(serverPlayer, partner);
        LootContext childContext = child != null ? EntityPredicate.createContext(serverPlayer, child) : null;
        this.trigger(serverPlayer, (triggerInstance) -> triggerInstance.matches(parentContext, partnerContext, childContext, moobloomType));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, List<ResourceLocation> types,
                                  Optional<ContextAwarePredicate> parent, Optional<ContextAwarePredicate> partner,
                                  Optional<ContextAwarePredicate> child) implements SimpleInstance {
        public boolean matches(LootContext parentContext, LootContext partnerContext, LootContext childContext, ResourceLocation type) {
            if ((types.isEmpty() || types.contains(type)) && (this.child.isEmpty() || this.child.get().matches(childContext))) {
                return (this.parent.isEmpty() || this.parent.get().matches(parentContext)) && (this.partner.isEmpty() || this.partner.get().matches(partnerContext)) || (this.parent.isEmpty() || this.parent.get().matches(partnerContext)) && (this.partner.isEmpty() || this.partner.get().matches(parentContext));
            }
            return false;
        }
    }
}
