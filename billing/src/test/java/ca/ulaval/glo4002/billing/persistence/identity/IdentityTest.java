package ca.ulaval.glo4002.billing.persistence.identity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

public class IdentityTest
{
    @Test
    public void equalsContract()
    {
        EqualsVerifier.forClass(Identity.class)
                .usingGetClass()
                .verify();
    }
}