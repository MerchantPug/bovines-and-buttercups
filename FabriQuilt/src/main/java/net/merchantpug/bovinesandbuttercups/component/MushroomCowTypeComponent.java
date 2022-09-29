package net.merchantpug.bovinesandbuttercups.component;

import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.resources.ResourceLocation;

public interface MushroomCowTypeComponent extends Component {
    ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType();
    ResourceLocation getMushroomCowTypeKey();
    void setMushroomCowType(ResourceLocation key);
}
