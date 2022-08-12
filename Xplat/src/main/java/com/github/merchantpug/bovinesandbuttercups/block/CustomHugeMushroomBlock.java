package com.github.merchantpug.bovinesandbuttercups.block;

import com.github.merchantpug.bovinesandbuttercups.block.entity.CustomHugeMushroomBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.block.entity.CustomMushroomBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class CustomHugeMushroomBlock extends BaseEntityBlock {
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;

    public CustomHugeMushroomBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        ItemStack itemStack = new ItemStack(this);
        BlockEntity blockEntity = blockGetter.getBlockEntity(blockPos);
        if (blockEntity instanceof CustomHugeMushroomBlockEntity chmbe) {
            CompoundTag compound = new CompoundTag();
            if (chmbe.getMushroomType() != null) {
                compound.putString("Type", chmbe.getMushroomType().getResourceLocation().toString());
                itemStack.getOrCreateTag().put("BlockEntityTag", compound);
            }
        }
        return itemStack;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        String type = "";
        if (level.getBlockEntity(pos) instanceof CustomHugeMushroomBlockEntity) {
            type = ((CustomHugeMushroomBlockEntity)level.getBlockEntity(pos)).getMushroomTypeName();
        }
        return this.defaultBlockState().setValue(DOWN,  !(level.getBlockEntity(pos.below()) instanceof CustomHugeMushroomBlockEntity && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.below())).getMushroomTypeName(), type))).setValue(UP, !(level.getBlockEntity(pos.above()) instanceof CustomHugeMushroomBlockEntity && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.above())).getMushroomTypeName(), type))).setValue(NORTH, !(level.getBlockEntity(pos.north()) instanceof CustomHugeMushroomBlockEntity && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.north())).getMushroomTypeName(), type))).setValue(EAST, !(level.getBlockEntity(pos.east()) instanceof CustomHugeMushroomBlockEntity && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.east())).getMushroomTypeName(), type))).setValue(SOUTH, !(level.getBlockEntity(pos.south()) instanceof CustomHugeMushroomBlockEntity && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.south())).getMushroomTypeName(), type))).setValue(WEST, !(level.getBlockEntity(pos.west()) instanceof CustomHugeMushroomBlockEntity && Objects.equals(((CustomHugeMushroomBlockEntity) level.getBlockEntity(pos.west())).getMushroomTypeName(), type)));
    }

    public BlockState updateShape(BlockState $$0, Direction $$1, BlockState $$2, LevelAccessor $$3, BlockPos $$4, BlockPos $$5) {
        return $$2.is(this) ? $$0.setValue(PROPERTY_BY_DIRECTION.get($$1), Boolean.FALSE) : super.updateShape($$0, $$1, $$2, $$3, $$4, $$5);
    }

    public BlockState rotate(BlockState $$0, Rotation $$1) {
        return $$0.setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.NORTH)), $$0.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.SOUTH)), $$0.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.EAST)), $$0.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.WEST)), $$0.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.UP)), $$0.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get($$1.rotate(Direction.DOWN)), $$0.getValue(DOWN));
    }

    public BlockState mirror(BlockState $$0, Mirror $$1) {
        return $$0.setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.NORTH)), $$0.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.SOUTH)), $$0.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.EAST)), $$0.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.WEST)), $$0.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.UP)), $$0.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get($$1.mirror(Direction.DOWN)), $$0.getValue(DOWN));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        $$0.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Services.PLATFORM.getCustomHugeMushroomBlockEntity().create(pos, state);
    }
}
