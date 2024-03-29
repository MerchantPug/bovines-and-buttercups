package net.merchantpug.bovinesandbuttercups.content.block.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomMushroomPotBlockEntity extends BlockEntity {
    @Nullable private MushroomType cachedMushroomType;
    @Nullable private String mushroomTypeName;

    public CustomMushroomPotBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovineBlockEntityTypes.POTTED_CUSTOM_MUSHROOM.get(), worldPosition, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        mushroomTypeName = tag.getString("Type");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Type", Objects.requireNonNullElse(this.mushroomTypeName, "bovinesandbuttercups:missing_mushroom"));
    }

    @Nullable public String getMushroomTypeName() {
        return mushroomTypeName;
    }

    public void setMushroomTypeName(@Nullable String value) {
        mushroomTypeName = value;
        this.getMushroomType();
    }

    public MushroomType getMushroomType() {
        try {
            if (this.getLevel() != null) {
                if (mushroomTypeName == null) {
                    return MushroomType.MISSING;
                } else if (cachedMushroomType != BovineRegistryUtil.getMushroomTypeFromKey(ResourceLocation.tryParse(mushroomTypeName))) {
                    cachedMushroomType = BovineRegistryUtil.getMushroomTypeFromKey(ResourceLocation.tryParse(mushroomTypeName));
                    return cachedMushroomType;
                } else if (cachedMushroomType != null) {
                    return cachedMushroomType;
                }
            }
        } catch (Exception e) {
            this.cachedMushroomType = MushroomType.MISSING;
            BovinesAndButtercups.LOG.warn("Could not load MushroomType at BlockPos '" + this.getBlockPos().toString() + "': ", e.getMessage());
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
