package com.smaspe.iterables;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Simon on 15/07/15.
 */
public class Test_ extends TestCase {

    public void testIter() {
        String[] values = new String[]{"a", "b"};
        Iterator<String> iterator = _.iter(values).iterator();
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("b", iterator.next());
        assertFalse(iterator.hasNext());
    }

    public void testChain() {
        List<String> first = Arrays.asList("one", "bis");
        List<String> empty = Arrays.asList();
        List<String> second = Arrays.asList("two");
        Iterator<String> iterator = _.chain(Arrays.asList(first, empty, second)).iterator();
        assertTrue(iterator.hasNext());
        assertEquals("one", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("bis", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("two", iterator.next());
        assertFalse(iterator.hasNext());
    }
}
