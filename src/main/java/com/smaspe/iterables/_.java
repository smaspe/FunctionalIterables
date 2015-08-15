package com.smaspe.iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Simon on 15/07/15.
 *
 * Utils class for before Java8 Streams. filters, map, chaining...
 *
 * Returned Iterables are never null
 */
public class _<T> implements Iterable<T> {

    private static final Iterator EMPTY = new Iterator() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    };

    public static <T> _<T> iter(T... array) {
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

    public Iterable<T> chainWith(Iterable<T> next) {
        return _.<T>chain(delegate, next);
    }

    public static <T> _<T> chain(Iterable<T>... iterables) {
        return chain(iter(iterables));
    }

    public static <T> _<T> chain(Iterable<Iterable<T>> iterables) {
        return from(() -> new Iterator<T>() {
            Iterator<Iterable<T>> iterator = iterables.iterator();
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
                return current.next();
            }
        });
    }

    public <R> _<R> map(Func<T, R> func) {
        return map(delegate, func);
    }

    public static <T, R> _<R> map(Iterable<T> input, Func<T, R> func) {
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

    public _<T> filter(Func<T, Boolean> func) {
        return filter(delegate, func);
    }

    public static <T> _<T> filter(Iterable<T> input, Func<T, Boolean> func) {
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

    private Iterable<T> delegate;

    public _(Iterable<T> delegate) {
        this.delegate = delegate;
    }

    public static <T> _<T> from(Iterable<T> delegate) {
        return new _<>(delegate);
    }

    @Override
    public Iterator<T> iterator() {
        return delegate.iterator();
    }

    public ArrayList<T> collect() {
        ArrayList<T> res = new ArrayList<>();
        for (T t : delegate) {
            res.add(t);
        }
        return res;
    }

    public interface Func<T, R> {
        R call(T input);
    }
}
