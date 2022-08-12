package com.github.merchantpug.bovinesandbuttercups.block.entity;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomType;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;

public class CustomMushroomBlockEntity extends BlockEntity {
    @Nullable private String mushroomTypeName;

    public CustomMushroomBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(Services.PLATFORM.getCustomMushroomBlockEntity(), worldPosition, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        mushroomTypeName = tag.getString("Type");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Type", Objects.requireNonNullElse(this.mushroomTypeName, "bovinesandbuttercups:missing"));
    }

    @Nullable public String getMushroomTypeName() {
        return mushroomTypeName;
    }

    public void setMushroomTypeName(@Nullable String value) {
        mushroomTypeName = value;
    }

    public MushroomType getMushroomType() {
        try {
            if (mushroomTypeName == null) {
                return MushroomType.MISSING;
            } else if (MushroomTypeRegistry.contains(ResourceLocation.tryParse(mushroomTypeName))) {
                return MushroomType.fromKey(mushroomTypeName);
            } else {
                return MushroomType.MISSING;
            }
        } catch (Exception e) {
            Constants.LOG.warn("Could not load MushroomType at blockpos " + this.getBlockPos().toString() + ": ", e.getMessage());
        }
        return MushroomType.MISSING;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}
