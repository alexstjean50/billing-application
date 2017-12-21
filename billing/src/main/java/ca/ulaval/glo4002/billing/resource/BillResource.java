package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.service.BillService;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.assembler.BillServiceAssembler;
import ca.ulaval.glo4002.billing.service.assembler.TransactionServiceAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.validation.RequestValidator;
import ca.ulaval.glo4002.billing.service.dto.response.BillAcceptationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/bills")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BillResource
{
    private static final String EMPTY_JSON_VALUE = "{}";
    private final BillService billService;
    private final TransactionService transactionService;

    public BillResource()
    {
        this.transactionService = new TransactionServiceAssembler().create();
        this.billService = new BillServiceAssembler().create();
    }

    public BillResource(BillService billService, TransactionService transactionService)
    {
        this.transactionService = transactionService;
        this.billService = billService;
    }

    @POST
    public Response createBill(BillCreationRequest request)
    {
        RequestValidator requestValidator = new RequestValidator<>(request);

        if (!requestValidator.isRequestValid())
        {
            return requestValidator.generateValidationErrorResponse();
        }

        BillCreationResponse response = this.billService.createBill(request);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @Path("/{id}")
    @POST
    public Response acceptBill(@PathParam("id") long billNumber)
    {
        BillAcceptationResponse response = this.billService.acceptBill(billNumber);

        long clientId = this.billService.retrieveRelatedClientId(billNumber);

        BigDecimal billAmount = this.billService.retrieveBillAmount(billNumber);

        this.transactionService.logTransaction(clientId, billAmount, TransactionType.INVOICE);

        return Response.ok()
                .entity(response)
                .build();
    }

    @Path("/{id}")
    @DELETE
    public Response cancelBill(@PathParam("id") long billNumber)
    {
        this.billService.cancelBill(billNumber);

        long clientId = this.billService.retrieveRelatedClientId(billNumber);

        BigDecimal billAmount = this.billService.retrieveBillAmount(billNumber);

        this.transactionService.logTransaction(clientId, billAmount, TransactionType.INVOICE_CANCELLED);

        return Response.accepted()
                .entity(EMPTY_JSON_VALUE)
                .build();
    }
}
