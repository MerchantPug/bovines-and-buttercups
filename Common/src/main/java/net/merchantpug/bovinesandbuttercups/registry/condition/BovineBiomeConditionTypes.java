package net.merchantpug.bovinesandbuttercups.registry.condition;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.AndConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.ConstantConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.NotConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.OrConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.data.condition.biome.BiomeLocationConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.merchantpug.bovinesandbuttercups.registry.RegistrationProvider;
import net.merchantpug.bovinesandbuttercups.registry.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Supplier;

public class BovineBiomeConditionTypes {
    private static final RegistrationProvider<BiomeConditionType<?>> BIOME_CONDITION_TYPE = RegistrationProvider.get(BovineRegistryKeys.BIOME_CONDITION_TYPE_KEY, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<BiomeConditionType<AndConditionConfiguration<Holder<Biome>>>> AND = register("and", () -> new BiomeConditionType<>(AndConditionConfiguration.getCodec(BiomeConfiguredCondition.CODEC)));
    public static final RegistryObject<BiomeConditionType<ConstantConditionConfiguration<Holder<Biome>>>> CONSTANT = register("constant", () -> new BiomeConditionType<>(ConstantConditionConfiguration.getCodec(BiomeConfiguredCondition.CODEC)));
    public static final RegistryObject<BiomeConditionType<NotConditionConfiguration<Holder<Biome>>>> NOT = register("not", () -> new BiomeConditionType<>(NotConditionConfiguration.getCodec(BiomeConfiguredCondition.CODEC)));
    public static final RegistryObject<BiomeConditionType<OrConditionConfiguration<Holder<Biome>>>> OR = register("or", () -> new BiomeConditionType<>(OrConditionConfiguration.getCodec(BiomeConfiguredCondition.CODEC)));
    public static final RegistryObject<BiomeConditionType<BiomeLocationConditionConfiguration>> BIOME_LOCATION = register("biome_location", () -> new BiomeConditionType<>(BiomeLocationConditionConfiguration.CODEC));

    public static void register() {

    }

    private static <CC extends ConditionConfiguration<Holder<Biome>>> RegistryObject<BiomeConditionType<CC>> register(String name, Supplier<BiomeConditionType<CC>> conditionType) {
        return BIOME_CONDITION_TYPE.register(name, conditionType);
    }
}
