package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.service.PaymentService;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.validation.RequestValidator;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import ca.ulaval.glo4002.billing.service.factory.PaymentServiceFactory;
import ca.ulaval.glo4002.billing.service.factory.TransactionServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource
{
    private final PaymentService paymentService;
    private final TransactionService transactionService;

    public PaymentResource()
    {
        this.transactionService = new TransactionServiceFactory().create();
        this.paymentService = new PaymentServiceFactory().create();
    }

    @POST
    public Response createPayment(PaymentCreationRequest request)
    {
        RequestValidator requestValidator = new RequestValidator<>(request);

        if (!requestValidator.isRequestValid())
        {
            return requestValidator.generateValidationErrorResponse();
        }

        PaymentCreationResponse response = this.paymentService.createPayment(request);

        this.transactionService.logPayment(request.clientId, request.amount);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }
}
