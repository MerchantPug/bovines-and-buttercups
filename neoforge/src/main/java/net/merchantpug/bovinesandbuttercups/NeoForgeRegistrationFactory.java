package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.registry.RegistrationProvider;
import com.google.auto.service.AutoService;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@AutoService(RegistrationProvider.Factory.class)
public class NeoForgeRegistrationFactory implements RegistrationProvider.Factory {

    @Override
    public <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        final var containerOpt = ModList.get().getModContainerById(modId);
        if (containerOpt.isEmpty())
            throw new NullPointerException("Cannot find mod container for id " + modId);
        final var cont = containerOpt.get();
        if (cont instanceof FMLModContainer fmlModContainer) {
            final var register = DeferredRegister.create(resourceKey, modId);
            register.register(fmlModContainer.getEventBus());
            return new Provider<>(modId, register);
        } else {
            throw new ClassCastException("The container of the mod " + modId + " is not a FML one!");
        }
    }

    private static class Provider<T> implements RegistrationProvider<T> {
        private final String modId;
        private final DeferredRegister<T> registry;

        private final Set<Supplier<T>> entries = new HashSet<>();
        private final Set<Supplier<T>> entriesView = Collections.unmodifiableSet(entries);

        private Provider(String modId, DeferredRegister<T> registry) {
            this.modId = modId;
            this.registry = registry;
        }

        @Override
        public String getModId() {
            return modId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends T> Supplier<I> register(String name, Supplier<? extends I> supplier) {
            final var obj = registry.<I>register(name, supplier);
            entries.add(obj::value);
            return obj;
        }

        @Override
        public Set<Supplier<T>> getEntries() {
            return entriesView;
        }
    }
}