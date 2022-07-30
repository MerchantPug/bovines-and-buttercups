package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlock;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.particle.ModelLocationParticleOption;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineParticleTypes;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "spawnDestroyParticles", at = @At("HEAD"), cancellable = true)
    private void changeParticlesIfCustomBlock(Level level, Player player, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (blockState.getBlock() instanceof CustomFlowerBlock && blockState.hasBlockEntity()) {
            if (!(level.getBlockEntity(blockPos) instanceof CustomFlowerBlockEntity customFlowerBlockEntity)) return;
            if (!blockState.isAir()) {
                SoundType soundtype = blockState.getSoundType();
                level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F, false);
            }
            if (level.isClientSide) {
                VoxelShape voxelShape = blockState.getShape(level, blockPos);
                voxelShape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                    double d1 = Math.min(1.0D, maxX - minX);
                    double d2 = Math.min(1.0D, maxY - minY);
                    double d3 = Math.min(1.0D, maxZ - minZ);
                    int i = Math.max(2, Mth.ceil(d1 / 0.25D));
                    int j = Math.max(2, Mth.ceil(d2 / 0.25D));
                    int k = Math.max(2, Mth.ceil(d3 / 0.25D));

                    for(int l = 0; l < i; ++l) {
                        for(int i1 = 0; i1 < j; ++i1) {
                            for(int j1 = 0; j1 < k; ++j1) {
                                double d4 = ((double)l + 0.5D) / (double)i;
                                double d5 = ((double)i1 + 0.5D) / (double)j;
                                double d6 = ((double)j1 + 0.5D) / (double)k;
                                double d7 = d4 * d1 + minX;
                                double d8 = d5 * d2 + minY;
                                double d9 = d6 * d3 + minZ;
                                ResourceLocation resourceLocation = Constants.resourceLocation("missing_flower");
                                String variant = "bovines";
                                if (customFlowerBlockEntity.getFlowerType().getFlowerModel() != null) {
                                    resourceLocation = customFlowerBlockEntity.getFlowerType().getFlowerModel();
                                    variant = customFlowerBlockEntity.getFlowerType().getFlowerModelVariant();
                                }

                                level.addParticle(new ModelLocationParticleOption(resourceLocation, variant), blockPos.getX() + d7, blockPos.getY() + d8, blockPos.getZ() + d9, d4 - 0.5D, d5 - 0.5D, d6 - 0.5D);
                            }
                        }
                    }
                });
            }
            ci.cancel();
        }
    }
}
