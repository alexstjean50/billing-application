package ca.ulaval.glo4002.billing.domain.billing.client;

public enum DueTerm
{
    IMMEDIATE(0), DAYS30(30), DAYS90(90);
    public final int daysToPay;

    DueTerm(int daysToPay)
    {
        this.daysToPay = daysToPay;
    }
}
