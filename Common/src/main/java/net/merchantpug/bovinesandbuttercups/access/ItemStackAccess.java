package net.merchantpug.bovinesandbuttercups.access;

import net.minecraft.world.level.Level;

public interface ItemStackAccess {
    Level getLevel();
    void setLevel(Level level);
}
