package ca.ulaval.glo4002.commons;

import java.util.Collection;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AssertHelpers
{
    public static <T> void assertHasExactlyXNotNullElements(int x, Collection<T> resultingCollection)
    {
        assertEquals(x, resultingCollection.size());
        assertTrue(resultingCollection.stream()
                .allMatch(Objects::nonNull));
    }
}
