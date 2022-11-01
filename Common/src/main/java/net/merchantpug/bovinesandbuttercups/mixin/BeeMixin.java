package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.entity.goal.MoveToFlowerCowGoal;
import net.merchantpug.bovinesandbuttercups.entity.goal.PollinateFlowerCowGoal;
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

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal implements BeeAccess {
    @Nullable @Unique private PollinateFlowerCowGoal bovinesandbuttercups$pollinateFlowerCowGoal;

    protected BeeMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void bovinesandbuttercups$addMoobloomRelatedGoals(CallbackInfo ci) {
        PollinateFlowerCowGoal pollinateGoal = new PollinateFlowerCowGoal((Bee)(Object)this);
        this.goalSelector.addGoal(4, pollinateGoal);
        this.goalSelector.addGoal(6, new MoveToFlowerCowGoal((Bee)(Object)this));
        this.bovinesandbuttercups$pollinateFlowerCowGoal = pollinateGoal;
    }

    public PollinateFlowerCowGoal bovinesandbuttercups$getPollinateFlowerCowGoal() {
        return this.bovinesandbuttercups$pollinateFlowerCowGoal;
    }
}
