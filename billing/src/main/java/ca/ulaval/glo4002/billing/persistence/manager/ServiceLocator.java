package ca.ulaval.glo4002.billing.persistence.manager;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator
{
    private final Map<Class<?>, Object> services;
    private static ServiceLocator instance;

    public ServiceLocator()
    {
        this.services = new HashMap<>();
    }

    public static void load(ServiceLocator locator)
    {
        instance = locator;
    }

    public static <T> T getService(Class<T> key)
    {
        Object requestedRepository = instance.services.get(key);
        return key.cast(requestedRepository);
    }

    public void loadService(Class key, Object service)
    {
        this.services.put(key, service);
    }
}
