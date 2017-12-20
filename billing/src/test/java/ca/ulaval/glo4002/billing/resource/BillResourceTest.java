package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.service.BillService;
import ca.ulaval.glo4002.billing.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BillResourceTest
{
    public static final long SOME_CLIENT_ID = 1L;
    public static final int SOME_BILL_NUMBER = 12;
    public static final BigDecimal SOME_AMOUNT = BigDecimal.TEN;
    @Mock
    private TransactionService transactionService;
    @Mock
    private BillService billService;
    private BillResource billResource;

    @Before
    public void setUp()
    {
        billResource = new BillResource(billService, transactionService);
    }

    @Test
    public void givenASubmission_whenAcceptingBill_thenShouldLogInvoiceTransaction()
    {
        given(this.billService.retrieveBillAmount(SOME_BILL_NUMBER)).willReturn(SOME_AMOUNT);
        given(this.billService.retrieveRelatedClientId(SOME_BILL_NUMBER)).willReturn(SOME_CLIENT_ID);

        billResource.acceptBill(SOME_BILL_NUMBER);

        verify(this.transactionService).logInvoiceAcceptance(SOME_CLIENT_ID, SOME_AMOUNT);
    }
}
