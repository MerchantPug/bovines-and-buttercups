package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineStatesAssociationRegistry;
import net.merchantpug.bovinesandbuttercups.client.resources.BovineBlockstateTypes;
import net.merchantpug.bovinesandbuttercups.content.block.CustomFlowerBlock;
import net.merchantpug.bovinesandbuttercups.content.block.CustomHugeMushroomBlock;
import net.merchantpug.bovinesandbuttercups.content.block.CustomMushroomBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.particle.ModelLocationParticleOption;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow @Final protected RandomSource random;

    @Shadow public abstract double getX();

    @Shadow private EntityDimensions dimensions;

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Shadow public abstract Level level();

    @Shadow public abstract Vec3 getDeltaMovement();

    @Inject(method = "spawnSprintParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getRenderShape()Lnet/minecraft/world/level/block/RenderShape;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void bovinesandbuttercups$spawnCustomFlowerSprintParticle(CallbackInfo ci, BlockPos blockPos, BlockState blockState) {
        if ((blockState.getBlock() instanceof CustomFlowerBlock || blockState.getBlock() instanceof CustomMushroomBlock || blockState.getBlock() instanceof CustomHugeMushroomBlock) && blockState.hasBlockEntity()) {
            Vec3 vec3 = this.getDeltaMovement();
            if (this.level().getBlockEntity(blockPos) instanceof CustomFlowerBlockEntity customFlowerBlockEntity) {
                this.level().addParticle(new ModelLocationParticleOption(BovineStatesAssociationRegistry.getBlock(BovineRegistryUtil.getFlowerTypeKey(customFlowerBlockEntity.getFlowerType()), BovineBlockstateTypes.FLOWER).orElseGet(() -> BovinesAndButtercups.asResource("bovinesandbuttercups/missing_flower")), "bovinesandbuttercups_persistent=true"), this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.dimensions.width, this.getY() + 0.1D, this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.dimensions.width, vec3.x * -4.0D, 1.5D, vec3.z * -4.0D);
            } else if (this.level().getBlockEntity(blockPos) instanceof CustomMushroomBlockEntity customMushroomBlockEntity) {
                this.level().addParticle(new ModelLocationParticleOption(BovineStatesAssociationRegistry.getBlock(BovineRegistryUtil.getMushroomTypeKey(customMushroomBlockEntity.getMushroomType()), BovineBlockstateTypes.MUSHROOM).orElseGet(() -> BovinesAndButtercups.asResource("bovinesandbuttercups/missing_mushroom")), "bovinesandbuttercups_"), this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.dimensions.width, this.getY() + 0.1D, this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.dimensions.width, vec3.x * -4.0D, 1.5D, vec3.z * -4.0D);
            } else if (this.level().getBlockEntity(blockPos) instanceof CustomHugeMushroomBlockEntity customHugeMushroomBlockEntity) {
                this.level().addParticle(new ModelLocationParticleOption(BovineStatesAssociationRegistry.getBlock(BovineRegistryUtil.getMushroomTypeKey(customHugeMushroomBlockEntity.getMushroomType()), BovineBlockstateTypes.MUSHROOM_BLOCK).orElseGet(() -> BovinesAndButtercups.asResource("bovinesandbuttercups/missing_mushroom_block")), "bovinesandbuttercups_down=true,east=true,north=true,south=true,up=true,west=true"), this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.dimensions.width, this.getY() + 0.1D, this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.dimensions.width, vec3.x * -4.0D, 1.5D, vec3.z * -4.0D);
            }
        }
    }
}
