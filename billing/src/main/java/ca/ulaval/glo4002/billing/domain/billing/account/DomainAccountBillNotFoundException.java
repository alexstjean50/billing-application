package ca.ulaval.glo4002.billing.domain.billing.account;

public class DomainAccountBillNotFoundException extends RuntimeException
{
    public DomainAccountBillNotFoundException(String message)
    {
        super(message);
    }
}
