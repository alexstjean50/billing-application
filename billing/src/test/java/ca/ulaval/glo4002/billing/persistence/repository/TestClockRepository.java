package ca.ulaval.glo4002.billing.persistence.repository;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;

import java.time.Clock;
import java.time.Instant;

public class TestClockRepository implements ClockRepository
{
    @Override
    public Instant retrieveCurrentTime()
    {
        return Instant.now(ServiceLocator.getService(Clock.class));
    }
}
