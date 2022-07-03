package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowType;
import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.network.MoobloomTypeListPacket;
import com.github.merchantpug.bovinesandbuttercups.network.SyncMoobloomTypePacket;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow @Final private List<ServerPlayer> players;

    @Shadow public abstract MinecraftServer getServer();

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void sendServerTokensToPlayer(Connection connection, ServerPlayer player, CallbackInfo ci) {
        Services.PLATFORM.sendPacketToPlayer(player, new MoobloomTypeListPacket(FlowerCowTypeRegistry.getIdToMoobloomTypes().keySet().toArray(new ResourceLocation[FlowerCowTypeRegistry.size()]), FlowerCowTypeRegistry.getIdToMoobloomTypes().values().toArray(new FlowerCowType[FlowerCowTypeRegistry.size()])));
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void sendServerTokensToPlayersAfterReload(CallbackInfo ci) {
        for (ServerPlayer player : this.players) {
            Services.PLATFORM.sendPacketToPlayer(player, new MoobloomTypeListPacket(FlowerCowTypeRegistry.getIdToMoobloomTypes().keySet().toArray(new ResourceLocation[FlowerCowTypeRegistry.size()]), FlowerCowTypeRegistry.getIdToMoobloomTypes().values().toArray(new FlowerCowType[FlowerCowTypeRegistry.size()])));
        }
        this.getServer().getAllLevels().forEach(level -> {
            level.getEntities(BovineEntityTypes.MOOBLOOM.get(), Entity::isAlive).forEach(flowerCow -> {
                try {
                    ResourceLocation resourceLocation = ResourceLocation.tryParse(flowerCow.getTypeId());
                    if (!FlowerCowTypeRegistry.contains(resourceLocation)) {
                        flowerCow.type = FlowerCowType.MISSING;
                        Services.PLATFORM.sendPacketToAllPlayers(level, new SyncMoobloomTypePacket(flowerCow.getId(), FlowerCowType.MISSING));

                    } else if (!flowerCow.type.equals(FlowerCowTypeRegistry.get(resourceLocation))) {
                        flowerCow.type = FlowerCowTypeRegistry.get(resourceLocation);
                        Services.PLATFORM.sendPacketToAllPlayers(level, new SyncMoobloomTypePacket(flowerCow.getId(), flowerCow.getFlowerCowType()));
                    }
                } catch (Exception e) {
                    flowerCow.type = FlowerCowType.MISSING;
                }
            });
        });
    }
}