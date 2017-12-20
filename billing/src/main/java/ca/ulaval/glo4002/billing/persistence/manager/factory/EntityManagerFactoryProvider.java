package ca.ulaval.glo4002.billing.persistence.manager.factory;

import javax.persistence.EntityManagerFactory;

public class EntityManagerFactoryProvider
{
    private static EntityManagerFactory instance;

    public static EntityManagerFactory getInstance()
    {
        if (instance == null)
        {
            instance = EntityManagerFactoryConfigurator.initializeEntityManagerFactory();
        }
        return instance;
    }
}
