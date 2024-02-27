package net.merchantpug.bovinesandbuttercups.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MushroomCowTypeAttachment implements IBovineAttachment {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("mooshroom_type");

    public static final Codec<MushroomCowTypeAttachment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ResourceLocation.CODEC.optionalFieldOf("type").forGetter(MushroomCowTypeAttachment::getTypeKey),
            ResourceLocation.CODEC.optionalFieldOf("previous_type").forGetter(MushroomCowTypeAttachment::getPreviousTypeKey),
            Codec.BOOL.fieldOf("allow_shearing").forGetter(MushroomCowTypeAttachment::shouldAllowShearing)
    ).apply(inst, MushroomCowTypeAttachment::new));


    private Optional<ResourceLocation> typeId;
    private Optional<ResourceLocation> previousTypeId;
    private ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> type;
    private boolean allowShearing;

    public MushroomCowTypeAttachment() {
        this(Optional.empty(), Optional.empty(), true);
    }

    public MushroomCowTypeAttachment(Optional<ResourceLocation> typeId, Optional<ResourceLocation> previousTypeId, boolean allowShearing) {
        this.typeId = typeId;
        this.previousTypeId = previousTypeId;
        this.allowShearing = allowShearing;
    }

    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getType() {
        try {
            if (this.typeId.isPresent()) {
                if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(typeId.get()) && this.type != null && this.type.configuration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(typeId.get(), BovineCowTypes.MUSHROOM_COW_TYPE.get()).configuration()) {
                    this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(typeId.get(), BovineCowTypes.MUSHROOM_COW_TYPE.get());
                    return this.type;
                } else if (this.type != null) {
                    return this.type;
                } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(typeId.get())) {
                    this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(typeId.get(), BovineCowTypes.MUSHROOM_COW_TYPE.get());
                    return this.type;
                }
                BovinesAndButtercups.LOG.warn("Could not find type '{}' from mooshroom. Setting type to 'bovinesandbuttercups:missing_mooshroom'.", this.typeId);
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get());
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get());
            BovinesAndButtercups.LOG.warn("Could not get type '{}' from mooshroom. Setting type to 'bovinesandbuttercups:missing_mooshroom'. {}", this.typeId, e.getMessage());
            return this.type;
        }
    }

    public Optional<ResourceLocation> getTypeKey() {
        return this.typeId;
    }

    public boolean shouldAllowShearing() {
        return allowShearing;
    }

    public void setAllowShearing(boolean value) {
        allowShearing = value;
    }

    public void setType(ResourceLocation key) {
        this.typeId = Optional.ofNullable(key);
        this.getType();
    }

    public Optional<ResourceLocation> getPreviousTypeKey() {
        return this.previousTypeId;
    }

    public void setPreviousTypeKey(@Nullable ResourceLocation key) {
        this.previousTypeId = Optional.ofNullable(key);
    }

    @Override
    public Codec<?> getCodec() {
        return CODEC;
    }
}