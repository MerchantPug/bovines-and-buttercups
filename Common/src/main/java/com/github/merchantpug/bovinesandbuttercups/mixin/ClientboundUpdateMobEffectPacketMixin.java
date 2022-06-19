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
    private HashMap<MobEffect, Integer> bovinesandbuttercups$nullifiedEffects = new HashMap<>();

    @Inject(method = "<init>(ILnet/minecraft/world/effect/MobEffectInstance;)V", at = @At("TAIL"))
    private void initNullifiedEffectsInstance(int entityId, MobEffectInstance effect, CallbackInfo ci) {
        this.bovinesandbuttercups$nullifiedEffects = ((MobEffectInstanceAccess)effect).bovinesandbuttercups$getNullifiedEffects();
    }

    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At("TAIL"))
    private void initNullifiedEffectsBuf(FriendlyByteBuf buf, CallbackInfo ci) {
        HashMap<MobEffect, Integer> nullifiedEffects = new HashMap<>();
        for (int i = 0; i < buf.readInt(); i++) {
            nullifiedEffects.put(MobEffect.byId(buf.readInt()), buf.readInt());
        }
        this.bovinesandbuttercups$nullifiedEffects = nullifiedEffects;
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void writeNullifiedEffects(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeInt(bovinesandbuttercups$nullifiedEffects.size());
        List<MobEffect> statusEffectList = bovinesandbuttercups$nullifiedEffects.keySet().stream().toList();
        for (int i = 0; i < bovinesandbuttercups$nullifiedEffects.size(); i++) {
            MobEffect effect = statusEffectList.get(i);
            buf.writeInt(Registry.MOB_EFFECT.getId(effect));
            buf.writeInt(bovinesandbuttercups$nullifiedEffects.get(effect));
        }
    }

    public HashMap<MobEffect, Integer> bovinesandbuttercups$getNullifiedEffects() {
        return bovinesandbuttercups$nullifiedEffects;
    }
}
