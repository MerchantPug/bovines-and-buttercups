package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class MushroomCowTypeCapabilityAttacher {
    private static class MushroomCowTypeCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public static final ResourceLocation IDENTIFIER = BovinesAndButtercups.asResource("mooshroom_type");

        private MushroomCowTypeCapabilityImpl backend;
        private final LazyOptional<MushroomCowTypeCapabilityImpl> optionalData = LazyOptional.of(() -> backend);

        private MushroomCowTypeCapabilityProvider(MushroomCow provider) {
            this.backend = new MushroomCowTypeCapabilityImpl(provider);
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return MushroomCowTypeCapability.INSTANCE.orEmpty(cap, this.optionalData);
        }

        void invalidate() {
            this.optionalData.invalidate();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof MushroomCow mushroomCow) {
            final MushroomCowTypeCapabilityProvider provider = new MushroomCowTypeCapabilityProvider(mushroomCow);

            event.addCapability(MushroomCowTypeCapabilityProvider.IDENTIFIER, provider);
        }
    }

    private MushroomCowTypeCapabilityAttacher() {
    }
}
