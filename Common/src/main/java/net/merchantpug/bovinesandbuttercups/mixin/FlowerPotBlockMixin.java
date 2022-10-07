package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.block.entity.CustomFlowerPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.block.entity.CustomMushroomPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin {
    @Shadow protected abstract boolean isEmpty();

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Ljava/util/Map;getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$useDataDefinedItemOnPot(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir, ItemStack stack, Item item) {
        if (item instanceof CustomFlowerItem) {
            if (this.isEmpty()) {
                level.setBlock(pos, BovineBlocks.POTTED_CUSTOM_FLOWER.get().defaultBlockState(), 3);
                ((CustomFlowerPotBlockEntity)level.getBlockEntity(pos)).setFlowerTypeName(BovineRegistryUtil.getFlowerTypeKey(level, Objects.requireNonNullElse(CustomFlowerItem.getFlowerTypeFromTag(level, stack).get(), FlowerType.MISSING)).toString());
                level.getBlockEntity(pos).setChanged();
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                player.awardStat(Stats.POT_FLOWER);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            } else {
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        } else if (item instanceof CustomMushroomItem) {
            if (this.isEmpty()) {
                level.setBlock(pos, BovineBlocks.POTTED_CUSTOM_MUSHROOM.get().defaultBlockState(), 3);
                ((CustomMushroomPotBlockEntity)level.getBlockEntity(pos)).setMushroomTypeName(BovineRegistryUtil.getMushroomTypeKey(level, Objects.requireNonNullElse(CustomMushroomItem.getMushroomTypeFromTag(level, stack).get(), MushroomType.MISSING)).toString());
                level.getBlockEntity(pos).setChanged();
                level.sendBlockUpdated(pos, state, level.getBlockState(pos), Block.UPDATE_ALL);
                player.awardStat(Stats.POT_FLOWER);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            } else {
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
    }
}
