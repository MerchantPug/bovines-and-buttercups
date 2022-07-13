package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.registry.BovineCriteriaTriggers;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, BlockPos blockPos, float yaw, GameProfile profile, @Nullable ProfilePublicKey profilePublicKey) {
        super(level, blockPos, yaw, profile, profilePublicKey);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void triggerNearbyEntityCriterion(CallbackInfo ci) {
        BovineCriteriaTriggers.NEARBY_ENTITY.trigger((ServerPlayer)(Object)this);
    }
}
