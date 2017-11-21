package ca.ulaval.glo4002.billing.domain.billing.bill;

public class InvalidDiscountAmountException extends RuntimeException
{
    private static final long serialVersionUID = 6222690403096051635L;

    InvalidDiscountAmountException(String message)
    {
        super(message);
    }
}
