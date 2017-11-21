package ca.ulaval.glo4002.billing.persistence.assembler.client;

import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.persistence.entity.ClientEntity;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class ClientAssemblerTest
{
    private static final long SOME_ID = 1891;
    private static final Instant SOME_DATE = Instant.now();
    private static final DueTerm SOME_TERM = DueTerm.IMMEDIATE;
    private ClientAssembler clientAssembler;

    @Before
    public void initializeClientAssembler()
    {
        this.clientAssembler = new ClientAssembler();
    }

    @Test
    public void givenAClientEntity_whenConvertedToClient_thenIdsShouldBeEqual()
    {
        ClientEntity someClientEntity = createSomeClientEntity();

        Client conversionResult = this.clientAssembler.toDomainModel(someClientEntity);

        assertEquals(someClientEntity.getId(), conversionResult.getId());
    }

    @Test
    public void givenAClientEntity_whenConvertedToClient_thenCreationDatesShouldBeEqual()
    {
        ClientEntity someClientEntity = createSomeClientEntity();

        Client conversionResult = this.clientAssembler.toDomainModel(someClientEntity);

        assertEquals(someClientEntity.getCreationDate(), conversionResult.getCreationDate());
    }

    @Test
    public void givenAClientEntity_whenConvertedToClient_thenDefaultTermsShouldBeEqual()
    {
        ClientEntity someClientEntity = createSomeClientEntity();

        Client conversionResult = this.clientAssembler.toDomainModel(someClientEntity);

        assertEquals(someClientEntity.getDefaultTerm(), conversionResult.getDefaultTerm());
    }

    private ClientEntity createSomeClientEntity()
    {
        return new ClientEntity(SOME_ID, SOME_DATE, SOME_TERM);
    }
}