package net.merchantpug.bovinesandbuttercups.mixin;

import com.mojang.datafixers.util.Pair;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.WorldDataConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(WorldLoader.class)
public class WorldLoaderMixin {
    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ReloadableServerResources;loadResources(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/core/RegistryAccess$Frozen;Lnet/minecraft/world/flag/FeatureFlagSet;Lnet/minecraft/commands/Commands$CommandSelection;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static <D, R> void bovinesandbuttercups$setRegistryAccessOnWorldStartup(WorldLoader.InitConfig initConfig, WorldLoader.WorldDataSupplier<D> worldDataSupplier, WorldLoader.ResultFactory<D, R> resultFactory, Executor executor, Executor executor2, CallbackInfoReturnable<CompletableFuture<R>> cir, Pair pair, CloseableResourceManager closeableResourceManager, LayeredRegistryAccess layeredRegistryAccess, LayeredRegistryAccess layeredRegistryAccess2, RegistryAccess.Frozen frozen, RegistryAccess.Frozen frozen2, WorldDataConfiguration worldDataConfiguration, WorldLoader.DataLoadOutput dataLoadOutput, LayeredRegistryAccess layeredRegistryAccess3, RegistryAccess.Frozen frozen3) {
        BovinesAndButtercups.setRegistryAccess(frozen3);
    }
}
