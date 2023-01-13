package net.merchantpug.bovinesandbuttercups.registry;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.structure.RanchStructure;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class BovineStructureTypes {
    private static final RegistrationProvider<StructureType<?>> STRUCTURE_TYPES = RegistrationProvider.get(Registry.STRUCTURE_TYPES, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<StructureType<RanchStructure>> RANCH = register("ranch", RanchStructure.CODEC);

    public static void register() {

    }

    private static <S extends Structure> RegistryObject<StructureType<S>> register(String name, Codec<S> codec) {
        return STRUCTURE_TYPES.register(name, () -> () -> codec);
    }
}
