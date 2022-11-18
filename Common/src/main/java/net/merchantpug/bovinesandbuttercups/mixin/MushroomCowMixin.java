package net.merchantpug.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowSpawnUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends EntitySuperMixin {
    @Shadow public abstract MushroomCow.MushroomType getMushroomType();

    @ModifyReturnValue(method = "checkMushroomSpawnRules", at = @At("RETURN"))
    private static boolean bovinesandbuttercups$allowSpawning(boolean original, EntityType<MushroomCow> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return (levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.MOOSHROOMS_SPAWNABLE_ON) || !levelAccessor.getBiome(blockPos).is(Biomes.MUSHROOM_FIELDS) && levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)) && Animal.isBrightEnoughToSpawn(levelAccessor, blockPos) && (MushroomCowSpawnUtil.getTotalSpawnWeight(levelAccessor, blockPos) > 0 || BovineRegistryUtil.configuredCowTypeStream(levelAccessor).filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).noneMatch(cct -> cct.getConfiguration().getNaturalSpawnWeight() > 0) && original);
    }

    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;getMushroomType()Lnet/minecraft/world/entity/animal/MushroomCow$MushroomType;"))
    private MushroomCow.MushroomType bovinesandbuttercups$allowFlowerFeeding(MushroomCow instance) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (this.getMushroomType() == MushroomCow.MushroomType.RED && Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().canEatFlowers()) {
            return MushroomCow.MushroomType.BROWN;
        } else if (this.getMushroomType() == MushroomCow.MushroomType.BROWN && !Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().canEatFlowers()) {
            return MushroomCow.MushroomType.RED;
        }
        return instance.getMushroomType();
    }

    @Inject(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;setMushroomType(Lnet/minecraft/world/entity/animal/MushroomCow$MushroomType;)V", shift = At.Shift.AFTER), cancellable = true)
    private void bovinesandbuttercups$changeTypeOnThunderHit(ServerLevel level, LightningBolt bolt, CallbackInfo ci) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (Services.COMPONENT.getPreviousMushroomCowTypeKeyFromCow(cow).isPresent()) {
            Services.COMPONENT.setMushroomCowType(cow, Services.COMPONENT.getPreviousMushroomCowTypeKeyFromCow(cow).get());
            Services.COMPONENT.setPreviousMushroomCowType(cow, null);
        } else if (Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().getThunderConversionTypes().isEmpty()) {
            this.bovinesandbuttercups$thunderHit(level, bolt);
            ci.cancel();
        } else {
            Services.COMPONENT.setPreviousMushroomCowType(cow, Services.COMPONENT.getMushroomCowTypeKeyFromCow(cow));

            List<CowTypeConfiguration.WeightedConfiguredCowType> compatibleList = new ArrayList<>();
            int totalWeight = 0;

            for (CowTypeConfiguration.WeightedConfiguredCowType weightedCowType : Services.COMPONENT.getMushroomCowTypeFromCow(cow).getConfiguration().getThunderConversionTypes().get()) {
                if (weightedCowType.getConfiguredCowType(level).isEmpty()) {
                    BovinesAndButtercups.LOG.warn("Lightning struck mooshroom at {} tried to get thunder conversion type '{}' that does not exist. (Skipping).", this.position(), weightedCowType.configuredCowTypeResource());
                    continue;
                } else if (!(weightedCowType.getConfiguredCowType(level).get().getConfiguration() instanceof MushroomCowConfiguration)) {
                    BovinesAndButtercups.LOG.warn("Lightning struck mooshroom at {} tried to get thunder conversion type '{}' that is not a mooshroom type. (Skipping).", this.position(), weightedCowType.configuredCowTypeResource());
                    continue;
                }

                if (weightedCowType.weight() > 0) {
                    compatibleList.add(weightedCowType);
                }
            }

            if (compatibleList.isEmpty()) {
                this.bovinesandbuttercups$thunderHit(level, bolt);
                ci.cancel();
            } else if (compatibleList.size() == 1) {
                Services.COMPONENT.setMushroomCowType(cow, compatibleList.get(0).configuredCowTypeResource());
            } else {
                for (CowTypeConfiguration.WeightedConfiguredCowType cct : compatibleList) {
                    totalWeight -= cct.weight();
                    if (totalWeight <= 0) {
                        Services.COMPONENT.setMushroomCowType(cow, compatibleList.get(0).configuredCowTypeResource());
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "getEffectFromItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$getEffectFromCustomFlowerStack(ItemStack itemStack, CallbackInfoReturnable<Optional<Pair<MobEffect, Integer>>> cir, Item item) {
        if (item instanceof CustomFlowerItem) {
            cir.setReturnValue(Optional.of(Pair.of(CustomFlowerItem.getSuspiciousStewEffect(((Entity)(Object)this).getLevel(), itemStack), CustomFlowerItem.getSuspiciousStewDuration(((Entity)(Object)this).getLevel(), itemStack))));
        }
    }
}
