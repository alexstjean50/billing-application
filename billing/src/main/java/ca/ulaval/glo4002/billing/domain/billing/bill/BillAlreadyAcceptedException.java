package ca.ulaval.glo4002.billing.domain.billing.bill;

public class BillAlreadyAcceptedException extends RuntimeException
{
    private static final long serialVersionUID = -4046507344762845461L;

    public BillAlreadyAcceptedException(String message)
    {
        super(message);
    }
}
