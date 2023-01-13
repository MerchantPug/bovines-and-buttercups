package net.merchantpug.bovinesandbuttercups.content.block.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypes;
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

public class CustomFlowerBlockEntity extends BlockEntity {
    @Nullable private FlowerType cachedFlowerType;
    @Nullable private String flowerTypeName;

    public CustomFlowerBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovineBlockEntityTypes.CUSTOM_FLOWER.get(), worldPosition, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        setFlowerTypeName(tag.getString("Type"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Type", Objects.requireNonNullElse(this.flowerTypeName, "bovinesandbuttercups:missing_flower"));
    }

    @Nullable public String getFlowerTypeName() {
        return flowerTypeName;
    }

    public void setFlowerTypeName(@Nullable String value) {
        flowerTypeName = value;
        this.getFlowerType();
    }

    public FlowerType getFlowerType() {
        try {
            if (this.getLevel() != null) {
                if (flowerTypeName == null) {
                    return FlowerType.MISSING;
                } else if (cachedFlowerType != BovineRegistryUtil.getFlowerTypeFromKey(this.getLevel(), ResourceLocation.tryParse(flowerTypeName))) {
                    cachedFlowerType = BovineRegistryUtil.getFlowerTypeFromKey(this.getLevel(), ResourceLocation.tryParse(flowerTypeName));
                    return cachedFlowerType;
                } else if (cachedFlowerType != null) {
                    return cachedFlowerType;
                }
            }
        } catch (Exception e) {
            this.cachedFlowerType = FlowerType.MISSING;
            BovinesAndButtercups.LOG.warn("Could not load FlowerType at BlockPos '" + this.getBlockPos().toString() + "': " + e.getMessage());
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
