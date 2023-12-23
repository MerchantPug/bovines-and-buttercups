package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IFlowerCowTargetAttachability {
    ResourceLocation ID = BovinesAndButtercups.asResource("moobloom_target");

    @Nullable UUID getMoobloom();
    void setMoobloom(@Nullable UUID moobloom);
}
