package net.merchantpug.bovinesandbuttercups.content.entity;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class FlowerCowFabriclike extends FlowerCow implements Shearable {
    public FlowerCowFabriclike(EntityType<? extends FlowerCow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level.isClientSide) {
                itemStack.hurtAndBreak(1, player, other -> other.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(SoundSource category) {
        List<ItemStack> stacks = this.xplatformShear(category);
        for (ItemStack stack : stacks) {
            this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), stack));
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.xplatformReadyForShearing();
    }
}
