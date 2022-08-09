package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.access.ClientboundUpdateMobEffectPacketAccess;
import com.github.merchantpug.bovinesandbuttercups.access.MobEffectInstanceAccess;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;

@Mixin(ClientboundUpdateMobEffectPacket.class)
public class ClientboundUpdateMobEffectPacketMixin implements ClientboundUpdateMobEffectPacketAccess {
    private HashMap<MobEffect, Integer> bovinesandbuttercups$lockedEffects = new HashMap<>();

    @Inject(method = "<init>(ILnet/minecraft/world/effect/MobEffectInstance;)V", at = @At("TAIL"))
    private void bovinesandbuttercups$initNullifiedEffectsInstance(int entityId, MobEffectInstance effect, CallbackInfo ci) {
        this.bovinesandbuttercups$lockedEffects = ((MobEffectInstanceAccess)effect).bovinesandbuttercups$getLockedEffects();
    }

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At("TAIL"))
    private void bovinesandbuttercups$initNullifiedEffectsBuf(FriendlyByteBuf buf, CallbackInfo ci) {
        HashMap<MobEffect, Integer> lockedEffects = new HashMap<>();
        for (int i = 0; i < buf.readInt(); i++) {
            lockedEffects.put(MobEffect.byId(buf.readInt()), buf.readInt());
        }
        this.bovinesandbuttercups$lockedEffects = lockedEffects;
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void bovinesandbuttercups$writeNullifiedEffects(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeInt(bovinesandbuttercups$lockedEffects.size());
        List<MobEffect> statusEffectList = bovinesandbuttercups$lockedEffects.keySet().stream().toList();
        for (int i = 0; i < bovinesandbuttercups$lockedEffects.size(); i++) {
            MobEffect effect = statusEffectList.get(i);
            buf.writeInt(Registry.MOB_EFFECT.getId(effect));
            buf.writeInt(bovinesandbuttercups$lockedEffects.get(effect));
        }
    }

    public HashMap<MobEffect, Integer> bovinesandbuttercups$getLockedEffects() {
        return bovinesandbuttercups$lockedEffects;
    }
}
