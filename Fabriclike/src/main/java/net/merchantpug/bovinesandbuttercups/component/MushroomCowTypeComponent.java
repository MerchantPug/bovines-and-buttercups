package net.merchantpug.bovinesandbuttercups.component;

import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface MushroomCowTypeComponent extends Component {
    ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType();
    ResourceLocation getMushroomCowTypeKey();
    void setMushroomCowType(ResourceLocation key);

    @Nullable ResourceLocation getPreviousMushroomCowTypeKey();
    void setPreviousMushroomCowTypeKey(@Nullable ResourceLocation key);
}
