package net.merchantpug.bovinesandbuttercups.block.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.block.CustomHugeMushroomBlock;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Objects;

public class CustomHugeMushroomBlockEntity extends BlockEntity {
    @Nullable private MushroomType cachedMushroomType;
    @Nullable private String mushroomTypeName;
    private boolean hasChanged;

    public CustomHugeMushroomBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(Services.PLATFORM.getCustomHugeMushroomBlockEntity(), worldPosition, blockState);
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

    public void flagChanged() {
        this.hasChanged = false;
    }

    public MushroomType getMushroomType() {
        try {
            if (this.getLevel() != null) {
                if (mushroomTypeName == null) {
                    return MushroomType.MISSING;
                } else if (cachedMushroomType != BovineRegistryUtil.getMushroomTypeFromKey(this.getLevel(), ResourceLocation.tryParse(mushroomTypeName))) {
                    cachedMushroomType = BovineRegistryUtil.getMushroomTypeFromKey(this.getLevel(), ResourceLocation.tryParse(mushroomTypeName));
                    this.hasChanged = false;
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, CustomHugeMushroomBlockEntity blockEntity) {
        if (!blockEntity.hasChanged) {
            BlockState newState = state;

            if (level.getBlockState(pos.above()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.above()) instanceof CustomHugeMushroomBlockEntity && ((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.above())).getMushroomTypeName().equals(blockEntity.getMushroomTypeName())) {
                newState = newState.setValue(CustomHugeMushroomBlock.UP, Boolean.FALSE);
            }

            if (level.getBlockState(pos.below()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.below()) instanceof CustomHugeMushroomBlockEntity && ((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.below())).getMushroomTypeName().equals(blockEntity.getMushroomTypeName())) {
                newState = newState.setValue(CustomHugeMushroomBlock.DOWN, Boolean.FALSE);
            }

            if (level.getBlockState(pos.west()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.west()) instanceof CustomHugeMushroomBlockEntity && ((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.west())).getMushroomTypeName().equals(blockEntity.getMushroomTypeName())) {
                newState = newState.setValue(CustomHugeMushroomBlock.WEST, Boolean.FALSE);
            }

            if (level.getBlockState(pos.north()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.north()) instanceof CustomHugeMushroomBlockEntity && ((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.north())).getMushroomTypeName().equals(blockEntity.getMushroomTypeName())) {
                newState = newState.setValue(CustomHugeMushroomBlock.NORTH, Boolean.FALSE);
            }

            if (level.getBlockState(pos.east()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.east()) instanceof CustomHugeMushroomBlockEntity && ((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.east())).getMushroomTypeName().equals(blockEntity.getMushroomTypeName())) {
                newState = newState.setValue(CustomHugeMushroomBlock.EAST, Boolean.FALSE);
            }

            if (level.getBlockState(pos.south()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.south()) instanceof CustomHugeMushroomBlockEntity && ((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.south())).getMushroomTypeName().equals(blockEntity.getMushroomTypeName())) {
                newState = newState.setValue(CustomHugeMushroomBlock.SOUTH, Boolean.FALSE);
            }

            level.setBlock(pos, newState, 3);

            blockEntity.hasChanged = true;
        }
    }
}
