package com.abhinav.java.designpatterns.scattergather;

import java.util.List;
import java.util.stream.IntStream;

public class ScatterGatherSequential {
    public static void main(String[] args) {
        List<String> list =
                IntStream.range(0, 10)
                        .boxed()
                        .map(ScatterGatherSequential::generateTask)
                        .toList();

        list.forEach(System.out::println);
    }

    private static String generateTask(int i) {
        return "task " + i;
    }
}
