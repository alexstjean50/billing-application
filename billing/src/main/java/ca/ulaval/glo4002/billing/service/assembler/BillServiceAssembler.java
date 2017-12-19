package ca.ulaval.glo4002.billing.service.assembler;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.service.BillService;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainAccountAssembler;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainBillAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.ItemRequestAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillAcceptationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;
import ca.ulaval.glo4002.billing.service.validator.ProductValidator;

public class BillServiceAssembler
{
    public BillService create()
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

        return new BillService(accountRepository, domainBillAssembler, billCreationResponseAssembler,
                billAcceptationResponseAssembler, accountRetriever);
    }
}
