package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class FlowerCowTargetCapabilityAttacher {
    private static class FlowerCowTargetCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public static final ResourceLocation IDENTIFIER = BovinesAndButtercups.asResource("moobloom_target");

        private FlowerCowTargetCapabilityImpl backend;
        private final LazyOptional<FlowerCowTargetCapabilityImpl> optionalData = LazyOptional.of(() -> backend);

        private FlowerCowTargetCapabilityProvider(Bee provider) {
            this.backend = new FlowerCowTargetCapabilityImpl(provider);
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return FlowerCowTargetCapabilityImpl.INSTANCE.orEmpty(cap, this.optionalData);
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
        if (event.getObject() instanceof Bee bee) {
            final FlowerCowTargetCapabilityProvider provider = new FlowerCowTargetCapabilityProvider(bee);

            event.addCapability(FlowerCowTargetCapabilityProvider.IDENTIFIER, provider);
        }
    }
}