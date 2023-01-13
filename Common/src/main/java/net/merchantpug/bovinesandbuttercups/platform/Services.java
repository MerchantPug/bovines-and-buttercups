package net.merchantpug.bovinesandbuttercups.platform;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.platform.services.IComponentHelper;
import net.merchantpug.bovinesandbuttercups.platform.services.IDataHelper;
import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import net.merchantpug.bovinesandbuttercups.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class Services {

    public static final IComponentHelper COMPONENT = load(IComponentHelper.class);
    public static final IDataHelper DATA = load(IDataHelper.class);
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        BovinesAndButtercups.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
