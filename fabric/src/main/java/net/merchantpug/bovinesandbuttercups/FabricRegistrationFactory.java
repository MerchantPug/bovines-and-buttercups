package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.registry.RegistrationProvider;
import com.google.auto.service.AutoService;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@AutoService(RegistrationProvider.Factory.class)
public class FabricRegistrationFactory  implements RegistrationProvider.Factory {

    @Override
    public <T> RegistrationProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        return new FabricRegistrationFactory.Provider<>(modId, resourceKey);
    }

    @Override
    public <T> RegistrationProvider<T> create(Registry<T> registry, String modId) {
        return new FabricRegistrationFactory.Provider<>(modId, registry);
    }

    private static class Provider<T> implements RegistrationProvider<T> {
        private final String modId;
        private final Registry<T> registry;

        private final Set<Supplier<T>> entries = new HashSet<>();
        private final Set<Supplier<T>> entriesView = Collections.unmodifiableSet(entries);

        @SuppressWarnings({"unchecked"})
        private Provider(String modId, ResourceKey<? extends Registry<T>> key) {
            this.modId = modId;

            final var reg = BuiltInRegistries.REGISTRY.get(key.location());
            if (reg == null) {
                throw new RuntimeException("Registry with name " + key.location() + " was not found!");
            }
            registry = (Registry<T>) reg;
        }

        private Provider(String modId, Registry<T> registry) {
            this.modId = modId;
            this.registry = registry;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends T> Supplier<I> register(String name, Supplier<? extends I> supplier) {
            final var rl = new ResourceLocation(modId, name);
            final var obj = Registry.register(registry, rl, supplier.get());
            entries.add(() -> obj);
            return () -> obj;
        }

        @Override
        public Collection<Supplier<T>> getEntries() {
            return entriesView;
        }

        @Override
        public String getModId() {
            return modId;
        }
    }
}
