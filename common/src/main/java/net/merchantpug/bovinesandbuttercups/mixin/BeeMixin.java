package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.content.entity.goal.PollinateFlowerCowGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal implements BeeAccess {
    @Nullable @Unique private PollinateFlowerCowGoal bovinesandbuttercups$pollinateFlowerCowGoal;

    protected BeeMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public PollinateFlowerCowGoal bovinesandbuttercups$getPollinateFlowerCowGoal() {
        return this.bovinesandbuttercups$pollinateFlowerCowGoal;
    }

    @Override
    public void bovinesandbuttercups$setPollinateFlowerCowGoal(PollinateFlowerCowGoal goal) {
        this.bovinesandbuttercups$pollinateFlowerCowGoal = goal;
    }
}
