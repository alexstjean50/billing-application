package ca.ulaval.glo4002.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationListener;

public class CrmServer implements Runnable
{
    private String[] args;
    private ApplicationListener<?> listener;

    public static void main(String[] args) throws Exception
    {
        new CrmServer(args).run();
    }

    public CrmServer(String[] args)
    {
        this.args = args;
    }

    public CrmServer(String[] args, ApplicationListener<?> listener)
    {
        this.args = args;
        this.listener = listener;
    }

    @Override
    public void run()
    {
        SpringApplication app = new SpringApplication(CrmSpringApplication.class);
        if (listener != null)
        {
            app.addListeners(listener);
        }
        app.run(args);
    }
}