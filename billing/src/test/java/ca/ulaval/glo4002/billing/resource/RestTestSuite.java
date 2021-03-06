package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.BillingServer;
import ca.ulaval.glo4002.billing.contexts.TestContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TransactionResourceRestTest.class})
public class RestTestSuite
{
    public static final int TEST_SERVER_PORT = 9292;
    private static BillingServer billingServer;

    @BeforeClass
    public static void setUpClass()
    {
        billingServer = new BillingServer();
        billingServer.start(TEST_SERVER_PORT, new TestContext());
    }

    @AfterClass
    public static void tearDownClass()
    {
        billingServer.stop();
    }
}
