package ca.ulaval.glo4002.billing.service.assembler.domain;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.ItemRequestAssembler;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DomainBillAssemblerTest
{
    private static final long SOME_CLIENT_ID = 0;
    private static final DueTerm SOME_DUE_TERM = DueTerm.IMMEDIATE;
    private static final DueTerm SOME_OTHER_DUE_TERM = DueTerm.DAYS30;
    private static final Instant SOME_DATE = Instant.now();
    @Mock
    private Client client;
    @Mock
    private ItemRequestAssembler itemRequestAssembler;
    @Mock
    private BillRepository billRepository;
    @Mock
    private ItemRequest itemRequest;
    private DomainBillAssembler domainBillAssembler;

    @Before
    public void initializeDomainBillAssembler()
    {
        this.domainBillAssembler = new DomainBillAssembler(this.billRepository, this.itemRequestAssembler);
    }

    @Test
    public void givenABillWithEmptyDueTerm_whenAssemblingNewBill_thenCreatedBillShouldHaveClientDefaultDueTerm()
    {
        given(this.client.getDefaultTerm()).willReturn(SOME_DUE_TERM);
        Bill bill = this.domainBillAssembler.toBill(createBillCreationRequest(null), this.client);

        assertEquals(SOME_DUE_TERM, bill.getDueTerm());
    }

    @Test
    public void whenCreatingBill_thenAllItemRequestsAreConvertedToItems()
    {
        this.domainBillAssembler.toBill(createBillCreationRequest(SOME_OTHER_DUE_TERM.name()), this.client);

        verify(this.itemRequestAssembler, times(1)).toDomainModel(anyList());
    }

    private BillCreationRequest createBillCreationRequest(String dueTerm)
    {
        return BillCreationRequest.create(SOME_CLIENT_ID, SOME_DATE, dueTerm,
                Collections.nCopies(3, this.itemRequest));
    }
}
