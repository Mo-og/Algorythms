package org.example.implementations;

import org.example.interfaces.SortByComparator;

import java.util.Comparator;

/**
 * Basic heapsort implementation
 * @param <T> type of sorted objects
 */
public class HeapSort<T> implements SortByComparator<T> {
    @Override
    public T[] sort(T[] arr, Comparator<T> comparator) {
        if (arr == null) return null;

        for (int i = arr.length / 2 - 1; i >= 0; i--)
            heapify(arr, arr.length, i, comparator);

        for (int i = arr.length - 1; i >= 0; i--) {
            var temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            heapify(arr, i, 0, comparator);
        }
        return arr;
    }

    void heapify(T[] arr, int n, int i, Comparator<T> comparator) {
        int largest = i; //will be root
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && comparator.compare(arr[l], arr[largest]) > 0)
            largest = l;

        if (r < n && comparator.compare(arr[r], arr[largest]) > 0)
            largest = r;

        if (largest != i) {
            T temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            heapify(arr, n, largest, comparator);
        }
    }
}
