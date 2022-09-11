package org.example;

import java.util.Random;

public class Person {
    private int height;
    private int weight;
    private int age;

    /**
     * Bad, needed for space efficiency of CountSort only.
     * Just for experiment!
     * <br>For 100_000_000 Person objects taken space:
     * <br>3800 MB with 'next' field
     * <br>3000 MB wothout 'next' field
     */
    public Person next;

    public Person(int height, int weight, int age) {
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("{%dcm, %3dkg, %dy.o.}", height, weight, age);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private static final Random RANDOM = new Random();

    public static Person generate() {
        return new Person(RANDOM.nextInt(140, 220), RANDOM.nextInt(40, 120), RANDOM.nextInt(10, 90));
    }

    public static Person[] generate(int amount) {
        Person[] people = new Person[amount];
        for (int i = 0; i < people.length; i++) people[i] = generate();
        return people;
    }
}
