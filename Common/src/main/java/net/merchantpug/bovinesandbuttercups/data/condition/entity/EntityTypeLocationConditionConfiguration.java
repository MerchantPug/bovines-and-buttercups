package net.merchantpug.bovinesandbuttercups.data.condition.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EntityTypeLocationConditionConfiguration extends ConditionConfiguration<Entity> {
    public static MapCodec<EntityTypeLocationConditionConfiguration> getCodec(RegistryAccess registryAccess) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                Codec.either(TagKey.hashedCodec(Registries.ENTITY_TYPE), BuiltInRegistries.ENTITY_TYPE.holderByNameCodec()).fieldOf("location").xmap(tagKeyBlockEither ->
                                tagKeyBlockEither.map(BuiltInRegistries.ENTITY_TYPE::getOrCreateTag, block -> (HolderSet<EntityType<?>>) HolderSet.direct(block)),
                        holders -> holders.unwrap().mapBoth(tagKey -> tagKey, holders1 -> holders1.get(0))).forGetter(EntityTypeLocationConditionConfiguration::getEntityTypes)
        ).apply(builder, EntityTypeLocationConditionConfiguration::new));
    }

    private final HolderSet<EntityType<?>> entityTypes;

    public EntityTypeLocationConditionConfiguration(HolderSet<EntityType<?>> entityTypes) {
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
