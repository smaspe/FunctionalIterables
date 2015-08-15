package com.smaspe.iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by Simon on 15/07/15.
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
    private Iterable<T> delegate;

    public _(Iterable<T> delegate) {
        this.delegate = delegate;
    }

    public static <T> _<T> iter(T... array) {
        return _(() -> new Iterator<T>() {
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

    public static <T> _<T> chain(Iterable<T>... iterables) {
        return chain(iter(iterables));
    }

    public static <T> _<T> chain(Iterable<Iterable<T>> iterables) {
        return _(() -> new Iterator<T>() {
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

    public static <T, R> _<R> map(Iterable<T> input, Func<T, R> func) {
        return _(() -> new Iterator<R>() {
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

    public static <T> _<T> filter(Iterable<T> input, Func<T, Boolean> func) {
        return _(() -> new Iterator<T>() {
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

    public static <T> _<T> _(Iterable<T> delegate) {
        return new _(delegate);
    }

    public Iterable<T> chainWith(Iterable<T> next) {
        return chain(delegate, next);
    }

    public <R> _<R> map(Func<T, R> func) {
        return map(delegate, func);
    }

    public _<T> filter(Func<T, Boolean> func) {
        return filter(delegate, func);
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
