package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.api.ICowTypeInstance;
import com.github.merchantpug.bovinesandbuttercups.data.CowTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.network.CowTypeListPacket;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.apache.commons.lang3.tuple.Triple;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow public abstract MinecraftServer getServer();

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void bovinesandbuttercups$sendServerTypesToPlayer(Connection connection, ServerPlayer player, CallbackInfo ci) {
        List<ICowTypeInstance> cowTypes = new ArrayList<>();
        for (ICowTypeInstance cowTypeInstance : CowTypeRegistry.values()) {
            if (cowTypeInstance.equals(cowTypeInstance.getType().getMissingCow())) continue;
            cowTypes.add(cowTypeInstance);
        }
        Services.PLATFORM.sendPacketToPlayer(player, new CowTypeListPacket(cowTypes));
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void bovinesandbuttercups$sendServerTypesToPlayers(CallbackInfo ci) {
        List<ICowTypeInstance> cowTypes = new ArrayList<>();
        for (ICowTypeInstance cowTypeInstance : CowTypeRegistry.values()) {
            if (cowTypeInstance.equals(cowTypeInstance.getType().getMissingCow())) continue;
            cowTypes.add(cowTypeInstance);
        }
        for (ServerLevel level : this.getServer().getAllLevels()) {
            Services.PLATFORM.sendPacketToAllPlayers(level, new CowTypeListPacket(cowTypes));
        }
    }
}