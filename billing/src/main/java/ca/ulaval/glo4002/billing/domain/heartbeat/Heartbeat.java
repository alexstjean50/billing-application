package ca.ulaval.glo4002.billing.domain.heartbeat;

public class Heartbeat
{
    public final long date;
    public final String token;

    public Heartbeat(String token)
    {
        this.token = token;
        this.date = System.currentTimeMillis();
    }
}
