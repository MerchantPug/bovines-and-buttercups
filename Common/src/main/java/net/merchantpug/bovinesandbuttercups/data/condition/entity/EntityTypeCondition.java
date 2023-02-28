package net.merchantpug.bovinesandbuttercups.data.condition.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EntityTypeCondition extends ConditionConfiguration<Entity> {
    public static final MapCodec<EntityTypeCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.either(TagKey.hashedCodec(Registries.ENTITY_TYPE), BuiltInRegistries.ENTITY_TYPE.holderByNameCodec()).fieldOf("location").xmap(tagKeyHolderEither ->
                    tagKeyHolderEither.map(BuiltInRegistries.ENTITY_TYPE::getOrCreateTag, entityType ->(HolderSet<EntityType<?>>)HolderSet.direct(entityType)),
                    holders -> holders.unwrap().mapBoth(tagKey -> tagKey, holders1 -> holders1.get(0))).forGetter(EntityTypeCondition::getEntityTypes)
    ).apply(builder, EntityTypeCondition::new));

    private final HolderSet<EntityType<?>> entityTypes;

    public EntityTypeCondition(HolderSet<EntityType<?>> entityTypes) {
        this.entityTypes = entityTypes;
    }

    @Override
    public boolean test(Entity entity) {
        return entityTypes.contains(entity.getType().builtInRegistryHolder());
    }

    public HolderSet<EntityType<?>> getEntityTypes() {
        return entityTypes;
    }
}
