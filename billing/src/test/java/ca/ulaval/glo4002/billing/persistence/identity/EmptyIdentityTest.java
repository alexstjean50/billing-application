package ca.ulaval.glo4002.billing.persistence.identity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EmptyIdentityTest
{
    private static final long HIBERNATE_EMPTY_ID_VALUE = 0;

    @Test
    public void whenGetId_thenReturnZero()
    {
        Identity emptyIdentity = Identity.EMPTY;

        assertEquals(HIBERNATE_EMPTY_ID_VALUE, emptyIdentity.getId());
    }
}