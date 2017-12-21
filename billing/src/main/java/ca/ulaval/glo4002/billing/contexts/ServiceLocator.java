package ca.ulaval.glo4002.billing.contexts;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator
{
    private static final Map<Class<?>, Object> services = new HashMap<>();

    public static <T> void loadService(Class<T> contract, T service)
    {
        services.put(contract, service);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<? extends T> contract)
    {
        if (!services.containsKey(contract))
        {
            throw new UnableResolveServiceException(contract);
        }

        return (T) services.get(contract);
    }

    private ServiceLocator()
    {
    }
}
