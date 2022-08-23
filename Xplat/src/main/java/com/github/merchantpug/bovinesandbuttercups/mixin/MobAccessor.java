package com.github.merchantpug.bovinesandbuttercups.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mob.class)
public interface MobAccessor {
    @Accessor("navigation")
    PathNavigation bovinesandbuttercups$getNavigation();

    @Invoker("rotlerp")
    float bovinesandbuttercups$getRotLerp(float rot, float rot2, float max);
}
