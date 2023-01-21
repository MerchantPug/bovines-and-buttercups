package net.merchantpug.bovinesandbuttercups.mixin.fabriclike;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin {
    @Shadow protected abstract boolean isEmpty();

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Ljava/util/Map;getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$useDataDefinedItemOnPot(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir, ItemStack stack, Item item) {
        if (item instanceof CustomFlowerItem) {
            if (this.isEmpty()) {
                level.setBlock(pos, BovineBlocks.POTTED_CUSTOM_FLOWER.get().defaultBlockState(), 3);
                ResourceLocation resourceLocation = BovineRegistryUtil.getFlowerTypeKey(CustomFlowerItem.getFlowerTypeFromTag(stack).orElse(FlowerType.MISSING));
                if (resourceLocation != null) {
                    ((CustomFlowerPotBlockEntity) level.getBlockEntity(pos)).setFlowerTypeName(resourceLocation.toString());
                }
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
                ResourceLocation resourceLocation = BovineRegistryUtil.getMushroomTypeKey(CustomMushroomItem.getMushroomTypeFromTag(stack).orElse(MushroomType.MISSING));
                if (resourceLocation != null) {
                    ((CustomMushroomPotBlockEntity)level.getBlockEntity(pos)).setMushroomTypeName(resourceLocation.toString());
                }
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
