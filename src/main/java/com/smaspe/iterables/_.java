package com.smaspe.iterables;

import java.util.Iterator;

/**
 * Created by Simon on 15/07/15.
 */
public class _ {

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

    public static <T> Iterable<T> iter(T... array) {
        return () -> new Iterator<T>() {
            public int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public T next() {
                return array[i++];
            }
        };
    }

    public static <T> Iterable<T> chain(Iterable<T>... iterables) {
        return chain(iter(iterables));
    }

    public static <T> Iterable<T> chain(Iterable<Iterable<T>> iterables) {
        Iterator<Iterable<T>> iterator = iterables.iterator();
        return () -> new Iterator<T>() {
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
        };
    }
}
