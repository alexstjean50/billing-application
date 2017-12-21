package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.service.PaymentService;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainAccountAssembler;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainPaymentAssembler;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainTransactionAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.PaymentCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;

import javax.validation.Valid;
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
        AccountRepository accountRepository = ServiceLocator.getService(AccountRepository.class);
        PaymentRepository paymentRepository = ServiceLocator.getService(PaymentRepository.class);
        ClientRepository clientRepository = ServiceLocator.getService(ClientRepository.class);
        DomainAccountAssembler domainAccountAssembler = new DomainAccountAssembler();
        DomainPaymentAssembler domainPaymentAssembler = new DomainPaymentAssembler(paymentRepository);
        PaymentCreationResponseAssembler paymentCreationResponseAssembler = new PaymentCreationResponseAssembler();
        AccountRetriever accountRetriever = new AccountRetriever(clientRepository, accountRepository,
                domainAccountAssembler);

        TransactionRepository transactionRepository = ServiceLocator.getService(TransactionRepository.class);
        ClockRepository clockRepository = ServiceLocator.getService(ClockRepository.class);
        DomainTransactionAssembler domainTransactionAssembler = new DomainTransactionAssembler(clockRepository,
                transactionRepository);

        this.paymentService = new PaymentService(accountRepository, domainPaymentAssembler,
                paymentCreationResponseAssembler, accountRetriever, clockRepository);
        this.transactionService = new TransactionService(transactionRepository, domainTransactionAssembler);
    }

    public PaymentResource(PaymentService paymentService, TransactionService transactionService)
    {
        this.paymentService = paymentService;
        this.transactionService = transactionService;
    }

    @POST
    public Response createPayment(@Valid PaymentCreationRequest request)
    {
        PaymentCreationResponse response = this.paymentService.createPayment(request);

        this.transactionService.logTransaction(request.getClientId(), request.getAmount(), TransactionType.PAYMENT);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }
}
