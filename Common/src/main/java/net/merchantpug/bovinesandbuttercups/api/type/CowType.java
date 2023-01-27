package net.merchantpug.bovinesandbuttercups.api.type;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class CowType<CTC extends CowTypeConfiguration> {
    public static final Codec<CowType<?>> CODEC = Services.PLATFORM.getCowTypeCodec();

    Function<RegistryAccess, Codec<CTC>> codecFunction;
    private Codec<ConfiguredCowType<CTC, CowType<CTC>>> serverConfiguredCodec;
    private Codec<ConfiguredCowType<CTC, CowType<CTC>>> clientConfiguredCodec;
    private Pair<ResourceLocation, ConfiguredCowType<CTC, CowType<CTC>>> defaultConfiguredCowType;

    public CowType(Function<RegistryAccess, Codec<CTC>> codecFunction) {
        this.codecFunction = codecFunction;
    }

    public Codec<ConfiguredCowType<CTC, CowType<CTC>>> getServerCodec() {
        if (this.serverConfiguredCodec != null) {
            return this.serverConfiguredCodec;
        }
        return this.serverConfiguredCodec = RecordCodecBuilder.create(instance ->
                instance.group(
                        asMapCodec(codecFunction.apply(BovinesAndButtercups.getServer() == null ? BovinesAndButtercups.getInitialRegistryAccess() : BovinesAndButtercups.getServer().registryAccess())).forGetter(ConfiguredCowType::getConfiguration),
                        Codec.INT.optionalFieldOf("loading_priority", 0).forGetter(ConfiguredCowType::getLoadingPriority)
                ).apply(instance, (ctc, integer) -> new ConfiguredCowType<>(this, ctc, integer)));
    }

    public Codec<ConfiguredCowType<CTC, CowType<CTC>>> getClientCodec() {
        if (this.clientConfiguredCodec != null) {
            return clientConfiguredCodec;
        }
        return this.clientConfiguredCodec = RecordCodecBuilder.create(instance ->
                instance.group(
                        asMapCodec(codecFunction.apply(Minecraft.getInstance().level == null ? BovinesAndButtercups.getInitialRegistryAccess() : Minecraft.getInstance().level.registryAccess())).forGetter(ConfiguredCowType::getConfiguration),
                        Codec.INT.optionalFieldOf("loading_priority", 0).forGetter(ConfiguredCowType::getLoadingPriority)
                ).apply(instance, (ctc, integer) -> new ConfiguredCowType<>(this, ctc, integer)));
    }

    public Pair<ResourceLocation, ConfiguredCowType<CTC, CowType<CTC>>> getDefaultCowType() {
        return this.defaultConfiguredCowType;
    }

    /**
     * Sets the default {@link ConfiguredCowType ConfiguredCowType} for this CowType.
     * This value is used as a placeholder for any failed attempts at getting a
     * configured cow type.
     * <p>
     * This value is automatically registered to the {@link net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry registry},
     * whenever it is cleared and is not synced to the client upon data pack load.
     * <p>
     * This must be run after the CowType instance has been initialized in the registry.
     *
     * @param location The ResourceLocation at which to register this object.
     * @param configuredCowType The ConfiguredCowType to register to the registered cowType.
     */
    public void setDefaultConfiguredCowType(ResourceLocation location, ConfiguredCowType<CTC, CowType<CTC>> configuredCowType) {
        if (defaultConfiguredCowType != null)
            BovinesAndButtercups.LOG.error("Tried setting default configured cow type to cow type that already has it.");
        else
            defaultConfiguredCowType = Pair.of(location, configuredCowType);
    }

    public static <C> MapCodec<C> asMapCodec(Codec<C> codec) {
        if (codec instanceof MapCodec.MapCodecCodec) {
            return ((MapCodec.MapCodecCodec<C>) codec).codec();
        }
        return codec.fieldOf("value");
    }
}
