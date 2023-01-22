package net.merchantpug.bovinesandbuttercups.client.integration.inspecio;

import com.mojang.math.Matrix4f;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.qsl.tooltip.api.ConvertibleTooltipData;

public class SourceMoobloomTooltipComponent implements ConvertibleTooltipData, ClientTooltipComponent {
    private ResourceLocation moobloomLocation;

    public SourceMoobloomTooltipComponent(ResourceLocation location) {
        this.moobloomLocation = location;
    }

    public String getTextString() {
        var moobloom = BovineRegistryUtil.getConfiguredCowTypeFromKey(moobloomLocation, BovineCowTypes.FLOWER_COW_TYPE.get());
        if (!moobloom.equals(BovineRegistryUtil.getDefaultMoobloom(BovineCowTypes.FLOWER_COW_TYPE.get()))) {
            if (moobloom.getConfiguration().getFlower().blockState().isPresent()) {
                return I18n.get(moobloom.getConfiguration().getFlower().blockState().get().getBlock().getDescriptionId());
            } else if (moobloom.getConfiguration().getFlower().flowerType().isPresent()) {
                ResourceLocation location = moobloom.getConfiguration().getFlower().flowerType().get();
                return I18n.get("block." + location.getNamespace() + "." + location.getPath());
            } else {
                return I18n.get("moobloom." + moobloomLocation.getNamespace() + "." + moobloomLocation.getPath() + ".name");
            }
        }
        return I18n.get("moobloom.bovinesandbuttercups.unknown.name");
    }

    @Override
    public int getWidth(Font textRenderer) {
        String moobloomName = I18n.get("bovinesandbuttercups.moobloom.source", getTextString());
        int max = 64;
        max = Math.max(max, 26 + textRenderer.width(moobloomName));
        return max;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    public void renderText(Font textRenderer, int x, int y, Matrix4f model, MultiBufferSource.BufferSource immediate) {
        String moobloomName = I18n.get("bovinesandbuttercups.moobloom.source", getTextString());
        textRenderer.drawInBatch(moobloomName, (float)(x + 24), y, 16755200, true, model, immediate, false, 0, 15728880);
    }

    @Override
    public ClientTooltipComponent toComponent() {
        return this;
    }
}
