package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RegistryAccess.class)
public interface RegistryAccessMixin {
    @Inject(method = "method_30531", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void bovinesandbuttercups$addDynamicRegistries(CallbackInfoReturnable<ImmutableMap> cir, ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistryAccess.RegistryData<?>> builder) {
        builder.put(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, new RegistryAccess.RegistryData<>(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, ConfiguredCowType.CODEC, ConfiguredCowType.CODEC));
        builder.put(BovineRegistryKeys.FLOWER_TYPE_KEY, new RegistryAccess.RegistryData<>(BovineRegistryKeys.FLOWER_TYPE_KEY, FlowerType.CODEC.codec(), FlowerType.CODEC.codec()));
        builder.put(BovineRegistryKeys.MUSHROOM_TYPE_KEY, new RegistryAccess.RegistryData<>(BovineRegistryKeys.MUSHROOM_TYPE_KEY, MushroomType.CODEC.codec(), MushroomType.CODEC.codec()));
    }
}
