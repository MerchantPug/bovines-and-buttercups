package net.merchantpug.bovinesandbuttercups.data.condition.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class PredicateConditionConfiguration extends ConditionConfiguration<Entity> {
    public static final MapCodec<PredicateConditionConfiguration> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("predicate").forGetter(PredicateConditionConfiguration::getPredicateLocation)
    ).apply(builder, PredicateConditionConfiguration::new));

    private final ResourceLocation predicateLocation;

    public PredicateConditionConfiguration(ResourceLocation predicateLocation) {
        this.predicateLocation = predicateLocation;
    }

    @Override
    public boolean test(Entity entity) {
        if (!entity.level.isClientSide) {
            var predicate = BovinesAndButtercups.getServer().getPredicateManager().get(predicateLocation);
            return predicate != null && predicate.test(new LootContext.Builder(((ServerLevel)entity.level)).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.THIS_ENTITY, entity).create(LootContextParamSets.SELECTOR));
        }
        return false;
    }

    public ResourceLocation getPredicateLocation() {
        return predicateLocation;
    }
}
