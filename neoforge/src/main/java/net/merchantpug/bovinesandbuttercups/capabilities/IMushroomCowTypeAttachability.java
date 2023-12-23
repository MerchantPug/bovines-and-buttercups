package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface IMushroomCowTypeAttachability {
    ResourceLocation ID = BovinesAndButtercups.asResource("mooshroom_type");

    ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType();
    ResourceLocation getMushroomCowTypeKey();
    void setMushroomType(ResourceLocation key);

    @Nullable ResourceLocation getPreviousMushroomTypeKey();
    void setPreviousMushroomTypeKey(@Nullable ResourceLocation key);

    boolean shouldAllowShearing();
    void setAllowShearing(boolean value);
}