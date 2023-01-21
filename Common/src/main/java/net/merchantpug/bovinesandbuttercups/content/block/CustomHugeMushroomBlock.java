package net.merchantpug.bovinesandbuttercups.content.block;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

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
        if (blockEntity instanceof CustomHugeMushroomBlockEntity chmbe && chmbe.getLevel() != null) {
            CompoundTag compound = new CompoundTag();
            if (chmbe.getMushroomType() != null && !chmbe.getMushroomType().equals(MushroomType.MISSING)) {
                compound.putString("Type", BovineRegistryUtil.getMushroomTypeKey(chmbe.getMushroomType()).toString());
                itemStack.getOrCreateTag().put("BlockEntityTag", compound);
            }
        }
        return itemStack;
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        ((CustomHugeMushroomBlockEntity)level.getBlockEntity(pos)).updateState();
        return super.updateShape(state, direction, state2, level, pos, pos2);
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
        return BovineBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK.get().create(pos, state);
    }
}
