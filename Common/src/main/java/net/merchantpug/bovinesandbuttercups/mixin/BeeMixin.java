package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal implements BeeAccess {
    @Nullable @Unique private UUID bovinesandbuttercups$moobloomUUID;

    protected BeeMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee;hasSavedFlowerPos()Z"))
    private void bovinesandbuttercups$writeCustomDataToNbt(CompoundTag tag, CallbackInfo ci) {
        if (this.bovinesandbuttercups$moobloomUUID != null) {
            tag.putUUID("MoobloomTarget", bovinesandbuttercups$moobloomUUID);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Bee;savedFlowerPos:Lnet/minecraft/core/BlockPos;", ordinal = 0))
    private void bovinesandbuttercups$readMoobloomFromNbt(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("MoobloomTarget")) {
            bovinesandbuttercups$moobloomUUID = tag.getUUID("MoobloomTarget");
        }
    }

    @Nullable public UUID bovinesandbuttercups$getTargetMoobloom() {
        return bovinesandbuttercups$moobloomUUID;
    }

    public void bovinesandbuttercups$setTargetMoobloom(@Nullable UUID value) {
        bovinesandbuttercups$moobloomUUID = value;
    }
}