package com.smaspe.iterables;

import java.util.Iterator;

/**
 * Created by Simon on 15/07/15.
 */
public class _ {

    public static <T> Iterable<T> iter(final T[] array) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    public int i = 0;

                    @Override
                    public boolean hasNext() {
                        return i < array.length;
                    }

                    @Override
                    public T next() {
                        return array[i++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
