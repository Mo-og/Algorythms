package org.example.interfaces;

import java.util.Comparator;

public interface SortByComparator<T> extends SortNumeric<T>{
    T[] sort(T[] elements, Comparator<T> comparator);

   default T[] sort(T[] elements, IntFunction<T> func){
       return sort(elements, Comparator.comparingInt(func::get));
   }

}
