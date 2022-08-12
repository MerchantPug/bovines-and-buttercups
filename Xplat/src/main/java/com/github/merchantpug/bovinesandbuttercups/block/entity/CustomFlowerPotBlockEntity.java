package com.github.merchantpug.bovinesandbuttercups.block.entity;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
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
            } else if (FlowerTypeRegistry.contains(ResourceLocation.tryParse(flowerTypeName))) {
                return FlowerType.fromKey(flowerTypeName);
            } else {
                return FlowerType.MISSING;
            }
        } catch (Exception e) {
            Constants.LOG.warn("Could not load FlowerType at blockpos " + this.getBlockPos().toString() + ": ", e.getMessage());
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
