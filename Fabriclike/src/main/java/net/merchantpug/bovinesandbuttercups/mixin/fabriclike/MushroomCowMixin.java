package net.merchantpug.bovinesandbuttercups.mixin.fabriclike;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.component.BovineEntityComponents;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowChildTypeUtil;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MushroomCow.class)
public class MushroomCowMixin {
    @Inject(method = "checkMushroomSpawnRules", at = @At("RETURN"), cancellable = true)
    private static void bovinesandbuttercups$allowSpawning(EntityType<MushroomCow> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) || !levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) && Animal.isBrightEnoughToSpawn(levelAccessor, blockPos) && (MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) > 0 || BovineRegistryUtil.configuredCowTypeStream().filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).noneMatch(cct -> cct.getConfiguration().getSettings().naturalSpawnWeight() > 0)));
    }

    @Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/MushroomCow;", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$setDataDrivenMooshroomOffspringType(ServerLevel serverLevel, AgeableMob ageableMob, CallbackInfoReturnable<MushroomCow> cir, MushroomCow mushroomCow) {
        BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(mushroomCow).setMushroomCowType(MushroomCowChildTypeUtil.chooseMooshroomBabyType((MushroomCow)(Object)this, (MushroomCow)ageableMob, mushroomCow, ((Animal)(Object)this).getLoveCause()));
    }

    @Inject(method = "shear", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
    private void bovinesandbuttercups$cancelItemDroppingIfUnnecessary(SoundSource soundSource, CallbackInfo ci) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().blockState().isEmpty() && Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().mushroomType().isEmpty()) {
            ci.cancel();
        }
    }

    @ModifyArg(method = "shear", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V"))
    private ItemStack bovinesandbuttercups$modifyShearItem(ItemStack stack) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().blockState().isPresent()) {
            return new ItemStack(Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().blockState().get().getBlock());
        } else if (Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().getMushroomType().isPresent()) {
            ItemStack itemStack = new ItemStack(BovineItems.CUSTOM_MUSHROOM.get());
            CompoundTag compound = new CompoundTag();
            compound.putString("Type", BovineRegistryUtil.getMushroomTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().getMushroomType().get()).toString());
            itemStack.getOrCreateTag().put("BlockEntityTag", compound);
            return itemStack;
        }
        return stack;
    }
}
