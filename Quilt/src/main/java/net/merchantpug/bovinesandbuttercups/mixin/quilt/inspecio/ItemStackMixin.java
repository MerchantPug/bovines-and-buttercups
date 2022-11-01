package net.merchantpug.bovinesandbuttercups.mixin.quilt.inspecio;

import net.merchantpug.bovinesandbuttercups.client.integration.inspecio.LockdownEffectTooltipComponent;
import net.merchantpug.bovinesandbuttercups.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import io.github.queerbric.inspecio.Inspecio;
import io.github.queerbric.inspecio.InspecioConfig;
import io.github.queerbric.inspecio.tooltip.CompoundTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.qsl.tooltip.api.client.TooltipComponentCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean is(Item $$0);

    @Inject(method = "getTooltipImage", at = @At("RETURN"), cancellable = true)
    private void bovinesandbuttercups$modifyNectarTooltip(CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        if (this.is(BovineItems.NECTAR_BOWL.get())) {
            List<TooltipComponent> components = new ArrayList<>();
            cir.getReturnValue().ifPresent(components::add);

            InspecioConfig config = Inspecio.getConfig();
            ItemStack stack = (ItemStack)(Object)this;

            if (config.getEffectsConfig().hasPotions()) {
                if (stack.is(Inspecio.HIDDEN_EFFECTS_TAG)) {
                    components.add(new LockdownEffectTooltipComponent());
                } else {
                    CompoundTag tag = stack.getTag();
                    if (tag != null && tag.contains(NectarBowlItem.EFFECTS_KEY, Tag.TAG_LIST)) {
                        List<MobEffectInstance> effects = new ArrayList<>();
                        ListTag effectsTag = tag.getList(NectarBowlItem.EFFECTS_KEY, Tag.TAG_COMPOUND);

                        for (int i = 0; i < effectsTag.size(); ++i) {
                            int duration = 400;
                            CompoundTag effectTag = effectsTag.getCompound(i);
                            if (effectTag.contains(NectarBowlItem.EFFECT_DURATION_KEY, Tag.TAG_INT)) {
                                duration = effectTag.getInt(NectarBowlItem.EFFECT_DURATION_KEY);
                            }

                            MobEffect mobEffect = MobEffect.byId(effectTag.getByte(NectarBowlItem.EFFECT_ID_KEY));
                            if (mobEffect != null) {
                                effects.add(new MobEffectInstance(mobEffect, duration));
                            }
                        }
                        components.add(new LockdownEffectTooltipComponent(effects, 1.0F));
                    }
                }
            }

            if (components.size() == 1) {
                cir.setReturnValue(Optional.of(components.get(0)));
            } else if (components.size() > 1) {
                var comp = new CompoundTooltipComponent();
                for (var component : components) {
                    ClientTooltipComponent clientComponent = TooltipComponentCallback.EVENT.invoker().getComponent(component);
                    if (component != null) {
                        comp.addComponent(clientComponent);
                    }
                }
                cir.setReturnValue(Optional.of(comp));
            }
        }
    }
}
