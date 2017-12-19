package ca.ulaval.glo4002.billing;

import ca.ulaval.glo4002.billing.contexts.Context;
import ca.ulaval.glo4002.billing.contexts.ProdContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.Optional;

public class BillingServer implements Runnable
{
    private static final int PORT = 8181;
    private static final String ROOT_PACKAGE = "ca.ulaval.glo4002.billing";
    private static final String CONTEXT_PROPERTY = "context";
    private static final String DEFAULT_CONTEXT = "prod";

    public static void main(String[] args)
    {
        new BillingServer().run();
    }

    @Override
    public void run()
    {
        Context context = resolveContext(Optional.ofNullable(System.getProperty(CONTEXT_PROPERTY))
                .orElse(DEFAULT_CONTEXT));
        Server server = new Server(PORT);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        ResourceConfig packageConfig = new ResourceConfig().packages(ROOT_PACKAGE);
        ServletContainer container = new ServletContainer(packageConfig);
        ServletHolder servletHolder = new ServletHolder(container);

        contextHandler.addServlet(servletHolder, "/*");
        context.apply();
        try
        {
            server.start();
            server.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            server.destroy();
        }
    }

    private static Context resolveContext(String contextName)
    {
        switch (contextName)
        {
            case "prod":
                return new ProdContext();
            default:
                throw new RuntimeException("Cannot load context " + contextName);
        }
    }
}