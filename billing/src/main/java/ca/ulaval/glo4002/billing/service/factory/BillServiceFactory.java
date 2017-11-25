package ca.ulaval.glo4002.billing.service.factory;

import ca.ulaval.glo4002.billing.domain.billing.account.AccountFactory;
import ca.ulaval.glo4002.billing.persistence.manager.ServiceLocator;
import ca.ulaval.glo4002.billing.service.AccountRetriever;
import ca.ulaval.glo4002.billing.service.BillService;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.ItemRequestAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillAcceptationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.filter.BillsFilterFactory;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;

public class BillServiceFactory
{
    public BillService create()
    {
        ClientRepository clientRepository = ServiceLocator.getService(ServiceLocator.CLIENT_REPOSITORY);
        AccountRepository accountRepository = ServiceLocator.getService(ServiceLocator.ACCOUNT_REPOSITORY);
        ProductRepository productRepository = ServiceLocator.getService(ServiceLocator.PRODUCT_REPOSITORY);
        BillRepository billRepository = ServiceLocator.getService(ServiceLocator.BILL_REPOSITORY);
        ItemRequestAssembler itemRequestAssembler = new ItemRequestAssembler();
        BillCreationResponseAssembler billCreationResponseAssembler = new BillCreationResponseAssembler();
        BillAcceptationResponseAssembler billAcceptationResponseAssembler = new BillAcceptationResponseAssembler();
        AccountRetriever accountRetriever = new AccountRetriever(clientRepository, accountRepository, new
                AccountFactory());

        return new BillService(accountRepository, productRepository, billRepository,
                itemRequestAssembler, billCreationResponseAssembler, billAcceptationResponseAssembler,
                accountRetriever);
    }
}
