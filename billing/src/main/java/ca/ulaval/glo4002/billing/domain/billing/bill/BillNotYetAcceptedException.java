package ca.ulaval.glo4002.billing.domain.billing.bill;

public class BillNotYetAcceptedException extends RuntimeException
{
    private static final long serialVersionUID = 1963825673934476304L;

    public BillNotYetAcceptedException(String message)
    {
        super(message);
    }
}
