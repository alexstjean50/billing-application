package ca.ulaval.glo4002.billing.persistence.repository.clock;

import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;

import java.time.Instant;

public class DefaultClockRepository implements ClockRepository
{
    @Override
    public Instant retrieveCurrentTime()
    {
        return Instant.now();
    }
}
