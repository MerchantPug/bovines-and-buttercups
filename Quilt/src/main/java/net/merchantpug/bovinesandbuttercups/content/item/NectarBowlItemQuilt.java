package net.merchantpug.bovinesandbuttercups.content.item;

import io.github.queerbric.inspecio.Inspecio;
import io.github.queerbric.inspecio.InspecioConfig;
import io.github.queerbric.inspecio.tooltip.CompoundTooltipComponent;
import net.merchantpug.bovinesandbuttercups.client.integration.inspecio.LockdownEffectTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.tooltip.api.client.TooltipComponentCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NectarBowlItemQuilt extends NectarBowlItem {
    public NectarBowlItemQuilt(Properties properties) {
        super(properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (QuiltLoader.isModLoaded("inspecio")) {
            List<TooltipComponent> components = new ArrayList<>();
            super.getTooltipImage(stack).ifPresent(components::add);

            InspecioConfig config = Inspecio.getConfig();

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
                return Optional.of(components.get(0));
            } else if (components.size() > 1) {
                var comp = new CompoundTooltipComponent();
                for (var component : components) {
                    ClientTooltipComponent clientComponent = TooltipComponentCallback.EVENT.invoker().getComponent(component);
                    if (component != null) {
                        comp.addComponent(clientComponent);
                    }
                }
                return Optional.of(comp);
            }
        }
        return super.getTooltipImage(stack);
    }
}
