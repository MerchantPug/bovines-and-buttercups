package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import com.github.merchantpug.bovinesandbuttercups.access.ClientboundUpdateMobEffectPacketAccess;
import com.github.merchantpug.bovinesandbuttercups.access.MobEffectInstanceAccess;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleUpdateMobEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;forceAddEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addNullifiedStatusEffectsToEntity(ClientboundUpdateMobEffectPacket packet, CallbackInfo ci, Entity entity, MobEffect mobEffect, MobEffectInstance mobEffectInstance) {
        ((MobEffectInstanceAccess)mobEffectInstance).bovinesandbuttercups$setNullifiedEffects(((ClientboundUpdateMobEffectPacketAccess)packet).bovinesandbuttercups$getNullifiedEffects());
    }
}
