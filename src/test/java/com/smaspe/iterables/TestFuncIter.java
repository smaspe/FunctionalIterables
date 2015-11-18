package com.smaspe.iterables;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon on 15/07/15.
 */
public class TestFuncIter extends TestCase {

    public void testIter() {
        String[] values = new String[]{"a", "b"};
        Iterator<String> iterator = FuncIter.iter(values).iterator();
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertFalse(iterator.hasNext());
    }

    public void testCollect() {
        List<Integer> result = FuncIter.iter(1, 2, 3, 4).collect();
        assertEquals(4, result.size());
    }

    public void testCollectMap() {
        Map<String, Integer> result = FuncIter.iter(1, 2, 3, 4).collectWithKeys(FuncIter.iter("one", "two", "three"));
        // Only 3 keys
        assertEquals(3, result.size());
        assertEquals(1, result.get("one").intValue());
        assertEquals(2, result.get("two").intValue());
        assertEquals(3, result.get("three").intValue());
    }

    public void testChain() {
        List<String> first = Arrays.asList("one", "bis");
        List<String> empty = Arrays.asList();
        List<String> second = Arrays.asList("two");
        Iterator<String> iterator = FuncIter.chain(first, empty, second).iterator();
        assertEquals("one", iterator.next());
        assertEquals("bis", iterator.next());
        assertEquals("two", iterator.next());
        assertFalse(iterator.hasNext());

        iterator = FuncIter.chain(Arrays.asList(first, empty, second)).iterator();
        assertEquals("one", iterator.next());
        assertEquals("bis", iterator.next());
        assertEquals("two", iterator.next());
        assertFalse(iterator.hasNext());

    }

    public void testMap() {
        List<Integer> result = FuncIter.iter(1, 2, 3, 4).map(i -> i * i).collect();
        assertEquals(1, result.get(0).intValue());
        assertEquals(4, result.get(1).intValue());
        assertEquals(9, result.get(2).intValue());
        assertEquals(16, result.get(3).intValue());
    }

    public void testFilter() {
        List<Integer> result = FuncIter.iter(1, 2, 3, 4, 5, 6).filter(i -> i % 2 == 0).collect();
        assertEquals(2, result.get(0).intValue());
        assertEquals(4, result.get(1).intValue());
        assertEquals(6, result.get(2).intValue());
        assertEquals(3, result.size());
    }

    public void testRepeat() {
        Iterator<String> result = FuncIter.repeat("foo").iterator();
        assertEquals("foo", result.next());
        assertEquals("foo", result.next());
        assertEquals("foo", result.next());
        assertEquals("foo", result.next());
        assertEquals("foo", result.next());
        // A teacher once told me that 5 is a good approximation for infinity.
    }

    public void testZip() {
        List<FuncIter.Pair<Integer, String>> result = FuncIter.iter(1, 2, 3, 4).zip(FuncIter.iter("one", "two", "three", "four", "five")).collect();
        assertEquals(1, result.get(0).first.intValue());
        assertEquals("one", result.get(0).second);
        assertEquals(4, result.get(3).first.intValue());
        assertEquals("four", result.get(3).second);
        assertEquals(4, result.size());
    }

    public void testAny() {
        assertTrue(FuncIter.iter(1, 3, 4, 5, 6).any(t -> t % 2 == 0));
        assertFalse(FuncIter.iter(1, 3, 5, 7).any(t -> t % 2 == 0));
    }

    public void testAll() {
        assertFalse(FuncIter.iter(1, 3, 4, 5, 6).all(t -> t % 2 == 0));
        assertTrue(FuncIter.iter(2, 4, 6).all(t -> t % 2 == 0));
    }

    public void testFirst() {
        FuncIter<String> two = FuncIter.iter("one", "two");
        assertEquals("one", two.firstOr(null));
        FuncIter<String> zero = FuncIter.iter();
        assertNull(zero.firstOr(null));
        assertEquals("value", zero.firstOr("value"));
    }
}
