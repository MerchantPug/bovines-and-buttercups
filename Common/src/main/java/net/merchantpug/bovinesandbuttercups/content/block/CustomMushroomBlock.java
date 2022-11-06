package net.merchantpug.bovinesandbuttercups.content.block;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomBlockEntity;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CustomMushroomBlock extends BaseEntityBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 6.0D, 11.0D);

    public CustomMushroomBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        ItemStack itemStack = new ItemStack(this);
        BlockEntity blockEntity = blockGetter.getBlockEntity(blockPos);
        if (blockEntity instanceof CustomMushroomBlockEntity cmbe && cmbe.getLevel() != null) {
            CompoundTag compound = new CompoundTag();
            if (cmbe.getMushroomType() != null && !cmbe.getMushroomType().equals(MushroomType.MISSING)) {
                compound.putString("Type", BovineRegistryUtil.getMushroomTypeKey(cmbe.getLevel(), cmbe.getMushroomType()).toString());
                itemStack.getOrCreateTag().put("BlockEntityTag", compound);
            }
        }
        return itemStack;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(25) == 0) {
            int i = 5;
            int j = 4;

            for(BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (level.getBlockState(blockpos).is(this)) {
                    --i;
                    if (i <= 0) {
                        return;
                    }
                }
            }

            BlockPos blockpos1 = pos.offset(randomSource.nextInt(3) - 1, randomSource.nextInt(2) - randomSource.nextInt(2), randomSource.nextInt(3) - 1);

            for(int k = 0; k < 4; ++k) {
                if (level.isEmptyBlock(blockpos1) && state.canSurvive(level, blockpos1)) {
                    pos = blockpos1;
                }

                blockpos1 = pos.offset(randomSource.nextInt(3) - 1, randomSource.nextInt(2) - randomSource.nextInt(2), randomSource.nextInt(3) - 1);
            }

            if (level.isEmptyBlock(blockpos1) && state.canSurvive(level, blockpos1)) {
                level.setBlock(blockpos1, state, 2);
                ((CustomMushroomBlockEntity)level.getBlockEntity(blockpos1)).setMushroomTypeName(((CustomMushroomBlockEntity)level.getBlockEntity(pos)).getMushroomTypeName());
                level.getBlockEntity(pos).setChanged();
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
            }
        }

    }

    protected boolean mayPlaceOn(BlockState p_54894_, BlockGetter p_54895_, BlockPos p_54896_) {
        return p_54894_.isSolidRender(p_54895_, p_54896_);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        if (belowState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return true;
        } else {
            return level.getRawBrightness(pos, 0) < 13 && this.mayPlaceOn(belowState, level, belowPos);
        }
    }

    public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
        if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof CustomMushroomBlockEntity mushroomBlockEntity) {
            return mushroomBlockEntity.getMushroomType().hugeMushroomStructureList().isPresent();
        }
        return false;
    }

    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos pos, BlockState state) {
        return (double)randomSource.nextFloat() < 0.4D;
    }

    public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos pos, BlockState state) {
        if (state.hasBlockEntity() && level.getBlockEntity(pos) instanceof CustomMushroomBlockEntity mushroomBlockEntity) {
            StructureTemplateManager structureTemplateManager = level.getStructureManager();

            if (mushroomBlockEntity.getMushroomType().hugeMushroomStructureList().isPresent()) {
                ResourceLocation resourceLocation = mushroomBlockEntity.getMushroomType().hugeMushroomStructureList().get().get(Math.abs(level.random.nextInt()) % mushroomBlockEntity.getMushroomType().hugeMushroomStructureList().get().size());
                Optional<StructureTemplate> optional = Optional.empty();
                try {
                    optional = structureTemplateManager.get(resourceLocation);
                } catch (ResourceLocationException ignored) {
                    BovinesAndButtercups.LOG.warn("Could not find structure template at location '" + resourceLocation.toString() + "'. Will skip growing huge mushroom structure.");
                }

                if (optional.isPresent()) {
                    level.removeBlock(pos, false);
                    StructureTemplate template = optional.get();
                    BlockPos centeredPos = new BlockPos(pos.getX() - template.getSize().getX() / 2, pos.getY(), pos.getZ() - template.getSize().getZ() / 2);
                    if (ChunkPos.rangeClosed(new ChunkPos(centeredPos), new ChunkPos(centeredPos.offset(template.getSize()))).allMatch((chunkPos) -> level.isLoaded(chunkPos.getWorldPosition()))) {
                        StructurePlaceSettings placeSettings = new StructurePlaceSettings().setRotationPivot(new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2));
                        if (!template.filterBlocks(centeredPos, placeSettings, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).isEmpty()) {
                            int minX = template.filterBlocks(centeredPos, placeSettings, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).stream().map(info -> info.pos.getX()).min(Integer::compare).orElseThrow();
                            int maxX = template.filterBlocks(centeredPos, placeSettings, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).stream().map(info -> info.pos.getX()).max(Integer::compare).orElseThrow();
                            int minY = template.filterBlocks(centeredPos, placeSettings, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).stream().map(info -> info.pos.getY()).min(Integer::compare).orElseThrow();
                            int maxY = template.filterBlocks(centeredPos, placeSettings, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).stream().map(info -> info.pos.getY()).max(Integer::compare).orElseThrow();
                            int minZ = template.filterBlocks(centeredPos, placeSettings, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).stream().map(info -> info.pos.getZ()).min(Integer::compare).orElseThrow();
                            int maxZ = template.filterBlocks(centeredPos, placeSettings, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).stream().map(info -> info.pos.getZ()).max(Integer::compare).orElseThrow();

                            if (level.getBlockStates(AABB.of(new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ))).allMatch(bs -> bs.isAir() || bs.is(BlockTags.LEAVES))) {
                                template.placeInWorld(level, centeredPos, centeredPos, placeSettings, randomSource, 2);
                                return;
                            }
                        }
                    }

                    level.setBlock(pos, state, Block.UPDATE_ALL);
                    ((CustomMushroomBlockEntity)level.getBlockEntity(pos)).setMushroomTypeName(mushroomBlockEntity.getMushroomTypeName());
                    level.getBlockEntity(pos).setChanged();
                    level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Services.PLATFORM.getCustomMushroomBlockEntity().create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.getFluidState().isEmpty();
    }

    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(blockState, blockGetter, blockPos, pathComputationType);
    }
}
