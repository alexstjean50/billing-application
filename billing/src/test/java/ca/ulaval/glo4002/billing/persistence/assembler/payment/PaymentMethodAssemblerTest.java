package ca.ulaval.glo4002.billing.persistence.assembler.payment;

import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethod;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethodSource;
import ca.ulaval.glo4002.billing.persistence.entity.PaymentMethodEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaymentMethodAssemblerTest
{
    private static final PaymentMethodSource SOME_PAYMENT_METHOD_SOURCE = PaymentMethodSource.CREDIT_CARD;
    private static final String SOME_TEXT = "Starscream";
    private static final Identity SOME_IDENTITY = Identity.EMPTY;
    private static final long SOME_OTHER_ID = 41;
    private static final long SOME_ID = 12;
    private static final PaymentMethodSource SOME_OTHER_PAYMENT_METHOD_SOURCE = PaymentMethodSource.CHECK;
    private static final String SOME_OTHER_TEXT = "Decepticon";
    private PaymentMethodAssembler paymentMethodAssembler;

    @Before
    public void initializePaymentMethodAssembler()
    {
        this.paymentMethodAssembler = new PaymentMethodAssembler();
    }

    @Test
    public void
    givenAPaymentMethod_whenConvertedToPaymentMethodEntity_thenPaymentMethodIdentityShouldCorrespondToPaymentMethodEntityId()
    {
        long expectedPaymentMethodEntityId = SOME_OTHER_ID;
        Identity someOtherIdentity = new Identity(expectedPaymentMethodEntityId);
        PaymentMethod somePaymentMethod = new PaymentMethod(someOtherIdentity, SOME_TEXT, SOME_PAYMENT_METHOD_SOURCE);

        PaymentMethodEntity result = this.paymentMethodAssembler.toPersistenceModel(somePaymentMethod);

        assertEquals(expectedPaymentMethodEntityId, result.getId());
    }

    @Test
    public void
    givenAPaymentMethodEntity_whenConvertedToPaymentMethod_thenPaymentMethodIdentityShouldCorrespondToPaymentMethodEntityId()
    {
        Identity expectedPaymentMethodIdentity = new Identity(SOME_OTHER_ID);
        PaymentMethodEntity somePaymentMethodEntity = new PaymentMethodEntity(SOME_OTHER_ID, SOME_TEXT,
                SOME_PAYMENT_METHOD_SOURCE);

        PaymentMethod result = this.paymentMethodAssembler.toDomainModel(somePaymentMethodEntity);

        assertEquals(expectedPaymentMethodIdentity, result.identity);
    }

    @Test
    public void givenAPaymentMethod_whenConvertedToPaymentMethodEntity_thenBankAccountsShouldBeEqual()
    {
        String expectedBankAccount = SOME_OTHER_TEXT;
        PaymentMethod somePaymentMethod = new PaymentMethod(SOME_IDENTITY, expectedBankAccount,
                SOME_PAYMENT_METHOD_SOURCE);

        PaymentMethodEntity result = this.paymentMethodAssembler.toPersistenceModel(somePaymentMethod);

        assertEquals(expectedBankAccount, result.getBankAccount());
    }

    @Test
    public void givenAPaymentMethodEntity_whenConvertedToPaymentMethod_thenBankAccountsShouldBeEqual()
    {
        String expectedBankAccount = SOME_OTHER_TEXT;
        PaymentMethodEntity somePaymentMethodEntity = new PaymentMethodEntity(SOME_ID, expectedBankAccount,
                SOME_PAYMENT_METHOD_SOURCE);

        PaymentMethod result = this.paymentMethodAssembler.toDomainModel(somePaymentMethodEntity);

        assertEquals(expectedBankAccount, result.bankAccount);
    }

    @Test
    public void givenAPaymentMethod_whenConvertedToPaymentMethodEntity_thenPaymentMethodSourcesShouldBeEqual()
    {
        PaymentMethodSource expectedPaymentMethodSource = SOME_OTHER_PAYMENT_METHOD_SOURCE;
        PaymentMethod somePaymentMethod = new PaymentMethod(SOME_IDENTITY, SOME_TEXT, expectedPaymentMethodSource);

        PaymentMethodEntity result = this.paymentMethodAssembler.toPersistenceModel(somePaymentMethod);

        assertEquals(expectedPaymentMethodSource, result.getSource());
    }

    @Test
    public void givenAPaymentMethodEntity_whenConvertedToPaymentMethod_thenPaymentMethodSourcesShouldBeEqual()
    {
        PaymentMethodSource expectedPaymentMethodSource = SOME_OTHER_PAYMENT_METHOD_SOURCE;
        PaymentMethodEntity somePaymentMethodEntity = new PaymentMethodEntity(SOME_ID, SOME_TEXT,
                expectedPaymentMethodSource);

        PaymentMethod result = this.paymentMethodAssembler.toDomainModel(somePaymentMethodEntity);

        assertEquals(expectedPaymentMethodSource, result.source);
    }
}
