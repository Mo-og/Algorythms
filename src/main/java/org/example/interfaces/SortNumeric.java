package org.example.interfaces;

public interface SortNumeric<T> {
    T[] sort(T[] elements, IntFunction<T> func);
}
