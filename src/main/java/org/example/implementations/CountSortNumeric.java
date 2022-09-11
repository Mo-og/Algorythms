package org.example.implementations;

import org.example.interfaces.IntFunction;
import org.example.interfaces.SortNumeric;

/**
 * Sort implementation of objects array based on counting sort, but adapted for objects instead of numbers
 * using hashmap-like principle (with non-unique elements)
 *
 * <br>Benefits: linear, stable
 * <br>Drawbacks: can use much space according to value range and elements count:
 *
 * <p>For number range 0 to 100 we'll need 100 + n space for nodes. So if range is too big (bigger than n) space is wasted.
 * E.g. for array {10, -100, 1000, 1, 2} an array of nodes is created of length 1100 (max-min).
 * On the other hand n node objects have to be created which may double the space used by input array objects.<p/>
 * <p>Array itself contains links and therefore is lightweight, space is taken by actual objects of array and nodes that have to be created.)<p/>
 * <p>
 * For Person sort is good because ranges of height, weight and age are small.
 *
 * @param <T> Type of element of array to sort
 */
public class CountSortNumeric<T> implements SortNumeric<T> {
    @Override
    @SuppressWarnings("unchecked")
    public T[] sort(T[] elements, IntFunction<T> f) {
        if (elements == null || elements.length < 2) return elements;

        int min = f.get(elements[0]);
        int max = f.get(elements[0]);
        for (T t : elements) {
            if (min > f.get(t)) min = f.get(t);
            else if (max < f.get(t)) max = f.get(t);
        }

        //Elements of nodes[] are StartNodes that have links to next elements
        // (for collecting to original array) and last one (to quickly add new elements)
        // Structure is similar to:
        // nodes[ StartNode,   null   ,   null   , StartNode, ...]
        //            \/                               \/     ...
        //           Node                             null    ...
        //            \/                                      ...
        //           null                                     ...

        Node<T>[] nodes = new Node[max - min + 1];
        for (T t : elements) {
            int index = f.get(t) - min;
            if (nodes[index] == null) {
                nodes[index] = new StartNode<>(t);
            } else {
                var n = new Node<>(t);
                if (((StartNode<T>) nodes[index]).last == null) {
                    nodes[index].next = n;
                } else {
                    ((StartNode<T>) nodes[index]).last.next = n;
                }
                ((StartNode<T>) nodes[index]).last = n;
            }
        }

        /*
        Here could be code for Task 3 since node[x] is a list of <Person> having same <e.g. weight>
         */

        //collecting elements from nodes array to original elements array
        int index = 0;
        var n = nodes[index];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = n.value; //first array element is min so it is not null

            //looking for next non-null element index
            if (n.next == null && i + 1 < elements.length) {
                while (nodes[++index] == null) ; //just increments index
                n = nodes[index];
            } else n = n.next;
        }

        return elements;
    }

    //can be local classes
    private static class Node<E> {
        private Node(E value) {
            this.value = value;
        }

        E value;
        Node<E> next;
    }

    private static class StartNode<E> extends Node<E> {
        private StartNode(E element) {
            super(element);
        }

        Node<E> last;
    }


}