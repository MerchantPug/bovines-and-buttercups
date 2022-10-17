package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends Animal {
    @Shadow public abstract MushroomCow.MushroomType getMushroomType();
    @Shadow @Nullable private UUID lastLightningBoltUUID;

    protected MushroomCowMixin(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;setMushroomType(Lnet/minecraft/world/entity/animal/MushroomCow$MushroomType;)V", shift = At.Shift.AFTER), cancellable = true)
    private void bovinesandbuttercups$changeTypeOnThunderHit(ServerLevel level, LightningBolt bolt, CallbackInfo ci) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (Services.PLATFORM.getPreviousMushroomCowTypeKeyFromCow(cow).isPresent()) {
            Services.PLATFORM.setMushroomCowType(cow, Services.PLATFORM.getPreviousMushroomCowTypeKeyFromCow(cow).get());
            Services.PLATFORM.setPreviousMushroomCowType(cow, null);
        } else if (Services.PLATFORM.getMushroomCowTypeFromCow(cow).getConfiguration().getThunderConversionTypes().isEmpty()) {
            super.thunderHit(level, bolt);
            ci.cancel();
        } else {
            Services.PLATFORM.setPreviousMushroomCowType(cow, Services.PLATFORM.getMushroomCowTypeKeyFromCow(cow));

            List<CowTypeConfiguration.WeightedConfiguredCowType> compatibleList = new ArrayList<>();
            int totalWeight = 0;

            for (CowTypeConfiguration.WeightedConfiguredCowType weightedCowType : Services.PLATFORM.getMushroomCowTypeFromCow(cow).getConfiguration().getThunderConversionTypes().get()) {
                if (weightedCowType.getConfiguredCowType(this.getLevel()).isEmpty() || !(weightedCowType.getConfiguredCowType(this.getLevel()).get().getConfiguration() instanceof MushroomCowConfiguration))
                    continue;

                if (weightedCowType.weight() > 0) {
                    compatibleList.add(weightedCowType);
                }
            }

            if (compatibleList.isEmpty()) {
                super.thunderHit(level, bolt);
                ci.cancel();
            } else if (compatibleList.size() == 1) {
                Services.PLATFORM.setMushroomCowType(cow, compatibleList.get(0).configuredCowType());
            } else {
                for (CowTypeConfiguration.WeightedConfiguredCowType cct : compatibleList) {
                    totalWeight -= cct.weight();
                    if (totalWeight <= 0) {
                        Services.PLATFORM.setMushroomCowType(cow, compatibleList.get(0).configuredCowType());
                        break;
                    }
                }
            }
        }
    }

    @Inject(method = "getEffectFromItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$getEffectFromCustomFlowerStack(ItemStack itemStack, CallbackInfoReturnable<Optional<Pair<MobEffect, Integer>>> cir, Item item) {
        if (item instanceof CustomFlowerItem) {
            cir.setReturnValue(Optional.of(Pair.of(CustomFlowerItem.getSuspiciousStewEffect(this.getLevel(), itemStack), CustomFlowerItem.getSuspiciousStewDuration(this.getLevel(), itemStack))));
        }
    }
}
