package ca.ulaval.glo4002.billing.persistence.assembler.account;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.strategy.allocation.AllocationStrategy;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.BillAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.payment.PaymentAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AccountEntity;
import ca.ulaval.glo4002.billing.persistence.entity.BillEntity;
import ca.ulaval.glo4002.billing.persistence.entity.PaymentEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AccountAssemblerTest
{
    private static final long SOME_ID = 0;
    private static final Identity SOME_IDENTITY = new Identity(SOME_ID);
    private static final long SOME_ACCOUNT_ID_VALUE = 0;
    private static final long SOME_OTHER_ACCOUNT_ID_VALUE = SOME_ACCOUNT_ID_VALUE + 1;
    private static final long SOME_CLIENT_ID = 0;
    private static final long SOME_OTHER_CLIENT_ID = SOME_CLIENT_ID + 1;
    private static final List<BillEntity> SOME_BILL_ENTITIES = new ArrayList<>();
    private static final List<PaymentEntity> SOME_PAYMENT_ENTITIES = new ArrayList<>();
    private static final Bill SOME_BILL = mock(Bill.class);
    private static final BillEntity SOME_BILL_ENTITY = mock(BillEntity.class);
    private static final Payment SOME_PAYMENT = mock(Payment.class);
    private static final PaymentEntity SOME_PAYMENT_ENTITY = mock(PaymentEntity.class);
    private static final Client SOME_CLIENT = mock(Client.class);
    @Mock
    private AllocationStrategy SOME_ALLOCATION_STRATEGY;
    private AccountAssembler accountAssembler;

    @Before
    public void createAssemblers()
    {
        ClientRepository clientRepository = mock(ClientRepository.class);
        given(clientRepository.findById(anyLong())).willAnswer(invocationOnMock ->
        {
            Client client = mock(Client.class);
            given(client.getId()).willReturn(invocationOnMock.getArgument(0));
            return client;
        });

        this.accountAssembler = new AccountAssembler(clientRepository, mock(PaymentAssembler.class), mock
                (BillAssembler.class));
    }

    @Test
    public void givenSomeAccount_whenConvertedToAccountEntity_thenClientEntityIdMustBeEqualToClientId()
    {
        Account someAccount = Account.create(SOME_IDENTITY, SOME_CLIENT, this.SOME_ALLOCATION_STRATEGY);

        AccountEntity result = this.accountAssembler.toPersistenceModel(someAccount);

        assertEquals(SOME_CLIENT_ID, result.getClientId());
    }

    @Test
    public void givenSomeAccountEntity_whenConvertedToAccount_thenClientIdMustBeEqualToClientEntityId()
    {
        long expectedClientId = SOME_OTHER_CLIENT_ID;
        AccountEntity someAccountEntity = this.createSomeAccountEntity();
        someAccountEntity.setClientId(expectedClientId);

        Account result = this.accountAssembler.toDomainModel(someAccountEntity);

        assertEquals(expectedClientId, result.getClient()
                .getId());
    }

    @Test
    public void givenSomeAccount_whenConvertedToAccountEntity_thenIdMustBeTheIdentityContent()
    {
        long expectedAccountId = SOME_OTHER_ACCOUNT_ID_VALUE;
        Identity someAccountIdentity = new Identity(expectedAccountId);
        Account someAccount = Account.create(someAccountIdentity, SOME_CLIENT, this.SOME_ALLOCATION_STRATEGY);

        AccountEntity result = this.accountAssembler.toPersistenceModel(someAccount);

        assertEquals(expectedAccountId, result.getAccountId());
    }

    @Test
    public void givenSomeAccountEntity_whenConvertedToAccount_thenIdentityMustContainTheId()
    {
        Identity expectedAccountIdentity = new Identity(SOME_OTHER_ACCOUNT_ID_VALUE);
        AccountEntity someAccountEntity = this.createSomeAccountEntity();
        someAccountEntity.setAccountId(SOME_OTHER_ACCOUNT_ID_VALUE);

        Account result = this.accountAssembler.toDomainModel(someAccountEntity);

        assertEquals(expectedAccountIdentity, result.getAccountId());
    }

    private AccountEntity createSomeAccountEntity()
    {
        return new AccountEntity(SOME_ACCOUNT_ID_VALUE, SOME_CLIENT_ID, SOME_BILL_ENTITIES, SOME_PAYMENT_ENTITIES);
    }
}
