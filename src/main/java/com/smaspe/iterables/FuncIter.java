package com.smaspe.iterables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Simon on 15/07/15.
 * <p>
 * Utils class for before Java8 Streams. filters, map, chaining...
 * <p>
 * Returned Iterables are never null
 */
public class FuncIter<T> implements Iterable<T> {

    private static final Iterator EMPTY = new Iterator() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            throw new NoSuchElementException();
        }
    };

    private Iterable<T> delegate;

    private FuncIter(Iterable<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    public static <T> FuncIter<T> from(Iterable<T> delegate) {
        return new FuncIter<>(delegate);
    }

    public static <T> FuncIter<T> iter(T... array) {
        return from(() -> new Iterator<T>() {
            public int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public T next() {
                return array[i++];
            }
        });
    }

    public static <T> FuncIter<T> repeat(T value) {
        return from(() -> new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return value;
            }
        });
    }

    public static <T> FuncIter<T> chain(Iterable<T>... iterables) {
        return chain(iter(iterables));
    }

    public static <T> FuncIter<T> chain(Iterable<? extends Iterable<T>> iterables) {
        return from(() -> new Iterator<T>() {
            Iterator<? extends Iterable<T>> iterator = iterables.iterator();
            Iterator<T> current = EMPTY;

            @Override
            public boolean hasNext() {
                while (!current.hasNext() && iterator.hasNext()) {
                    current = iterator.next().iterator();
                }
                return current.hasNext();
            }

            @Override
            public T next() {
                hasNext();
                return current.next();
            }
        });
    }

    public FuncIter<T> chainWith(Iterable<T> next) {
        return FuncIter.<T>chain(this, next);
    }

    public <R> FuncIter<R> map(Func<T, R> func) {
        return map(this, func);
    }

    public static <T, R> FuncIter<R> map(Iterable<T> input, Func<T, R> func) {
        return from(() -> new Iterator<R>() {
            Iterator<T> iter = input.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public R next() {
                return func.call(iter.next());
            }
        });
    }

    public FuncIter<T> filter(Func<T, Boolean> func) {
        return filter(this, func);
    }

    public static <T> FuncIter<T> filter(Iterable<T> input, Func<T, Boolean> func) {
        return from(() -> new Iterator<T>() {
            boolean hasKnownNext = false;
            Iterator<T> iter = input.iterator();
            T nextValue = null;

            @Override
            public boolean hasNext() {
                if (hasKnownNext) {
                    return true;
                }
                while (iter.hasNext()) {
                    nextValue = iter.next();
                    if (func.call(nextValue)) {
                        hasKnownNext = true;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public T next() {
                if (hasNext()) {
                    hasKnownNext = false;
                    return nextValue;
                }
                throw new NoSuchElementException();
            }
        });
    }

    public boolean all(Func<T, Boolean> test) {
        return all(this, test);
    }

    public static <T> boolean all(Iterable<T> input, Func<T, Boolean> test) {
        return !any(input, (t -> !test.call(t)));
    }

    public boolean any(Func<T, Boolean> test) {
        return any(this, test);
    }

    public static <T> boolean any(Iterable<T> input, Func<T, Boolean> test) {
        for (T t : input) {
            if (test.call(t)) {
                return true;
            }
        }
        return false;
    }

    public T firstOr(T defaultValue) {
        return firstOr(this, defaultValue);
    }

    public static <T> T firstOr(Iterable<T> input, T defaultValue) {
        Iterator<T> iterator = input.iterator();
        return iterator.hasNext() ? iterator.next() : defaultValue;
    }

    public <V> FuncIter<Pair<T, V>> zip(Iterable<V> with) {
        return zip(this, with);
    }

    public static <U, V> FuncIter<Pair<U, V>> zip(Iterable<U> firsts, Iterable<V> seconds) {
        return from(() -> new Iterator<Pair<U, V>>() {
            Iterator<U> iterFirst = firsts.iterator();
            Iterator<V> iterSecond = seconds.iterator();

            @Override
            public boolean hasNext() {
                return iterFirst.hasNext() && iterSecond.hasNext();
            }

            @Override
            public Pair<U, V> next() {
                return new Pair<>(iterFirst.next(), iterSecond.next());
            }
        });
    }

    public ArrayList<T> collect() {
        return collect(this);
    }

    public static <T> ArrayList<T> collect(Iterable<T> input) {
        ArrayList<T> res = new ArrayList<>();
        for (T t : input) {
            res.add(t);
        }
        return res;
    }

    public <K> HashMap<K, T> collectWithKeys(Iterable<K> keys) {
        return collectWithKeys(this, keys);
    }

    public static <K, V> HashMap<K, V> collectWithKeys(Iterable<V> values, Iterable<K> keys) {
        HashMap<K, V> res = new HashMap<>();
        for (Pair<K, V> t : from(keys).zip(values)) {
            res.put(t.first, t.second);
        }
        return res;
    }

    public interface Func<T, R> {
        R call(T input);
    }

    public static class Pair<U, V> {
        public U first;
        public V second;

        private Pair(U first, V second) {
            this.first = first;
            this.second = second;
        }
    }
}
