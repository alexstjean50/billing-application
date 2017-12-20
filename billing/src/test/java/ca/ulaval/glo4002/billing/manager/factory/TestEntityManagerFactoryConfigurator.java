package ca.ulaval.glo4002.billing.manager.factory;

import ca.ulaval.glo4002.billing.persistence.entity.*;
import com.google.common.collect.ImmutableMap;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.hibernate.cfg.AvailableSettings.*;

public class TestEntityManagerFactoryConfigurator
{
    private static final String H2_DRIVER_CLASS = "org.h2.Driver";
    private static final String DATABASE_URL = "jdbc:h2:mem:test";
    private static final String DATABASE_STARTUP_MODE = "create-drop";

    public static EntityManagerFactory initializeEntityManagerFactory()
    {

        return new HibernatePersistenceProvider().createContainerEntityManagerFactory
                (archiverPersistenceUnitInfo(), ImmutableMap.<String, Object>builder().put(DRIVER, H2_DRIVER_CLASS)
                        .put(URL, DATABASE_URL)
                        .put(DIALECT, org.hibernate.dialect.H2Dialect.class)
                        .put(HBM2DDL_AUTO, DATABASE_STARTUP_MODE)
                        .put(SHOW_SQL, false)
                        .put(QUERY_STARTUP_CHECKING, false)
                        .put(GENERATE_STATISTICS, false)
                        .put(USE_REFLECTION_OPTIMIZER, false)
                        .put(USE_SECOND_LEVEL_CACHE, false)
                        .put(USE_QUERY_CACHE, false)
                        .put(USE_STRUCTURED_CACHE, false)
                        .put(STATEMENT_BATCH_SIZE, 20)
                        .build());
    }

    private static PersistenceUnitInfo archiverPersistenceUnitInfo()
    {
        List<String> entityClassNames = retrieveEntityClassNames();

        return new PersistenceUnitInfo()
        {
            @Override
            public String getPersistenceUnitName()
            {
                return "ApplicationPersistenceUnit";
            }

            @Override
            public String getPersistenceProviderClassName()
            {
                return "org.hibernate.jpa.HibernatePersistenceProvider";
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType()
            {
                return PersistenceUnitTransactionType.RESOURCE_LOCAL;
            }

            @Override
            public DataSource getJtaDataSource()
            {
                return null;
            }

            @Override
            public DataSource getNonJtaDataSource()
            {
                return null;
            }

            @Override
            public List<String> getMappingFileNames()
            {
                return Collections.emptyList();
            }

            @Override
            public List<java.net.URL> getJarFileUrls()
            {
                try
                {
                    return Collections.list(this.getClass()
                            .getClassLoader()
                            .getResources(""));
                }
                catch (IOException e)
                {
                    throw new UncheckedIOException(e);
                }
            }

            @Override
            public URL getPersistenceUnitRootUrl()
            {
                return null;
            }

            @Override
            public List<String> getManagedClassNames()
            {
                return entityClassNames;
            }

            @Override
            public boolean excludeUnlistedClasses()
            {
                return false;
            }

            @Override
            public SharedCacheMode getSharedCacheMode()
            {
                return null;
            }

            @Override
            public ValidationMode getValidationMode()
            {
                return null;
            }

            @Override
            public Properties getProperties()
            {
                return new Properties();
            }

            @Override
            public String getPersistenceXMLSchemaVersion()
            {
                return null;
            }

            @Override
            public ClassLoader getClassLoader()
            {
                return null;
            }

            @Override
            public void addTransformer(ClassTransformer transformer)
            {
            }

            @Override
            public ClassLoader getNewTempClassLoader()
            {
                return null;
            }
        };
    }

    private static List<String> retrieveEntityClassNames()
    {
        return Arrays.stream(retrieveEntities())
                .map(Class::getName)
                .collect(Collectors.toList());
    }

    private static Class<?>[] retrieveEntities()
    {
        return new Class<?>[]{
                AccountEntity.class,
                AllocationEntity.class,
                BillEntity.class,
                ItemEntity.class,
                PaymentEntity.class,
                PaymentMethodEntity.class
        };
    }
}
