package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.service.BillService;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainAccountAssembler;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainBillAssembler;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainTransactionAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.ItemRequestAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.validation.RequestValidator;
import ca.ulaval.glo4002.billing.service.dto.response.BillAcceptationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillAcceptationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;
import ca.ulaval.glo4002.billing.service.validator.ProductValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/bills")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BillResource
{
    private final BillService billService;
    private final TransactionService transactionService;

    public BillResource()
    {
        ClientRepository clientRepository = ServiceLocator.getService(ClientRepository.class);
        AccountRepository accountRepository = ServiceLocator.getService(AccountRepository.class);
        ProductRepository productRepository = ServiceLocator.getService(ProductRepository.class);
        BillRepository billRepository = ServiceLocator.getService(BillRepository.class);

        ProductValidator productValidator = new ProductValidator(productRepository);
        ItemRequestAssembler itemRequestAssembler = new ItemRequestAssembler(productValidator);
        BillCreationResponseAssembler billCreationResponseAssembler = new BillCreationResponseAssembler();
        BillAcceptationResponseAssembler billAcceptationResponseAssembler = new BillAcceptationResponseAssembler();
        AccountRetriever accountRetriever = new AccountRetriever(clientRepository, accountRepository, new
                DomainAccountAssembler());
        DomainBillAssembler domainBillAssembler = new DomainBillAssembler(billRepository, itemRequestAssembler);

        TransactionRepository transactionRepository = ServiceLocator.getService(TransactionRepository.class);
        ClockRepository clockRepository = ServiceLocator.getService(ClockRepository.class);
        DomainTransactionAssembler domainTransactionAssembler = new DomainTransactionAssembler(clockRepository,
                transactionRepository);

        this.billService = new BillService(accountRepository, clockRepository, domainBillAssembler,
                billCreationResponseAssembler,
                billAcceptationResponseAssembler, accountRetriever);
        this.transactionService = new TransactionService(transactionRepository, domainTransactionAssembler);
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

        return Response.noContent()
                .build();
    }
}
