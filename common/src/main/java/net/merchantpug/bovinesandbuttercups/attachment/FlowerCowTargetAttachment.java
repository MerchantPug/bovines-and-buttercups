package net.merchantpug.bovinesandbuttercups.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class FlowerCowTargetAttachment implements IBovineAttachment {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("moobloom_target");

    public static final Codec<FlowerCowTargetAttachment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.CODEC.optionalFieldOf("moobloom_target").forGetter(FlowerCowTargetAttachment::getMoobloomOptional)
    ).apply(inst, FlowerCowTargetAttachment::new));
    private Optional<UUID> moobloomUUID;

    public FlowerCowTargetAttachment() {
        this(Optional.empty());
    }
    public FlowerCowTargetAttachment(Optional<UUID> moobloomUUID) {
        this.moobloomUUID = moobloomUUID;
    }

    public @Nullable UUID getMoobloom() {
        return moobloomUUID.orElse(null);
    }

    public Optional<UUID> getMoobloomOptional() {
        return moobloomUUID;
    }

    public void setMoobloom(@Nullable UUID moobloomUUID) {
        this.moobloomUUID = Optional.ofNullable(moobloomUUID);
    }

    @Override
    public Codec<?> getCodec() {
        return CODEC;
    }
}
