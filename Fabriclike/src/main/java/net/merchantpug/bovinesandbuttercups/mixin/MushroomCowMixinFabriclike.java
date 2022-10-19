package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCow.class)
public class MushroomCowMixinFabriclike extends Cow {
    public MushroomCowMixinFabriclike(EntityType<? extends MushroomCow> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "shear", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
    private void bovinesandbuttercups$cancelItemDroppingIfUnnecessary(SoundSource soundSource, CallbackInfo ci) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (Services.PLATFORM.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().blockState().isEmpty() && Services.PLATFORM.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().mushroomType().isEmpty()) {
            ci.cancel();
        }
    }

    @ModifyArg(method = "shear", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V"))
    private ItemStack bovinesandbuttercups$modifyShearItem(ItemStack stack) {
        MushroomCow cow = (MushroomCow)(Object)this;
        if (Services.PLATFORM.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().blockState().isPresent()) {
            return new ItemStack(Services.PLATFORM.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().blockState().get().getBlock());
        } else if (Services.PLATFORM.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().getMushroomType(this.getLevel()).isPresent()) {
            ItemStack itemStack = new ItemStack(Services.PLATFORM.getCustomMushroomItem());
            CompoundTag compound = new CompoundTag();
            compound.putString("Type", BovineRegistryUtil.getMushroomTypeKey(this.getLevel(), Services.PLATFORM.getMushroomCowTypeFromCow(cow).getConfiguration().getMushroom().getMushroomType(this.getLevel()).get()).toString());
            itemStack.getOrCreateTag().put("BlockEntityTag", compound);
            return itemStack;
        }
        return stack;
    }
}
