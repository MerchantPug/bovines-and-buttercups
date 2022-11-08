package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCowForge;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BovineEntityTypesForge {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<EntityType<FlowerCowForge>> MOOBLOOM = ENTITY_TYPES.register("moobloom", () -> EntityType.Builder.of(FlowerCowForge::new, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10).build(BovinesAndButtercups.asResource("moobloom").toString()));

    public static void init() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
