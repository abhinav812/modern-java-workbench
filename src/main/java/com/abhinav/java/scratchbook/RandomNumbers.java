package com.abhinav.java.scratchbook;

import java.util.Random;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class RandomNumbers {
    public static void main(String[] args) {
        Logger log = Logger.getLogger(RandomNumbers.class.getName());
        log.setLevel(Level.INFO);

        final IntPredicate isEven = i -> i % 2 == 0;
        final Predicate<Integer> isPrime =
                number -> IntStream.rangeClosed(2, number / 2)
                        .noneMatch(i -> number % i == 0);
        final var random = new Random(100);
        final IntSupplier randomIntSup = () -> (int) (random.nextInt() + 1); // List of random number between 1 and 100
        final IntSupplier randomIntSup2 = () -> new Random().nextInt(100);

        final var randIntegers = IntStream.generate(randomIntSup2)
                .limit(100)
                .boxed()
                .toList();

       log.info(String.valueOf(randIntegers.size()));

        final var resultList = randIntegers.stream().filter(isPrime).toList();

        log.info(String.valueOf(resultList.size()));
        log.info(resultList.toString());


    }
}
