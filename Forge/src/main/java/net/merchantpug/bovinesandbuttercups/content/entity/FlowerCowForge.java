package net.merchantpug.bovinesandbuttercups.content.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.IForgeShearable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FlowerCowForge extends FlowerCow implements IForgeShearable {
    public FlowerCowForge(EntityType<? extends FlowerCow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isShearable(@NotNull ItemStack item, Level level, BlockPos pos) {
        return xplatformReadyForShearing();
    }

    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos, int fortune) {
        SoundSource soundSource = player != null ? SoundSource.PLAYERS : SoundSource.BLOCKS;
        return this.xplatformShear(soundSource);
    }
}
