package net.merchantpug.bovinesandbuttercups.mixin.fabric;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercupsFabric;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void bovinesandbuttercups$setServer(CallbackInfo ci) {
        BovinesAndButtercupsFabric.setServer((MinecraftServer)(Object)this);
    }
}
