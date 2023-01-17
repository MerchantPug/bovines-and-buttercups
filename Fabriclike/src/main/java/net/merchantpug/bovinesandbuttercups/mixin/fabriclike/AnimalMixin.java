package net.merchantpug.bovinesandbuttercups.mixin.fabriclike;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.merchantpug.bovinesandbuttercups.registry.BovineTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public class AnimalMixin {
    @Unique private static EntityType<? extends Animal> bovinesandbuttercups$capturedEntityType;
    @Unique private static LevelAccessor bovinesandbuttercups$capturedLevelAccessor;
    @Unique private static BlockPos bovinesandbuttercups$capturedBlockPos;

    @Inject(method = "checkAnimalSpawnRules", at = @At("RETURN"))
    private static void bovinesandbuttercups$captureValuesForSpawnDenying(EntityType<? extends Animal> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource, CallbackInfoReturnable<Boolean> cir) {
        bovinesandbuttercups$capturedEntityType = entityType;
        bovinesandbuttercups$capturedLevelAccessor = levelAccessor;
        bovinesandbuttercups$capturedBlockPos = blockPos;
    }

    @ModifyReturnValue(method = "checkAnimalSpawnRules", at = @At(value = "RETURN"))
    private static boolean bovinesandbuttercups$denyCowSpawningIfTagged(boolean value) {
        return value && !(bovinesandbuttercups$capturedEntityType == EntityType.COW && bovinesandbuttercups$capturedLevelAccessor.getBiome(bovinesandbuttercups$capturedBlockPos).is(BovineTags.PREVENT_COW_SPAWNS));
    }
}
