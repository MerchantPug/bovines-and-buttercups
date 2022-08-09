package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlock;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.particle.ModelLocationParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> resourceKey, Holder<DimensionType> dimensionType, Supplier<ProfilerFiller> profilerFiller, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, resourceKey, dimensionType, profilerFiller, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Shadow public abstract void addParticle(ParticleOptions p_104706_, double p_104707_, double p_104708_, double p_104709_, double p_104710_, double p_104711_, double p_104712_);

    @Shadow public abstract void playLocalSound(double p_104600_, double p_104601_, double p_104602_, SoundEvent p_104603_, SoundSource p_104604_, float p_104605_, float p_104606_, boolean p_104607_);

    @Inject(method = "addDestroyBlockEffect", at = @At("HEAD"), cancellable = true)
    private void bovinesandbuttercups$changeParticlesIfCustomBlock(BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if (blockState.getBlock() instanceof CustomFlowerBlock && blockState.hasBlockEntity()) {
            if (!(this.getBlockEntity(blockPos) instanceof CustomFlowerBlockEntity customFlowerBlockEntity)) return;
            VoxelShape voxelShape = blockState.getShape(this, blockPos);
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
                            if (customFlowerBlockEntity.getFlowerType().getModelLocation() != null) {
                                resourceLocation = customFlowerBlockEntity.getFlowerType().getModelLocation();
                                variant = customFlowerBlockEntity.getFlowerType().getModelVariant();
                            }
                            this.addParticle(new ModelLocationParticleOption(resourceLocation, variant), blockPos.getX() + d7, blockPos.getY() + d8, blockPos.getZ() + d9, d4 - 0.5D, d5 - 0.5D, d6 - 0.5D);
                        }
                    }
                }
            });
            ci.cancel();
        }
    }
}