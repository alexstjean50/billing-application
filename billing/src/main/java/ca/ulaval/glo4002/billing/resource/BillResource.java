package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.persistence.repository.ClientNotFoundException;
import ca.ulaval.glo4002.billing.service.BillService;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.BillStatusParameter;
import ca.ulaval.glo4002.billing.service.dto.request.DiscountApplicationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.validation.RequestValidator;
import ca.ulaval.glo4002.billing.service.dto.response.BillAcceptationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillResponse;
import ca.ulaval.glo4002.billing.service.factory.BillServiceFactory;
import ca.ulaval.glo4002.billing.service.factory.TransactionServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
        this.transactionService = new TransactionServiceFactory().create();
        this.billService = new BillServiceFactory().create();
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

        this.transactionService.logInvoiceAcceptance(clientId, billAmount);

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

        this.transactionService.logInvoiceCancellation(clientId, billAmount);

        return Response.accepted()
                .entity(EMPTY_JSON_VALUE)
                .build();
    }

    @GET
    public Response getBills(@QueryParam("clientId") Long clientId, @QueryParam("status") BillStatusParameter status)
    {
        Optional<Long> optionalClientId = Optional.ofNullable(clientId);
        try
        {
            List<BillResponse> response = this.billService.getBills(optionalClientId,
                    Optional.ofNullable(status));

            return Response.ok()
                    .entity(response)
                    .build();
        }
        catch (ClientNotFoundException exception)
        {
            throw new InvalidClientIdException(exception, exception.entityKey);
        }
    }

    @Path("/{id}")
    @PUT
    public Response applyDiscountToBill(@PathParam("id") long billNumber, DiscountApplicationRequest request)
    {
        RequestValidator requestValidator = new RequestValidator<>(request);

        if (!requestValidator.isRequestValid())
        {
            return requestValidator.generateValidationErrorResponse();
        }

        this.billService.applyDiscount(billNumber, request);

        long clientId = this.billService.retrieveRelatedClientId(billNumber);

        this.transactionService.logDiscount(clientId, request.amount);

        return Response.ok()
                .entity(EMPTY_JSON_VALUE)
                .build();
    }
}
