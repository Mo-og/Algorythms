package org.example;

import org.example.implementations.CountSortNumeric;
import org.example.implementations.CountSortPerson;
import org.example.implementations.HeapSort;
import org.example.interfaces.IntFunction;
import org.example.interfaces.SortNumeric;

import java.util.Arrays;
import java.util.Comparator;

public class SortTest {
    public static final int OBJECTS_COUNT = 1_000_000;
    public static final int ITERATIONS = 10;
    public static final String DELIMITER = "#".repeat(30);
    private static final boolean countSortPeopleEnabled = true;
    private static final boolean javaArraysSortEnabled = true;
    private static final boolean countSortNumericEnabled = true;
    private static final boolean heapSortEnabled = true;

    public static void main(String[] args) {
        /*
         * Three variations of sorting are presented (and compared to default Java Arrays.sort):
         *
         * CountSortNumeric (Counting sort implementation for objects that are sorted by single numeric value)
         * CountSortPerson  (Memory usage-optimised CountSortNumeric specific for Person.class that breaks 'The Open Closed Principle' for it)
         * Heapsort         (Memory-friendly algorithm with Comparator)
         *
         * TO DO for sorts: handle null values in arrays
         *
         * See their classes to learn more
         * */

        /*
         * For array of 10_000_000 objects timing is (in milliseconds):
         *
         *   CountSortPeople: 928 ms on average
         *       Arrays.sort: 2296 ms on average
         *  CountSortNumeric: 965 ms on average
         *          HeapSort: 10891 ms on average
         *
         * For 100_000_000 objects (tested separately):
         *
         *   CountSortPeople: 9486 ms on average
         *       Arrays.sort: 34317 ms on average // if 'next' field is removed from Person.class otherwise - OutOfMemoryError
         *  CountSortNumeric: -1 ms         // <- OutOfMemoryError
         *          HeapSort: 119578 ms on average
         * */

        var time = System.currentTimeMillis();
        Person[] people = Person.generate(OBJECTS_COUNT);
        System.out.println("Generated after: " + (System.currentTimeMillis() - time) + " ms");

        testAllSorts(people, ITERATIONS); // simple benchmark

        //TASK 1,2
        System.out.println("Sorted by age:");
        print(new CountSortPerson().sort(people, Person::getAge), 100);
        System.out.println("Sorted by weight:");
        print(new CountSortPerson().sort(people, Person::getWeight), 100);
        System.out.println("Sorted by height:");
        print(new CountSortPerson().sort(people, Person::getHeight), 100);
        //since CountSortPerson is a stable sort, array becomes sorted by height, then weight and then age

        //EXTRA QUESTION
        /*
            - Is it possible to ensure linear time complexity in task 1?
            - Yes. Since we need to sort an array by a single numeric value we can use algorithm with linear complexity like Counting sort
         */

        //TASK 3
        //for generated array of Person maximum of unique records is 6400 since height and
        // weight ranges for Person.generate(...) are 80 and 80*80 is 6400
        Person[] people1 = {
                new Person(1, 1, 1),
                new Person(1, 1, 1),
                new Person(1, 1, 1),
                new Person(1, 1, 1),
                new Person(1, 1, 1),
                new Person(1, 1, 1)
        };
        System.out.println("Unique values: " + findSameWeightDifferentHeight(people1));//should be 1

        Person[] people2 = {
                new Person(1, 1, 1),
                new Person(2, 1, 1),
                new Person(3, 1, 1),
                new Person(4, 1, 1),
                new Person(5, 1, 1),
                new Person(6, 1, 1)
        };
        System.out.println("Unique values: " + findSameWeightDifferentHeight(people2));//should be 6

        Person[] people3 = {
                new Person(1, 1, 1),
                new Person(1, 2, 1),
                new Person(1, 3, 1),
                new Person(1, 4, 1),
                new Person(1, 5, 1),
                new Person(1, 6, 1)
        };
        System.out.println("Unique values: " + findSameWeightDifferentHeight(people3));//should be 6

        Person[] people4 = {
                new Person(1, 1, 1),
                new Person(1, 1, 2),
                new Person(1, 1, 3),
                new Person(1, 1, 4),
                new Person(1, 1, 5),
                new Person(1, 1, 6)
        };
        System.out.println("Unique values: " + findSameWeightDifferentHeight(people4));//should be 1 (age is ignored)

        Person[] people5 = {
                new Person(1, 1, 1), //1
                new Person(2, 1, 1), //2
                new Person(1, 1, 1),
                new Person(1, 2, 1), //3
                new Person(2, 2, 1), //4
                new Person(3, 2, 1), //5
                new Person(1, 3, 1), //6
                new Person(1, 3, 1),
                new Person(2, 3, 1)  //7
        };
        System.out.println("Unique values: " + findSameWeightDifferentHeight(people5));//should be 7
    }

    private static int findSameWeightDifferentHeight(Person[] people) {
        return findWithSameXDifferentY(people, Person::getWeight, Person::getHeight);
    }

    @SuppressWarnings("unchecked")
    private static <T> int findWithSameXDifferentY(T[] arr, IntFunction<T> paramXFunc, IntFunction<T> paramYFunc) {
        if (arr.length < 1) return 0;
        int count = 1;

        if (arr instanceof Person[] arr1){
            new CountSortPerson().sort(arr1, (IntFunction<Person>) paramYFunc);
            new CountSortPerson().sort(arr1, (IntFunction<Person>) paramXFunc);
        }else {
            new CountSortNumeric<T>().sort(arr, paramYFunc);
            new CountSortNumeric<T>().sort(arr, paramXFunc);
        }

        T prev = arr[0];
        for (T element : arr) {
            if (paramXFunc.get(prev) == paramXFunc.get(element) && paramYFunc.get(prev) == paramYFunc.get(element))
                continue;
            count++;
            prev = element;
        }
        return count;
    }

    private static void testAllSorts(Person[] people) {
        testAllSorts(people, 1);
    }

    private static void testAllSorts(Person[] people, int iterations) {
        //CountSortPeople
        if (countSortPeopleEnabled)
            testAverageSortTime(people, new CountSortPerson(), Person::getAge, iterations);

        //Java Arrays.sort()
        if (javaArraysSortEnabled)
            testArraysSortAverageTime(people, Person::getAge, iterations);

        //CountSortNumeric
        if (countSortNumericEnabled) {
            var countSort = new CountSortNumeric<Person>();
            try {
                testAverageSortTime(people, countSort, Person::getAge, iterations);
            } catch (OutOfMemoryError e) {  // seems to work in this case ¯\_(ツ)_/¯
                printSortBenchmark(countSort, -1);
            }
        }

        //HeapSort
        if (heapSortEnabled)
            testAverageSortTime(people, new HeapSort<>(), Person::getAge, iterations);

        System.out.println();
    }

    private static <T> void printSortBenchmark(SortNumeric<T> sort, long time) {
        System.out.printf("%20s: %3d ms%n", sort.getClass().getSimpleName(), time);
    }

    public static void print(Person[] people) {
        for (Person person : people) System.out.println(person);
        System.out.println(DELIMITER);
    }

    public static void print(Person[] people, int limit) {
        for (int i = 0, peopleLength = Math.min(people.length, limit); i < peopleLength; i++) {
            Person person = people[i];
            System.out.println(person);
        }
        System.out.println(DELIMITER);
    }

    private static <T> void testAverageSortTime(T[] arr, SortNumeric<T> sort, IntFunction<T> f, int iterations) {
        long time = 0;
        for (int i = 0; i < iterations; i++) {
            if (iterations != 1)
                System.out.printf("\r%20s: %d%%", sort.getClass().getSimpleName(), (int) (((double) i / iterations) * 100));
            var arrCopy = arr.clone();
            var t = System.currentTimeMillis();
            sort.sort(arrCopy, f);
            time += System.currentTimeMillis() - t;
        }
        System.out.printf(iterations > 1 ? "\r%20s: %3d ms on average%n" : "\r%20s: %3d ms%n", sort.getClass().getSimpleName(), (int) ((double) time / iterations));
    }

    private static <T> void testArraysSortAverageTime(T[] arr, IntFunction<T> f, int iterations) {
        long time = 0;
        for (int i = 0; i < iterations; i++) {
            if (iterations != 1)
                System.out.printf("\r%20s: %d%%", "Arrays.sort", (int) (((double) i / iterations) * 100));
            var arrCopy = arr.clone();
            var t = System.currentTimeMillis();
            Arrays.sort(arrCopy, Comparator.comparingInt(f::get));
            time += System.currentTimeMillis() - t;
        }
        System.out.printf(iterations > 1 ? "\r%20s: %3d ms on average%n" : "\r%20s: %3d ms%n", "Arrays.sort", (int) ((double) time / iterations));
    }
}
