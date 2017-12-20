package ca.ulaval.glo4002.billing.contexts;

import java.time.Clock;

public interface Context
{
    void apply(Clock clock);
}
