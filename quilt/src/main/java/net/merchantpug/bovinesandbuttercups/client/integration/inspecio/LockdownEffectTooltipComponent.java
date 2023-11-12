package net.merchantpug.bovinesandbuttercups.client.integration.inspecio;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.mixin.quilt.inspecio.StatusEffectTooltipComponentAccessor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.queerbric.inspecio.tooltip.StatusEffectTooltipComponent;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import org.joml.Matrix4f;

import java.util.List;

public class LockdownEffectTooltipComponent extends StatusEffectTooltipComponent {
    private static final ResourceLocation MYSTERY_TEXTURE = new ResourceLocation("inspecio", "textures/mob_effects/mystery.png");
    private static final ResourceLocation LOCKDOWN_TEXTURE = BovinesAndButtercups.asResource("textures/mob_effect/lockdown.png");

    public LockdownEffectTooltipComponent(List<MobEffectInstance> list, float multiplier) {
        super(list, multiplier);
    }

    public LockdownEffectTooltipComponent() {
        super();
    }

    @Override
    public int getWidth(Font textRenderer) {
        if (((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getHidden()) {
            return 26 + textRenderer.width(((StatusEffectTooltipComponentAccessor) this).bovinesandbuttercups$invokeGetHiddenText());
        } else {
            int max = 64;

            for(int i = 0; i < ((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getList().size(); ++i) {
                MobEffectInstance statusEffectInstance = ((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getList().get(i);
                String statusEffectName = I18n.get(statusEffectInstance.getEffect().getDescriptionId(), new Object[0]);
                Component duration;
                if (statusEffectInstance.getDuration() > 1) {
                    duration = MobEffectUtil.formatDuration(statusEffectInstance, ((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getMultiplier());

                    max = Math.max(max, 26 + textRenderer.width(duration));
                }

                max = Math.max(max, 26 + textRenderer.width(statusEffectName));
            }
            return max;
        }
    }

    @Override
    public int getHeight() {
        return ((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getHidden() ? 30 : 10 + ((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getList().size() * 20;
    }

    public void renderImage(Font textRenderer, int x, int y, GuiGraphics graphics, ItemRenderer itemRenderer) {
        Minecraft client = Minecraft.getInstance();
        MobEffectTextureManager mobEffectTextureManager = client.getMobEffectTextures();
        List<MobEffectInstance> statusEffectList = ((StatusEffectTooltipComponentAccessor) this).bovinesandbuttercups$getList();

        if (((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getHidden()) {
            graphics.blit(MYSTERY_TEXTURE, x, y, 0.0F, 0.0F, 18, 18, 18, 18);
            graphics.blit(LOCKDOWN_TEXTURE, x, y, 0.0F, 0.0F, 18, 18, 18, 18);
        } else if (!statusEffectList.isEmpty()) {
            int lockdownEffectIndex = Minecraft.getInstance().player.tickCount / (160 / statusEffectList.size()) % statusEffectList.size();

            MobEffect statusEffect = statusEffectList.get(lockdownEffectIndex).getEffect();

            TextureAtlasSprite sprite = mobEffectTextureManager.get(statusEffect);
            graphics.blit(x, y, 0, 18, 18, sprite);
            graphics.blit(LOCKDOWN_TEXTURE, x, y, 0.0F, 0.0F, 18, 18, 18, 18);
        }
    }

    public void renderText(Font textRenderer, int x, int y, Matrix4f model, MultiBufferSource.BufferSource immediate) {
        textRenderer.drawInBatch(BovineEffects.LOCKDOWN.get().getDisplayName(), (float)(x + 24), (float)y, 16777215, true, model, immediate, Font.DisplayMode.NORMAL, 0, 15728880);

        if (((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getHidden()) {
            textRenderer.drawInBatch(((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$invokeGetHiddenText(), (float)(x + 24), (float)y + 10, 11184810, true, model, immediate, Font.DisplayMode.NORMAL, 0, 15728880);
            textRenderer.drawInBatch(((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$invokeGetHiddenTime(), (float)(x + 24), (float)(y + 20), 11184810, true, model, immediate, Font.DisplayMode.NORMAL, 0, 15728880);
        } else {
            for (int i = 0; i < ((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getList().size(); ++i) {
                MobEffectInstance mobEffectInstance = ((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getList().get(i);
                String mobEffectName = I18n.get(mobEffectInstance.getEffect().getDescriptionId(), new Object[0]);

                int off = 0;
                if (mobEffectInstance.getDuration() <= 1) {
                    off += 5;
                }

                textRenderer.drawInBatch(mobEffectName, (float)(x + 24), (float)(y + i * 20 + off + 10), 11184810, true, model, immediate, Font.DisplayMode.NORMAL, 0, 15728880);
                Component duration;
                if (mobEffectInstance.getDuration() > 1) {
                    duration = MobEffectUtil.formatDuration(mobEffectInstance, ((StatusEffectTooltipComponentAccessor)this).bovinesandbuttercups$getMultiplier());
                    textRenderer.drawInBatch(duration, (float)(x + 24), (float)(y + i * 20 + 20), 11184810, true, model, immediate, Font.DisplayMode.NORMAL, 0, 15728880);
                }
            }
        }
    }
}
