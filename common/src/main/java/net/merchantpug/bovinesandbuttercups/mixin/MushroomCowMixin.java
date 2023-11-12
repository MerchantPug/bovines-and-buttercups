package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
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
    @Shadow public abstract MushroomCow.MushroomType getVariant();

    @Redirect(method = "mobInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;getVariant()Lnet/minecraft/world/entity/animal/MushroomCow$MushroomType;"))
    private MushroomCow.MushroomType bovinesandbuttercups$allowFlowerFeeding(MushroomCow instance) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (this.getVariant() == MushroomCow.MushroomType.RED && Services.COMPONENT.getMushroomCowTypeFromCow(cow).configuration().canEatFlowers()) {
            return MushroomCow.MushroomType.BROWN;
        } else if (this.getVariant() == MushroomCow.MushroomType.BROWN && !Services.COMPONENT.getMushroomCowTypeFromCow(cow).configuration().canEatFlowers()) {
            return MushroomCow.MushroomType.RED;
        }
        return instance.getVariant();
    }

    @Inject(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/MushroomCow;setVariant(Lnet/minecraft/world/entity/animal/MushroomCow$MushroomType;)V", shift = At.Shift.AFTER), cancellable = true)
    private void bovinesandbuttercups$changeTypeOnThunderHit(ServerLevel level, LightningBolt bolt, CallbackInfo ci) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (Services.COMPONENT.getPreviousMushroomCowTypeKeyFromCow(cow).isPresent()) {
            Services.COMPONENT.setMushroomCowType(cow, Services.COMPONENT.getPreviousMushroomCowTypeKeyFromCow(cow).get());
            Services.COMPONENT.setPreviousMushroomCowType(cow, null);
        } else if (Services.COMPONENT.getMushroomCowTypeFromCow(cow).configuration().getSettings().thunderConverts().isEmpty()) {
            this.bovinesandbuttercups$thunderHit(level, bolt);
            ci.cancel();
        } else {
            Services.COMPONENT.setPreviousMushroomCowType(cow, Services.COMPONENT.getMushroomCowTypeKeyFromCow(cow));

            List<CowTypeConfiguration.WeightedConfiguredCowType> compatibleList = new ArrayList<>();
            int totalWeight = 0;

            for (CowTypeConfiguration.WeightedConfiguredCowType weightedCowType : Services.COMPONENT.getMushroomCowTypeFromCow(cow).configuration().getSettings().thunderConverts().get()) {
                if (weightedCowType.getConfiguredCowType().isEmpty()) {
                    BovinesAndButtercups.LOG.warn("Lightning struck mooshroom at {} tried to get thunder conversion type '{}' that does not exist. (Skipping).", this.position(), weightedCowType.configuredCowTypeResource());
                    continue;
                } else if (!(weightedCowType.getConfiguredCowType().get().configuration() instanceof MushroomCowConfiguration)) {
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

    @Inject(method = "getEffectsFromItemStack", at = @At(value = "HEAD", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$getEffectFromCustomFlowerStack(ItemStack stack, CallbackInfoReturnable<Optional<List<SuspiciousEffectHolder.EffectEntry>>> cir) {
        if (stack.getItem() instanceof CustomFlowerItem) {
            cir.setReturnValue(Optional.of(List.of(new SuspiciousEffectHolder.EffectEntry(CustomFlowerItem.getSuspiciousStewEffect(stack), CustomFlowerItem.getSuspiciousStewDuration(stack)))));
        }
    }
}
