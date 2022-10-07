package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.component.BovineEntityComponentInitializer;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixinFabriclike extends Animal {
    protected MushroomCowMixinFabriclike(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void bovinesandbuttercups$backwardsCompatibilityLayer(CompoundTag tag, CallbackInfo ci) {
        MushroomCow cow = (MushroomCow)(Object) this;

        if (BovineRegistryUtil.configuredCowTypeStream(level).filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).allMatch(cct -> ((MushroomCowConfiguration) cct.getConfiguration()).naturalSpawnWeight() == 0) && BovineRegistryUtil.getConfiguredCowTypeKey(level, Services.PLATFORM.getMushroomCowTypeFromCow(cow)).equals(BovinesAndButtercups.asResource("missing_mooshroom"))) {
            if (cow.getMushroomType() == MushroomCow.MushroomType.BROWN) {
                BovineEntityComponentInitializer.MUSHROOM_COW_TYPE_COMPONENT.get(cow).setMushroomCowType(BovinesAndButtercups.asResource("brown_mushroom"));
            } else {
                BovineEntityComponentInitializer.MUSHROOM_COW_TYPE_COMPONENT.get(cow).setMushroomCowType(BovinesAndButtercups.asResource("red_mushroom"));
            }
        }
    }
}
