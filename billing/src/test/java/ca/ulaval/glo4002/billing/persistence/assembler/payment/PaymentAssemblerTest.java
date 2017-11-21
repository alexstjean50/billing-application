package ca.ulaval.glo4002.billing.persistence.assembler.payment;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethod;
import ca.ulaval.glo4002.billing.persistence.assembler.allocation.AllocationAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AllocationEntity;
import ca.ulaval.glo4002.billing.persistence.entity.PaymentEntity;
import ca.ulaval.glo4002.billing.persistence.entity.PaymentMethodEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class PaymentAssemblerTest
{
    private static final Identity SOME_IDENTITY = new Identity(38);
    private static final Money SOME_AMOUNT = Money.valueOf(23);
    private static final Instant SOME_DATE = Instant.now();
    private static final long SOME_ID = 78;
    private static final long SOME_OTHER_ID = 45;
    private static final Identity SOME_OTHER_IDENTITY = new Identity(SOME_OTHER_ID);
    private static final Money SOME_OTHER_AMOUNT = Money.valueOf(22);
    private static final List<Allocation> SOME_ALLOCATIONS = new ArrayList<>();
    private static final List<AllocationEntity> SOME_ALLOCATION_ENTITIES = new ArrayList<>();
    private static final long SOME_PAYMENT_NUMBER = 23;
    private static final Instant SOME_OTHER_DATE = Instant.ofEpochSecond(331);
    private static final PaymentMethod SOME_PAYMENT_METHOD = mock(PaymentMethod.class);
    private static final PaymentMethodEntity SOME_PAYMENT_METHOD_ENTITY = mock(PaymentMethodEntity.class);
    private PaymentAssembler paymentAssembler;

    @Before
    public void initializePaymentAssembler()
    {
        PaymentMethodAssembler paymentMethodAssembler = mock(PaymentMethodAssembler.class);
        AllocationAssembler allocationAssembler = mock(AllocationAssembler.class);
        given(paymentMethodAssembler.toDomainModel(notNull())).willReturn(SOME_PAYMENT_METHOD);
        given(paymentMethodAssembler.toPersistenceModel(notNull())).willReturn(SOME_PAYMENT_METHOD_ENTITY);

        this.paymentAssembler = new PaymentAssembler(paymentMethodAssembler, allocationAssembler);
    }

    @Test
    public void givenAPayment_whenConvertedToPaymentEntity_thenPaymentIdentityShouldCorrespondToPaymentEntityId()
    {
        long expectedPaymentEntityId = SOME_OTHER_ID;
        Identity someOtherIdentity = new Identity(expectedPaymentEntityId);
        Payment somePayment = new Payment(someOtherIdentity, SOME_PAYMENT_NUMBER, SOME_AMOUNT, SOME_DATE,
                SOME_PAYMENT_METHOD, SOME_ALLOCATIONS);

        PaymentEntity result = this.paymentAssembler.toPersistenceModel(somePayment);

        assertEquals(expectedPaymentEntityId, result.getPaymentId());
    }

    @Test
    public void givenAPaymentEntity_whenConvertedToPayment_thenPaymentIdentityShouldCorrespondToPaymentEntityId()
    {
        Identity expectedPaymentIdentity = SOME_OTHER_IDENTITY;
        PaymentEntity somePaymentEntity = createSomePaymentEntity();
        somePaymentEntity.setPaymentId(expectedPaymentIdentity.getId());

        Payment result = this.paymentAssembler.toDomainModel(somePaymentEntity);

        assertEquals(expectedPaymentIdentity, result.getPaymentId());
    }

    @Test
    public void givenAPayment_whenConvertedToPaymentEntity_thenAmountsShouldBeEqual()
    {
        Money expectedAmount = SOME_OTHER_AMOUNT;
        Payment somePayment = new Payment(SOME_IDENTITY, SOME_PAYMENT_NUMBER, expectedAmount, SOME_DATE,
                SOME_PAYMENT_METHOD, SOME_ALLOCATIONS);

        PaymentEntity result = this.paymentAssembler.toPersistenceModel(somePayment);

        assertEquals(expectedAmount.asBigDecimal(), result.getAmount());
    }

    @Test
    public void givenAPaymentEntity_whenConvertedToPayment_thenAmountsShouldBeEqual()
    {
        Money expectedAmount = SOME_OTHER_AMOUNT;
        PaymentEntity somePaymentEntity = createSomePaymentEntity();
        somePaymentEntity.setAmount(expectedAmount.asBigDecimal());

        Payment result = this.paymentAssembler.toDomainModel(somePaymentEntity);

        assertEquals(expectedAmount, result.getAmount());
    }

    @Test
    public void givenAPayment_whenConvertedToPaymentEntity_thenPaymentNumberShouldBeEqual()
    {
        long expectedPaymentNumber = SOME_PAYMENT_NUMBER;
        Payment somePayment = new Payment(SOME_IDENTITY, expectedPaymentNumber, SOME_AMOUNT, SOME_DATE,
                SOME_PAYMENT_METHOD, SOME_ALLOCATIONS);

        PaymentEntity result = this.paymentAssembler.toPersistenceModel(somePayment);

        assertEquals(expectedPaymentNumber, result.getPaymentNumber());
    }

    @Test
    public void givenAPayment_whenConvertedToPaymentEntity_thenPaymentDatesShouldBeEqual()
    {
        Instant expectedPaymentDate = SOME_OTHER_DATE;
        Payment somePayment = new Payment(SOME_IDENTITY, SOME_PAYMENT_NUMBER, SOME_AMOUNT, expectedPaymentDate,
                SOME_PAYMENT_METHOD, SOME_ALLOCATIONS);

        PaymentEntity result = this.paymentAssembler.toPersistenceModel(somePayment);

        assertEquals(expectedPaymentDate, result.getCreationDate());
    }

    @Test
    public void givenAPaymentEntity_whenConvertedToPayment_thenPaymentDatesShouldBeEqual()
    {
        Instant expectedPaymentDate = SOME_OTHER_DATE;
        PaymentEntity somePaymentEntity = createSomePaymentEntity();
        somePaymentEntity.setCreationDate(expectedPaymentDate);

        Payment result = this.paymentAssembler.toDomainModel(somePaymentEntity);

        assertEquals(expectedPaymentDate, result.getPaymentDate());
    }

    @Test
    public void givenAPayment_whenConvertedToPaymentEntity_thenPaymentMethodShouldBeConvertedToPaymentMethodEntity()
    {
        Payment somePayment = new Payment(SOME_IDENTITY, SOME_PAYMENT_NUMBER, SOME_AMOUNT, SOME_DATE,
                SOME_PAYMENT_METHOD, SOME_ALLOCATIONS);

        PaymentEntity result = this.paymentAssembler.toPersistenceModel(somePayment);

        assertNotNull(result.getPaymentMethodEntity());
    }

    @Test
    public void givenAPaymentEntity_whenConvertedToPayment_thenPaymentMethodEntityShouldBeConvertedToPaymentMethod()
    {
        PaymentEntity somePaymentEntity = createSomePaymentEntity();

        Payment result = this.paymentAssembler.toDomainModel(somePaymentEntity);

        assertNotNull(result.getPaymentMethod());
    }

    private PaymentEntity createSomePaymentEntity()
    {
        return new PaymentEntity(SOME_ID, SOME_AMOUNT.asBigDecimal(), SOME_DATE, SOME_PAYMENT_NUMBER,
                SOME_PAYMENT_METHOD_ENTITY, SOME_ALLOCATION_ENTITIES);
    }
}
