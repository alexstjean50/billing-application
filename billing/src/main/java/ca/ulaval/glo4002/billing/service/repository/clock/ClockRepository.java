package ca.ulaval.glo4002.billing.service.repository.clock;

import java.time.Instant;

public interface ClockRepository
{
    Instant retrieveCurrentTime();
}
