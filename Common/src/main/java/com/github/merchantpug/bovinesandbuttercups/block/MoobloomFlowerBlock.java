package com.github.merchantpug.bovinesandbuttercups.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MoobloomFlowerBlock extends FlowerBlock {
    public static final BooleanProperty BUD = BooleanProperty.create("bud");
    protected static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);

    public MoobloomFlowerBlock(MobEffect suspiciousStewEffect, int effectDuration, Properties settings) {
        super(suspiciousStewEffect, effectDuration, settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(BUD, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Vec3 vec3d = state.getOffset(world, pos);
        return state.getValue(BUD) ? Block.box(5.0, 0.0, 5.0, 11.0, 8.0, 11.0).move(vec3d.x, vec3d.y, vec3d.z) : SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BUD);
    }

    @Override
    public MobEffect getSuspiciousStewEffect() {
        /* for (MoobloomEntityType moobloomEntityType : MoobloomEntityTypeRegistry.values()) {
            if (moobloomEntityType.getFlower() != null && moobloomEntityType.getFlower().getBlock().equals(this.asBlock()) && moobloomEntityType.getStatusEffectInstance() != null) {
                return moobloomEntityType.getStatusEffectInstance().getEffect();
            }
        }
         */
        return super.getSuspiciousStewEffect();
    }

    @Override
    public int getEffectDuration() {
        /* for (MoobloomEntityType moobloomEntityType : MoobloomEntityTypeRegistry.values()) {
            if (moobloomEntityType.getFlower() != null && moobloomEntityType.getFlower().getBlock().equals(this.asBlock()) && moobloomEntityType.getStatusEffectInstance() != null) {
                return moobloomEntityType.getStatusEffectInstance().getDuration();
            }
        }
         */
        return super.getEffectDuration();
    }
}
