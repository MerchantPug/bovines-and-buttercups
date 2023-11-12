package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.IForgeRegistry;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class BovineRegistriesForge {
    public static final DeferredRegister<CowType<?>> COW_TYPE = DeferredRegister.create(BovineRegistryKeys.COW_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<CowType<?>>> COW_TYPE_REGISTRY = COW_TYPE.makeRegistry(() -> new RegistryBuilder<CowType<?>>().disableSaving().hasTags());

    public static final DeferredRegister<EntityConditionType<?>> ENTITY_CONDITION_TYPE = DeferredRegister.create(BovineRegistryKeys.ENTITY_CONDITION_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<EntityConditionType<?>>> ENTITY_CONDITION_TYPE_REGISTRY = ENTITY_CONDITION_TYPE.makeRegistry(() -> new RegistryBuilder<EntityConditionType<?>>().disableSaving().hasTags());

    public static final DeferredRegister<BlockConditionType<?>> BLOCK_CONDITION_TYPE = DeferredRegister.create(BovineRegistryKeys.BLOCK_CONDITION_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<BlockConditionType<?>>> BLOCK_CONDITION_TYPE_REGISTRY = BLOCK_CONDITION_TYPE.makeRegistry(() -> new RegistryBuilder<BlockConditionType<?>>().disableSaving().hasTags());

    public static final DeferredRegister<BiomeConditionType<?>> BIOME_CONDITION_TYPE = DeferredRegister.create(BovineRegistryKeys.BIOME_CONDITION_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<BiomeConditionType<?>>> BIOME_CONDITION_TYPE_REGISTRY = BIOME_CONDITION_TYPE.makeRegistry(() -> new RegistryBuilder<BiomeConditionType<?>>().disableSaving().hasTags());


    public static void init(IEventBus eventBus) {
        COW_TYPE.register(eventBus);
        ENTITY_CONDITION_TYPE.register(eventBus);
        BLOCK_CONDITION_TYPE.register(eventBus);
        BIOME_CONDITION_TYPE.register(eventBus);
    }
}