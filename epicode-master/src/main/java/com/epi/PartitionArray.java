// Copyright (c) 2015 Elements of Programming Interviews. All rights reserved.

package com.epi;

import java.util.*;

public class PartitionArray {
    public static void groupByAge(List<Person> people) {
        Map<Integer, Integer> ageToCount = new HashMap<>();
        for (Person p : people) {
            if (ageToCount.containsKey(p.age)) {
                ageToCount.put(p.age, ageToCount.get(p.age) + 1);
            } else {
                ageToCount.put(p.age, 1);
            }
        }
        Map<Integer, Integer> ageToOffset = new HashMap<>();
        int offset = 0;
        for (Map.Entry<Integer, Integer> kc : ageToCount.entrySet()) {
            ageToOffset.put(kc.getKey(), offset);
            offset += kc.getValue();
        }

        while (!ageToOffset.isEmpty()) {
            Map.Entry<Integer, Integer> from
                    = ageToOffset.entrySet().iterator().next();
            Integer toAge = people.get(from.getValue()).age;
            Integer toValue = ageToOffset.get(toAge);
            Collections.swap(people, from.getValue(), toValue);
            // Use ageToCount to see when we are finished with a particular age.
            Integer count = ageToCount.get(toAge) - 1;
            ageToCount.put(toAge, count);
            if (count > 0) {
                ageToOffset.put(toAge, toValue + 1);
            } else {
                ageToOffset.remove(toAge);
            }
        }
    }

    private static String randomString(int len) {
        StringBuilder ret = new StringBuilder();
        Random rnd = new Random();

        while (len-- > 0) {
            ret.append((char) (rnd.nextInt(26) + 97));
        }
        return ret.toString();
    }
    // @exclude

    private static void simpleTest() {
        List<Person> people
                = Arrays.asList(new Person(20, "foo"), new Person(10, "bar"),
                new Person(20, "widget"), new Person(20, "something"));
        groupByAge(people);
        if (people.get(0).age == 10) {
            assert (people.get(1).age == 20 && people.get(2).age == 20
                    && people.get(3).age == 20);
        } else {
            assert (people.get(1).age == 20 && people.get(2).age == 20
                    && people.get(3).age == 10);
        }
    }

    public static void main(String[] args) {
        simpleTest();
        Random rnd = new Random();
        for (int times = 0; times < 1000; ++times) {
            int size = 0;
            if (args.length == 1 || args.length == 2) {
                size = Integer.parseInt(args[0]);
            } else {
                size = rnd.nextInt(10000) + 1;
            }
            int k = 0;
            if (args.length == 2) {
                k = Integer.parseInt(args[1]);
            } else {
                k = rnd.nextInt(size) + 1;
            }
            List<Person> people = new ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                people.add(
                        new Person(rnd.nextInt(k), randomString(rnd.nextInt(10) + 1)));
            }
            Set<Integer> ageSet = new HashSet<>();
            for (Person p : people) {
                ageSet.add(p.age);
            }

            groupByAge(people);

            // Check the correctness of sorting.
            int diffCount = 1;
            for (int i = 1; i < people.size(); ++i) {
                if (!people.get(i).age.equals(people.get(i - 1).age)) {
                    ++diffCount;
                }
            }
            assert (diffCount == ageSet.size());
        }
    }

    // @include
    private static class Person {
        public Integer age;
        public String name;

        public Person(Integer k, String n) {
            age = k;
            name = n;
        }
    }
}
