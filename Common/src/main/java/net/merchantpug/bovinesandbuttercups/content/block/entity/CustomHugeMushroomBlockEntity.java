package net.merchantpug.bovinesandbuttercups.content.block.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.content.block.CustomHugeMushroomBlock;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypes;
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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomHugeMushroomBlockEntity extends BlockEntity {
    @Nullable private MushroomType cachedMushroomType;
    @Nullable private String mushroomTypeName;

    public CustomHugeMushroomBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BovineBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK.get(), worldPosition, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.setMushroomTypeName(tag.getString("Type"));
        if (this.getLevel() != null) {
            this.updateState();
        }
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

    public void updateState() {
        Level level = this.getLevel();
        BlockPos pos = this.getBlockPos();
        BlockState newState = this.getBlockState();

        if (level.getBlockState(pos.above()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.above()) instanceof CustomHugeMushroomBlockEntity && level.getBlockEntity(pos.above()) != null && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.above())).getMushroomTypeName(), this.getMushroomTypeName())) {
            newState = newState.setValue(CustomHugeMushroomBlock.UP, Boolean.FALSE);
        }

        if (level.getBlockState(pos.below()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.below()) instanceof CustomHugeMushroomBlockEntity && level.getBlockEntity(pos.below()) != null && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.below())).getMushroomTypeName(), this.getMushroomTypeName())) {
            newState = newState.setValue(CustomHugeMushroomBlock.DOWN, Boolean.FALSE);
        }

        if (level.getBlockState(pos.west()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.west()) instanceof CustomHugeMushroomBlockEntity && level.getBlockEntity(pos.west()) != null && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.west())).getMushroomTypeName(), this.getMushroomTypeName())) {
            newState = newState.setValue(CustomHugeMushroomBlock.WEST, Boolean.FALSE);
        }

        if (level.getBlockState(pos.north()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.north()) instanceof CustomHugeMushroomBlockEntity && level.getBlockEntity(pos.north()) != null && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.north())).getMushroomTypeName(), this.getMushroomTypeName())) {
            newState = newState.setValue(CustomHugeMushroomBlock.NORTH, Boolean.FALSE);
        }

        if (level.getBlockState(pos.east()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.east()) instanceof CustomHugeMushroomBlockEntity && level.getBlockEntity(pos.east()) != null && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.east())).getMushroomTypeName(), this.getMushroomTypeName())) {
            newState = newState.setValue(CustomHugeMushroomBlock.EAST, Boolean.FALSE);
        }

        if (level.getBlockState(pos.south()).is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos.south()) instanceof CustomHugeMushroomBlockEntity && level.getBlockEntity(pos.south()) != null && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.south())).getMushroomTypeName(), this.getMushroomTypeName())) {
            newState = newState.setValue(CustomHugeMushroomBlock.SOUTH, Boolean.FALSE);
        }

        level.setBlock(pos, newState, 3);
    }
}
