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
    private Server server;

    public static void main(String[] args)
    {
        new BillingServer().run();
    }

    @Override
    public void run()
    {
        Context context = resolveContext(Optional.ofNullable(System.getProperty(CONTEXT_PROPERTY))
                .orElse(DEFAULT_CONTEXT));
        start(PORT, context);
        join();
    }

    public void start(int port, Context context)
    {
        context.apply();
        this.server = new Server(port);
        ServletContextHandler contextHandler = new ServletContextHandler(this.server, "/");
        ResourceConfig packageConfig = new ResourceConfig().packages(ROOT_PACKAGE);
        ServletContainer container = new ServletContainer(packageConfig);
        ServletHolder servletHolder = new ServletHolder(container);

        contextHandler.addServlet(servletHolder, "/*");

        try
        {
            this.server.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            tryStopServer();
        }
    }

    private void tryStopServer()
    {
        try
        {
            server.destroy();
        }
        catch (Exception e)
        {
            return;
        }
    }

    private void join()
    {
        try
        {
            this.server.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        if (this.server != null)
        {
            try
            {
                this.server.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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