package com.smaspe.iterables;

import junit.framework.TestCase;

import java.util.*;

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
        Iterator<?> chain = FuncIter.chain().iterator();
        assertFalse(chain.hasNext());
        try {
            chain.next();
            fail();
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }

        List<String> first = Arrays.asList("one", "bis");
        List<String> empty = Arrays.asList();
        List<String> second = Arrays.asList("two");
        Iterator<String> iterator = FuncIter.chain(first, empty, second).iterator();
        assertEquals("one", iterator.next());
        assertEquals("bis", iterator.next());
        assertEquals("two", iterator.next());
        assertFalse(iterator.hasNext());

        List<Integer> chained = FuncIter.iter(1, 2, 3).chainWith(FuncIter.iter(4, 5)).collect();
        assertEquals(5, chained.size());
        assertEquals(1, chained.get(0).intValue());
        assertEquals(5, chained.get(4).intValue());
    }

    public void testMap() {
        List<Integer> result = FuncIter.iter(1, 2, 3, 4).map(i -> i * i).collect();
        assertEquals(1, result.get(0).intValue());
        assertEquals(4, result.get(1).intValue());
        assertEquals(9, result.get(2).intValue());
        assertEquals(16, result.get(3).intValue());
    }

    public void testEach() {
        Map<String, String> value = new HashMap<>();
        FuncIter.iter(value).each(i -> i.put("key", "value"));
        assertEquals(1, value.size());
        assertEquals("value", value.get("key"));
    }

    public void testFilter() {
        try {
            Iterator<Integer> iterator = FuncIter.iter(1, 2, 3).filter(i -> false).iterator();
            assertFalse(iterator.hasNext());
            iterator.next();
            fail();
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
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
        assertTrue(result.hasNext());
        // A teacher once told me that 5 is a good approximation for infinity.
    }

    public void testZip() {
        List<FuncIter.Pair<Integer, String>> result = FuncIter.iter(1, 2, 3, 4, 5).zip(FuncIter.iter("one", "two", "three", "four")).collect();
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

    public void testRange() {
        try {
            FuncIter.range(0,0,0);
            fail();
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
        try {
            Iterator<Long> iterator = FuncIter.range(0, 0, 1).iterator();
            assertFalse(iterator.hasNext());
            iterator.next();
            fail();
        } catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }

        List<Long> range = FuncIter.range(3).collect();
        assertEquals(3, range.size());
        assertEquals(0, range.get(0).intValue());
        assertEquals(1, range.get(1).intValue());
        assertEquals(2, range.get(2).intValue());

        range = FuncIter.range(3, 7).collect();
        assertEquals(4, range.size());
        assertEquals(3, range.get(0).intValue());
        assertEquals(4, range.get(1).intValue());
        assertEquals(5, range.get(2).intValue());
        assertEquals(6, range.get(3).intValue());

        range = FuncIter.range(0, 4, 2).collect();
        assertEquals(2, range.size());
        assertEquals(0, range.get(0).intValue());
        assertEquals(2, range.get(1).intValue());

        range = FuncIter.range(0, 3, 2).collect();
        assertEquals(2, range.size());
        assertEquals(0, range.get(0).intValue());
        assertEquals(2, range.get(1).intValue());

        range = FuncIter.range(2, 0, -1).collect();
        assertEquals(2, range.size());
        assertEquals(2, range.get(0).intValue());
        assertEquals(1, range.get(1).intValue());

        range = FuncIter.range(3, 0, -2).collect();
        assertEquals(2, range.size());
        assertEquals(3, range.get(0).intValue());
        assertEquals(1, range.get(1).intValue());
    }

    public void testReduce() {
        FuncIter<Long> values = FuncIter.range(4);
        assertEquals(6, (long) values.reduce((a, b) -> a + b, 0l));
        assertEquals(42, (long) values.reduce((a, b) -> b, 42l));
        assertEquals(3, (long) values.reduce((a, b) -> a, 42l));
        FuncIter<String> empty = FuncIter.iter();
        assertEquals(null, empty.reduce(null, null));
    }

    public void testFlatMap() {
        FuncIter<Long> values = FuncIter.range(2,4);
        List<Long> result = values.flatMap(FuncIter::range).collect();
        assertEquals(5, result.size());
        assertEquals(0l, result.get(0).longValue());
        assertEquals(1l, result.get(1).longValue());
        assertEquals(0l, result.get(2).longValue());
        assertEquals(1l, result.get(3).longValue());
        assertEquals(2l, result.get(4).longValue());
    }
}
