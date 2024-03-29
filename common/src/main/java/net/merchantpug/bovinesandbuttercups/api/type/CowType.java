package net.merchantpug.bovinesandbuttercups.api.type;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.minecraft.resources.ResourceLocation;

public class CowType<CTC extends CowTypeConfiguration> {
    public static final Codec<CowType<?>> CODEC = Services.PLATFORM.getCowTypeCodec();

    private Codec<ConfiguredCowType<CTC, CowType<CTC>>> codec;
    private Pair<ResourceLocation, ConfiguredCowType<CTC, CowType<CTC>>> defaultConfiguredCowType;

    public CowType(Codec<CTC> codec) {
        this.codec = RecordCodecBuilder.create(instance ->
                instance.group(
                        ClassUtil.asMapCodec(codec).forGetter(ConfiguredCowType::configuration),
                        Codec.INT.optionalFieldOf("loading_priority", 0).forGetter(ConfiguredCowType::loadingPriority)
                ).apply(instance, (ctc, integer) -> new ConfiguredCowType<>(this, ctc, integer)));;
    }

    public Codec<ConfiguredCowType<CTC, CowType<CTC>>> getCodec() {
        return codec;
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
}
