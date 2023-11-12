package net.merchantpug.bovinesandbuttercups.mixin.neoforge;

import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MushroomCow.class)
public interface MushroomCowAccessor {
    @Invoker("setVariant")
    void bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType type);
}