package net.merchantpug.bovinesandbuttercups.registry.condition;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.ConstantConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.NotConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.AndConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.OrConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.data.condition.entity.*;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.merchantpug.bovinesandbuttercups.registry.RegistrationProvider;
import net.merchantpug.bovinesandbuttercups.registry.RegistryObject;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class BovineEntityConditionTypes {
    public static final RegistrationProvider<EntityConditionType<?>> ENTITY_CONDITION_TYPE = RegistrationProvider.get(BovineRegistryKeys.ENTITY_CONDITION_TYPE_KEY, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<EntityConditionType<AndConditionConfiguration<Entity>>> AND = register("and", () -> new EntityConditionType<>(AndConditionConfiguration.getCodec(EntityConfiguredCondition::getCodec)));
    public static final RegistryObject<EntityConditionType<ConstantConditionConfiguration<Entity>>> CONSTANT = register("constant", () -> new EntityConditionType<>(ConstantConditionConfiguration.getCodec()));
    public static final RegistryObject<EntityConditionType<NotConditionConfiguration<Entity>>> NOT = register("not", () -> new EntityConditionType<>(NotConditionConfiguration.getCodec(EntityConfiguredCondition::getCodec)));
    public static final RegistryObject<EntityConditionType<OrConditionConfiguration<Entity>>> OR = register("or", () -> new EntityConditionType<>(OrConditionConfiguration.getCodec(EntityConfiguredCondition::getCodec)));
    public static final RegistryObject<EntityConditionType<BiomeConditionCondition>> BIOME_CONDITION = register("biome_condition", () -> new EntityConditionType<>(BiomeConditionCondition::getCodec));
    public static final RegistryObject<EntityConditionType<BlocksInRadiusCondition>> BLOCKS_IN_RADIUS = register("blocks_in_radius", () -> new EntityConditionType<>(BlocksInRadiusCondition::getCodec));
    public static final RegistryObject<EntityConditionType<EntitiesInRadiusCondition>> ENTITIES_IN_RADIUS = register("entities_in_radius", () -> new EntityConditionType<>(EntitiesInRadiusCondition::getCodec));
    public static final RegistryObject<EntityConditionType<EntityTypeLocationConditionConfiguration>> ENTITY_TYPE_LOCATION = register("entity_type_location", () -> new EntityConditionType<>(EntityTypeLocationConditionConfiguration::getCodec));
    public static final RegistryObject<EntityConditionType<PredicateCondition>> PREDICATE = register("predicate", () -> new EntityConditionType<>(PredicateCondition::getCodec));

    public static void register() {

    }

    private static <CC extends ConditionConfiguration<Entity>> RegistryObject<EntityConditionType<CC>> register(String name, Supplier<EntityConditionType<CC>> conditionType) {
        return ENTITY_CONDITION_TYPE.register(name, conditionType);
    }
}
