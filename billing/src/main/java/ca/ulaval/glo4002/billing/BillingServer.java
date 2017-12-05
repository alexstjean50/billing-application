package ca.ulaval.glo4002.billing;

import ca.ulaval.glo4002.billing.persistence.manager.ServiceLocatorConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class BillingServer implements Runnable
{
    private static final int PORT = 8181;
    private static final String ROOT_PACKAGE = "ca.ulaval.glo4002.billing";

    public static void main(String[] args)
    {
        new BillingServer().run();
    }

    @Override
    public void run()
    {
        Server server = new Server(PORT);
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        ResourceConfig packageConfig = new ResourceConfig().packages(ROOT_PACKAGE);
        ServletContainer container = new ServletContainer(packageConfig);
        ServletHolder servletHolder = new ServletHolder(container);

        contextHandler.addServlet(servletHolder, "/*");
        try
        {
            ServiceLocatorConfiguration.configure();
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
}