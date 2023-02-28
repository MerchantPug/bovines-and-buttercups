package net.merchantpug.bovinesandbuttercups.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.minecraft.core.Registry;

public class BovineRegistriesFabric {
    public static final Registry<CowType<?>> COW_TYPE;
    public static final Registry<EntityConditionType<?>> ENTITY_CONDITION_TYPE;
    public static final Registry<BlockConditionType<?>> BLOCK_CONDITION_TYPE;
    public static final Registry<BiomeConditionType<?>> BIOME_CONDITION_TYPE;

    public static void init() {

    }

    static {
        COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<CowType<?>>castClass(CowType.class), BovineRegistryKeys.COW_TYPE_KEY.location()).buildAndRegister();
        ENTITY_CONDITION_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<EntityConditionType<?>>castClass(EntityConditionType.class), BovineRegistryKeys.ENTITY_CONDITION_TYPE_KEY.location()).buildAndRegister();
        BLOCK_CONDITION_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<BlockConditionType<?>>castClass(BlockConditionType.class), BovineRegistryKeys.BLOCK_CONDITION_TYPE_KEY.location()).buildAndRegister();
        BIOME_CONDITION_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<BiomeConditionType<?>>castClass(BiomeConditionType.class), BovineRegistryKeys.BIOME_CONDITION_TYPE_KEY.location()).buildAndRegister();
    }
}
