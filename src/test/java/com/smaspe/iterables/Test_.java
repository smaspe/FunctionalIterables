package com.smaspe.iterables;

import junit.framework.TestCase;

import java.util.Iterator;

/**
 * Created by Simon on 15/07/15.
 */
public class Test_ extends TestCase {
    public void testIter() {
        String[] values = new String[] {"a", "b"};
        Iterator<String> iterator = _.iter(values).iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertFalse(iterator.hasNext());
    }
}
