package net.merchantpug.bovinesandbuttercups.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(FlowerPotBlock.class)
public interface FlowerPotBlockAccessor {
    @Accessor("POTTED_BY_CONTENT")
    static Map<Block, Block> bovinesandbuttercups$getPottedByContent() {
        throw new RuntimeException("");
    }
}
