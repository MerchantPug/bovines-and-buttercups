package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.api.ICowTypeInstance;
import com.github.merchantpug.bovinesandbuttercups.data.CowTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.network.CowTypeListPacket;
import com.github.merchantpug.bovinesandbuttercups.network.FlowerTypeListPacket;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow public abstract MinecraftServer getServer();

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void bovinesandbuttercups$sendServerTypesToPlayer(Connection connection, ServerPlayer player, CallbackInfo ci) {
        HashMap<ICowTypeInstance, ICowType> cowTypes = new HashMap<>();
        for (ICowTypeInstance cowTypeInstance : CowTypeRegistry.values()) {
            if (cowTypeInstance.equals(cowTypeInstance.getType().getMissingCow())) continue;
            cowTypes.put(cowTypeInstance, cowTypeInstance.getType());
        }
        Services.PLATFORM.sendPacketToPlayer(player, new CowTypeListPacket(cowTypes));
        Services.PLATFORM.sendPacketToPlayer(player, new FlowerTypeListPacket(new ArrayList<>(FlowerTypeRegistry.values())));
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void bovinesandbuttercups$sendServerTypesToPlayers(CallbackInfo ci) {
        HashMap<ICowTypeInstance, ICowType> cowTypes = new HashMap<>();
        for (ICowTypeInstance cowTypeInstance : CowTypeRegistry.values()) {
            if (cowTypeInstance.equals(cowTypeInstance.getType().getMissingCow())) continue;
            cowTypes.put(cowTypeInstance, cowTypeInstance.getType());
        }
        for (ServerLevel level : this.getServer().getAllLevels()) {
            Services.PLATFORM.sendPacketToAllPlayers(level, new CowTypeListPacket(cowTypes));
            Services.PLATFORM.sendPacketToAllPlayers(level, new FlowerTypeListPacket(new ArrayList<>(FlowerTypeRegistry.values())));
        }
    }
}