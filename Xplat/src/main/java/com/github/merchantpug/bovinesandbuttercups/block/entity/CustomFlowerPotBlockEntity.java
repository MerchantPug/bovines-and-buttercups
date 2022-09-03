package com.github.merchantpug.bovinesandbuttercups.block.entity;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.block.FlowerType;
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

public class CustomFlowerPotBlockEntity extends BlockEntity {
    @Nullable private FlowerType cachedFlowerType;
    @Nullable private String flowerTypeName;

    public CustomFlowerPotBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(Services.PLATFORM.getCustomFlowerPotBlockEntity(), worldPosition, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        flowerTypeName = tag.getString("Type");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Type", Objects.requireNonNullElse(this.flowerTypeName, "bovinesandbuttercups:missing"));
    }

    @Nullable public String getFlowerTypeName() {
        return flowerTypeName;
    }

    public void setFlowerTypeName(@Nullable String value) {
        flowerTypeName = value;
    }

    public FlowerType getFlowerType() {
        try {
            if (flowerTypeName == null) {
                return FlowerType.MISSING;
            } else if (cachedFlowerType != FlowerType.fromKey(this.getLevel(), ResourceLocation.tryParse(flowerTypeName))) {
                cachedFlowerType = FlowerType.fromKey(this.getLevel(), ResourceLocation.tryParse(flowerTypeName));
                return cachedFlowerType;
            } else if (cachedFlowerType != null) {
                return cachedFlowerType;
            }
        } catch (Exception e) {
            this.cachedFlowerType = FlowerType.MISSING;
            Constants.LOG.warn("Could not load FlowerType at BlockPos '" + this.getBlockPos().toString() + "': ", e.getMessage());
        }
        return FlowerType.MISSING;
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
