package net.merchantpug.bovinesandbuttercups.content.block;

import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class MoobloomFlowerBlock extends FlowerBlock {
    public static final BooleanProperty PERSISTENT = BooleanProperty.create("persistent");

    public MoobloomFlowerBlock(MobEffect effect, int duration, Properties properties) {
        super(effect, duration, properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(PERSISTENT, Boolean.TRUE));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(PERSISTENT) && random.nextInt(2) == 0 && level.getEntitiesOfClass(Bee.class, AABB.ofSize(Vec3.atCenterOf(pos), 2.0, 2.0, 2.0)).isEmpty() && level.getEntitiesOfClass(Player.class, AABB.ofSize(Vec3.atCenterOf(pos), 5.0, 5.0, 5.0)).isEmpty()) {
            level.destroyBlock(pos, false);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PERSISTENT);
        Services.PLATFORM.addMoobloomFlowerBlockProperties(builder);
    }
}