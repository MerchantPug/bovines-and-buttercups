package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
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

import java.util.Optional;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends Animal {
    @Shadow public abstract MushroomCow.MushroomType getMushroomType();

    @Shadow protected abstract void setMushroomType(MushroomCow.MushroomType $$0);

    protected MushroomCowMixin(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void bovinesandbuttercups$backwardsCompatibilityLayer(CompoundTag tag, CallbackInfo ci) {
        MushroomCow cow = (MushroomCow)(Object) this;
        if (BovineRegistryUtil.configuredCowTypeStream(level).filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).allMatch(cct -> ((MushroomCowConfiguration) cct.getConfiguration()).naturalSpawnWeight() == 0)) {
            if (Services.PLATFORM.getMushroomCowTypeFromCow(cow) == null) {
                if (cow.getMushroomType() == MushroomCow.MushroomType.BROWN) {
                    Services.PLATFORM.setMushroomCowType(cow, BovinesAndButtercups.asResource("brown_mushroom"));
                } else if (cow.getMushroomType() == MushroomCow.MushroomType.RED) {
                    Services.PLATFORM.setMushroomCowType(cow, BovinesAndButtercups.asResource("red_mushroom"));
                }
            } else {
                if (BovineRegistryUtil.getConfiguredCowTypeKey(this.level, Services.PLATFORM.getMushroomCowTypeFromCow((MushroomCow)(Object)this)).equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
                    this.setMushroomType(MushroomCow.MushroomType.BROWN);
                } else if (BovineRegistryUtil.getConfiguredCowTypeKey(this.level, Services.PLATFORM.getMushroomCowTypeFromCow((MushroomCow)(Object)this)).equals(BovinesAndButtercups.asResource("red_mushroom"))) {
                    this.setMushroomType(MushroomCow.MushroomType.RED);
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
