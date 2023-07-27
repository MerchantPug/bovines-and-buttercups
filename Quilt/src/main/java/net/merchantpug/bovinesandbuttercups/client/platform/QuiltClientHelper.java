package net.merchantpug.bovinesandbuttercups.client.platform;

import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.client.platform.services.IClientHelper;
import net.merchantpug.bovinesandbuttercups.mixin.quilt.client.ModelManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

@AutoService(IClientHelper.class)
public class QuiltClientHelper implements IClientHelper {

    @Override
    public BakedModel getModel(ResourceLocation resourceLocation) {
        ModelManagerAccessor accessor = ((ModelManagerAccessor)Minecraft.getInstance().getModelManager());
        return accessor.getBakedRegistry().getOrDefault(resourceLocation, accessor.getMissingModel());
    }

}
