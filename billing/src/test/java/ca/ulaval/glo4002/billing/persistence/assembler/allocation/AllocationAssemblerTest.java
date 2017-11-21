package ca.ulaval.glo4002.billing.persistence.assembler.allocation;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.persistence.assembler.payment.PaymentAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AllocationEntity;
import ca.ulaval.glo4002.billing.persistence.entity.PaymentEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class AllocationAssemblerTest
{
    private static final long SOME_ID = 0;
    private static final long SOME_OTHER_ID = SOME_ID + 1;
    private static final Instant SOME_DATE = Instant.now();
    private static final Identity SOME_IDENTITY = new Identity(SOME_ID);
    private static final Money SOME_AMOUNT = Money.valueOf(1232);
    private static final PaymentEntity SOME_PAYMENT_ENTITY = mock(PaymentEntity.class);
    private static final long SOME_BILL_NUMBER = 0;
    private static final Payment SOME_PAYMENT = mock(Payment.class);
    private static final Money SOME_OTHER_AMOUNT = Money.valueOf(1);
    private static final Instant SOME_OTHER_INSTANT = Instant.ofEpochMilli(23);
    private AllocationAssembler allocationAssembler;

    @Before
    public void initializeAllocationAssembler()
    {
        PaymentAssembler somePaymentAssembler = mock(PaymentAssembler.class);
        given(somePaymentAssembler.toDomainModel(notNull())).willReturn(SOME_PAYMENT);
        given(somePaymentAssembler.toPersistenceModel(notNull())).willReturn(SOME_PAYMENT_ENTITY);

        this.allocationAssembler = new AllocationAssembler();
    }

    @Test
    public void givenAnAllocation_whenConvertedToAllocationEntity_thenAllocationEntityIdShouldBeEqualToAllocationId()
    {
        long expectedAllocationEntityId = SOME_OTHER_ID;
        Identity someOtherIdentity = new Identity(expectedAllocationEntityId);
        Allocation someAllocation = new Allocation(someOtherIdentity, SOME_BILL_NUMBER,
                SOME_AMOUNT, SOME_DATE);

        AllocationEntity result = this.allocationAssembler.toPersistenceModel(someAllocation);

        assertEquals(expectedAllocationEntityId, result.getAllocationId());
    }

    @Test
    public void givenAnAllocationEntity_whenConvertedToAllocation_thenAllocationEntityIdShouldBeEqualToAllocationId()
    {
        Identity expectedAllocationIdentity = new Identity(SOME_OTHER_ID);
        AllocationEntity someAllocationEntity = createSomeAllocationEntity();
        someAllocationEntity.setAllocationId(SOME_OTHER_ID);

        Allocation result = this.allocationAssembler.toDomainModel(someAllocationEntity);

        assertEquals(expectedAllocationIdentity, result.getAllocationId());
    }

    @Test
    public void givenAnAllocation_whenConvertedToAllocationEntity_thenAllocatedAmountsShouldBeEqual()
    {
        Money expectedAllocatedAmount = SOME_OTHER_AMOUNT;
        Allocation someAllocation = new Allocation(SOME_IDENTITY, SOME_BILL_NUMBER,
                expectedAllocatedAmount, SOME_DATE);

        AllocationEntity result = this.allocationAssembler.toPersistenceModel(someAllocation);

        assertEquals(expectedAllocatedAmount.asBigDecimal(), result.getAllocatedAmount());
    }

    @Test
    public void givenAnAllocationEntity_whenConvertedToAllocation_thenAllocatedAmountsShouldBeEqual()
    {
        Money expectedAllocatedAmount = SOME_OTHER_AMOUNT;
        AllocationEntity someAllocationEntity = createSomeAllocationEntity();
        someAllocationEntity.setAllocatedAmount(expectedAllocatedAmount.asBigDecimal());

        Allocation result = this.allocationAssembler.toDomainModel(someAllocationEntity);

        assertEquals(expectedAllocatedAmount, result.getAllocatedAmount());
    }

    @Test
    public void givenAnAllocation_whenConvertedToAllocationEntity_thenCreationDatesShouldBeEqual()
    {
        Instant expectedCreationDate = SOME_OTHER_INSTANT;
        Allocation someAllocation = new Allocation(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_AMOUNT,
                expectedCreationDate);

        AllocationEntity result = this.allocationAssembler.toPersistenceModel(someAllocation);

        assertEquals(expectedCreationDate, result.getCreationDate());
    }

    @Test
    public void givenAnAllocationEntity_whenConvertedToAllocation_thenCreationDatesShouldBeEqual()
    {
        Instant expectedCreateDate = SOME_OTHER_INSTANT;
        AllocationEntity someAllocationEntity = createSomeAllocationEntity();
        someAllocationEntity.setCreationDate(expectedCreateDate);

        Allocation result = this.allocationAssembler.toDomainModel(someAllocationEntity);

        assertEquals(expectedCreateDate, result.getCreationDate());
    }

    @Test
    public void givenAnAllocation_whenConvertedToAllocationEntity_thenBillNumbersShouldBeEqual()
    {
        Allocation someAllocation = createSomeAllocation();

        AllocationEntity result = this.allocationAssembler.toPersistenceModel(someAllocation);

        assertEquals(someAllocation.getBillNumber(), result.getBillNumber());
    }

    private Allocation createSomeAllocation()
    {
        return new Allocation(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_AMOUNT, SOME_DATE);
    }

    private AllocationEntity createSomeAllocationEntity()
    {
        return new AllocationEntity(SOME_ID, SOME_BILL_NUMBER, SOME_AMOUNT.asBigDecimal(), SOME_DATE);
    }
}
