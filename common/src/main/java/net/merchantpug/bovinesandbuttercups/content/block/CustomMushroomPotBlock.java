package net.merchantpug.bovinesandbuttercups.content.block;

import com.mojang.serialization.MapCodec;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CustomMushroomPotBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

    public CustomMushroomPotBlock(Properties properties) {
        super(properties);
    }

    // Once data driven blocks are introduced, datapackers should be adding flowers using that.
    @Override
    protected MapCodec<CustomMushroomPotBlock> codec() {
        return Block.simpleCodec(CustomMushroomPotBlock::new);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack handStack = player.getItemInHand(hand);
        Item item = handStack.getItem();
        BlockState blockState = (item instanceof BlockItem blockItem && Services.PLATFORM.getPottedBlockMap().containsKey(blockItem.getBlock()) || item instanceof CustomFlowerItem || item instanceof CustomMushroomItem ? ((BlockItem) item).getBlock() : Blocks.AIR).defaultBlockState();
        boolean isAir = blockState.is(Blocks.AIR);
        if (isAir) {
            ItemStack flowerStack = this.getContent(level, pos);
            if (handStack.isEmpty()) {
                player.setItemInHand(hand, flowerStack);
            } else if (!player.addItem(flowerStack)) {
                player.drop(flowerStack, false);
            }

            level.setBlock(pos, Blocks.FLOWER_POT.defaultBlockState(), 3);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.CONSUME;
        }
    }

    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return this.getContent(level, pos);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    public ItemStack getContent(BlockGetter level, BlockPos pos) {
        ItemStack stack = new ItemStack(BovineItems.CUSTOM_MUSHROOM.get());
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CustomMushroomPotBlockEntity cmpbe) {
            CompoundTag compound = new CompoundTag();
            compound.putString("Type", Objects.requireNonNullElse(cmpbe.getMushroomTypeName(), "bovinesandbuttercups:missing"));
            stack.getOrCreateTag().put("BlockEntityTag", compound);
        }
        return stack;
    }

    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BovineBlockEntityTypes.POTTED_CUSTOM_MUSHROOM.get().create(pos, state);
    }
}
