package net.merchantpug.bovinesandbuttercups.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface FlowerCowTargetComponent extends Component {
    @Nullable UUID getMoobloom();
    void setMoobloom(@Nullable UUID moobloom);
}
