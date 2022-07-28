package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import com.github.merchantpug.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.minecraft.client.resources.ClientPackSource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ClientPackSource.class)
public class ClientPackSourceMixin {
    @Inject(method = "loadPacks", at = @At(value = "TAIL"))
    private void loadResourcePacks(Consumer<Pack> consumer, Pack.PackConstructor constructor, CallbackInfo ci) {
        Pack bovinesMojangPack = BovinesAndButtercupsClient.createMojangMoobloomPack(constructor);
        if (bovinesMojangPack != null) {
            consumer.accept(bovinesMojangPack);
        }

        Pack noGrassPack = BovinesAndButtercupsClient.createNoGrassPack(constructor);
        if (noGrassPack != null) {
            consumer.accept(noGrassPack);
        }
    }
}