package net.merchantpug.bovinesandbuttercups.data.condition.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class PredicateCondition extends ConditionConfiguration<Entity> {
    public static MapCodec<PredicateCondition> getCodec() {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("predicate").forGetter(PredicateCondition::getPredicateLocation)
        ).apply(builder, PredicateCondition::new));
    }

    private final ResourceLocation predicateLocation;

    public PredicateCondition(ResourceLocation predicateLocation) {
        this.predicateLocation = predicateLocation;
    }

    @Override
    public boolean test(Entity entity) {
        if (!entity.level().isClientSide) {
            var predicate = BovinesAndButtercups.getServer().getLootData().getElement(LootDataType.PREDICATE, predicateLocation);
            return predicate != null && predicate.test(new LootContext.Builder(new LootParams.Builder((ServerLevel)entity.level()).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.THIS_ENTITY, entity).create(LootContextParamSets.SELECTOR)).create(null));
        }
        return false;
    }

    public ResourceLocation getPredicateLocation() {
        return predicateLocation;
    }
}
