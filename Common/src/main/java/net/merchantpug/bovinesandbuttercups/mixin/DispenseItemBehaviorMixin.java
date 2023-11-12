package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(targets = "net/minecraft/core/dispenser/DispenseItemBehavior$19")
public class DispenseItemBehaviorMixin {
    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BoneMealItem;growCrop(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$executeMoobloomBoneMeal(BlockSource blockSource, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir, Level level, BlockPos pos) {
        List<FlowerCow> flowerCows = level.getEntitiesOfClass(FlowerCow.class, new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), cow -> cow.isAlive() && !cow.isBaby());
        if (!flowerCows.isEmpty()) {
            flowerCows.get(0).spreadFlowers(true);
            cir.setReturnValue(itemStack);
        }
    }
}
