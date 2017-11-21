package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.dto.response.TransactionEntryResponse;
import ca.ulaval.glo4002.billing.service.dto.response.TransactionsResponse;
import ca.ulaval.glo4002.billing.service.factory.TransactionServiceFactory;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/ledger/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource
{
    private final TransactionService transactionService;

    public TransactionResource()
    {
        this.transactionService = new TransactionServiceFactory().create();
    }

    @GET
    public TransactionsResponse retrieveTransactions(@QueryParam("startMonth") String startMonth,
                                                     @QueryParam("endMonth") String endMonth,
                                                     @QueryParam("year") Long year)
    {
        startMonth = StringUtils.stripStart(startMonth, "0");
        endMonth = StringUtils.stripStart(endMonth, "0");

        Optional<String> optionalStartMonth = Optional.ofNullable(startMonth);
        Optional<String> optionalEndMonth = Optional.ofNullable(endMonth);
        Optional<Long> optionalYear = Optional.ofNullable(year);

        List<TransactionEntryResponse> transactionEntryResponses = this.transactionService.retrieveTransactions(optionalStartMonth, optionalEndMonth, optionalYear);

        return new TransactionsResponse(transactionEntryResponses);
    }
}
