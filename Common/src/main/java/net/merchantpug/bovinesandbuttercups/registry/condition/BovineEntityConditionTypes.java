package net.merchantpug.bovinesandbuttercups.registry.condition;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.ConstantConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.NotConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.AndConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.OrConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.data.condition.entity.BlocksInRadiusCondition;
import net.merchantpug.bovinesandbuttercups.data.condition.entity.EntitiesInRadiusCondition;
import net.merchantpug.bovinesandbuttercups.data.condition.entity.EntityTypeCondition;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.merchantpug.bovinesandbuttercups.registry.RegistrationProvider;
import net.merchantpug.bovinesandbuttercups.registry.RegistryObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.function.Supplier;

public class BovineEntityConditionTypes {
    public static final RegistrationProvider<EntityConditionType<?>> ENTITY_CONDITION_TYPE = RegistrationProvider.get(BovineRegistryKeys.ENTITY_CONDITION_TYPE_KEY, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<EntityConditionType<AndConditionConfiguration<Entity>>> AND = register("and", () -> new EntityConditionType<>(AndConditionConfiguration.getCodec(EntityConfiguredCondition.CODEC)));
    public static final RegistryObject<EntityConditionType<ConstantConditionConfiguration<Entity>>> CONSTANT = register("constant", () -> new EntityConditionType<>(ConstantConditionConfiguration.getCodec(EntityConfiguredCondition.CODEC)));
    public static final RegistryObject<EntityConditionType<NotConditionConfiguration<Entity>>> NOT = register("not", () -> new EntityConditionType<>(NotConditionConfiguration.getCodec(EntityConfiguredCondition.CODEC)));
    public static final RegistryObject<EntityConditionType<OrConditionConfiguration<Entity>>> OR = register("or", () -> new EntityConditionType<>(OrConditionConfiguration.getCodec(EntityConfiguredCondition.CODEC)));
    public static final RegistryObject<EntityConditionType<BlocksInRadiusCondition>> BLOCKS_IN_RADIUS = register("blocks_in_radius", () -> new EntityConditionType<>(BlocksInRadiusCondition.CODEC));
    public static final RegistryObject<EntityConditionType<EntitiesInRadiusCondition>> ENTITIES_IN_RADIUS = register("entities_in_radius", () -> new EntityConditionType<>(EntitiesInRadiusCondition.CODEC));
    public static final RegistryObject<EntityConditionType<EntityTypeCondition>> ENTITY_TYPE = register("entity_type", () -> new EntityConditionType<>(EntityTypeCondition.CODEC));

    public static void register() {

    }

    private static <CC extends ConditionConfiguration<Entity>> RegistryObject<EntityConditionType<CC>> register(String name, Supplier<EntityConditionType<CC>> cowType) {
        return ENTITY_CONDITION_TYPE.register(name, cowType);
    }
}
