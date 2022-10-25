package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class LockdownEffectCapabilityAttacher {
    private static class LockdownEffectCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        public static final ResourceLocation IDENTIFIER = BovinesAndButtercups.asResource("lockdown");

        private LockdownEffectCapabilityImpl backend;
        private final LazyOptional<LockdownEffectCapabilityImpl> optionalData = LazyOptional.of(() -> backend);

        private LockdownEffectCapabilityProvider(LivingEntity provider) {
            this.backend = new LockdownEffectCapabilityImpl(provider);
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return LockdownEffectCapabilityImpl.INSTANCE.orEmpty(cap, this.optionalData);
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
        if (event.getObject() instanceof LivingEntity living) {
            final LockdownEffectCapabilityProvider provider = new LockdownEffectCapabilityProvider(living);

            event.addCapability(LockdownEffectCapabilityProvider.IDENTIFIER, provider);
        }
    }
}