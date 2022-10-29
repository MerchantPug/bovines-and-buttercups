package net.merchantpug.bovinesandbuttercups.mixin;

import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Bee.class)
public interface BeeAccessor {
    @Invoker("setHasNectar")
    void bovinesandbuttercups$invokeSetHasNectar(boolean value);

    @Accessor("ticksWithoutNectarSinceExitingHive")
    int bovinesandbuttercups$getTicksWithoutNectarSinceExitingHive();
}
