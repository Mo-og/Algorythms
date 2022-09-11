package org.example.implementations;

import org.example.Person;
import org.example.interfaces.IntFunction;
import org.example.interfaces.SortNumeric;

/**
 * Basically it is a {@link CountSortNumeric} implementation that relies on Person object itself (having 'next' field) as on Node,
 * allowing not to waste space on creating Node objects. Was implemented because CountSortNumeric ran out of memory on 100 million objects sort.
 * @see CountSortNumeric
 */
public class CountSortPerson implements SortNumeric<Person>{ // can be more generic if Person implements some <Node> interface

    @Override
    public Person[] sort(Person[] elements, IntFunction<Person> f) {
        if (elements == null || elements.length < 2) return elements;

        //looking for min and max elements
        int min = f.get(elements[0]);
        int max = f.get(elements[0]);
        for (Person t : elements) {
            if (min > f.get(t)) min = f.get(t);
            else if (max < f.get(t)) max = f.get(t);
        }

        //collecting elements from original array to ordered nodes[] array
        StartNode[] nodes = new StartNode[max - min + 1];
        for (Person p : elements) {
            int index = f.get(p) - min;
            if (nodes[index] == null) {
                nodes[index] = new StartNode(p);
            } else {
                if ((nodes[index]).last == null) {
                    nodes[index].value.next = p;
                } else {
                    nodes[index].last.next = p;
                }
                nodes[index].last = p;
            }
        }

        //collecting elements from nodes array to original elements array
        int index = 0;
        int i = 0;
        while (i < elements.length) {
            Person p = nodes[index].value;

            do {
                elements[i++] = p; //first array element is min so it is not null
                p = p.next;
                elements[i - 1].next = null;
            } while (p != null);

            //looking for next non-null element index
            while (i < elements.length && nodes[++index] == null) ; //just increments index
        }
        return elements;
    }

    private static class StartNode {
        Person value;
        Person last;

        public StartNode(Person value) {
            this.value = value;
        }
    }
}
