package ca.ulaval.glo4002.application;

import ca.ulaval.glo4002.billing.BillingServer;
import ca.ulaval.glo4002.crm.CrmServer;

public class ApplicationServer
{
    public static void main(String[] args)
    {
        Thread crm = new Thread(new CrmServer(args));
        Thread billing = new Thread(new BillingServer());

        billing.start();
        crm.start();
    }
}