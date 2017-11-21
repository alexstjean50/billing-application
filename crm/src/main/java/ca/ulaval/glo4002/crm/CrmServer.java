package ca.ulaval.glo4002.crm;

import org.springframework.boot.SpringApplication;

public class CrmServer implements Runnable
{
    private String[] args;

    public static void main(String[] args) throws Exception
    {
        new CrmServer(args).run();
    }

    public CrmServer(String[] args)
    {
        this.args = args;
    }

    @Override
    public void run()
    {
        SpringApplication.run(CrmSpringApplication.class, args);
    }
}
