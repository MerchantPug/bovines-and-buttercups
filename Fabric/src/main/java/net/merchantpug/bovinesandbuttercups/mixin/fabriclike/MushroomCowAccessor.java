package net.merchantpug.bovinesandbuttercups.mixin.fabriclike;

import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MushroomCow.class)
public interface MushroomCowAccessor {
    @Invoker("setMushroomType")
    void bovinesandbuttercups$invokeSetMushroomType(MushroomCow.MushroomType type);
}
