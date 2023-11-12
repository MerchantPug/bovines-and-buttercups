package net.merchantpug.bovinesandbuttercups.client.platform;

import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.client.platform.services.IClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

@AutoService(IClientHelper.class)
public class ForgeClientHelper implements IClientHelper {

    @Override
    public BakedModel getModel(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().getModelManager().getModel(resourceLocation);
    }

}
