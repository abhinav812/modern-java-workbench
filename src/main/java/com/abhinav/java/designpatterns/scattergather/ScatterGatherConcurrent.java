package com.abhinav.java.designpatterns.scattergather;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("java:S106") // suppressing sonarlint warning
public class ScatterGatherConcurrent {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        final Predicate<Integer> isEven = i -> i % 2 == 0;
        final IntSupplier randomIntSup = () -> new Random().nextInt(100);
        final var randIntegers = IntStream.generate(randomIntSup)
                .limit(10)
                .boxed()
                .toList();
        System.out.println("Random List: " + randIntegers);

        var splitByEvenList = randIntegers.stream().collect(Collectors.partitioningBy(isEven)).values().stream().toList();
        /*
        var oddList = collect.get(0);
        var evenList = collect.get(1);
        System.out.println("EvenList: " + evenList);
        System.out.println("Odd List: " + oddList);
        */

        // Scatter
        var completableFutures = splitByEvenList.stream().map(ScatterGatherConcurrent::squareUp).toList();

        // Gather
        var result = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .get(5, TimeUnit.SECONDS);
        System.out.println(result);

        var output2 = result.stream().flatMap(List::stream).toList();
        System.out.println("SquaredUp: " + output2);

        var output3 = Lists.newArrayList();
        result.forEach(output3::addAll);
        System.out.println("SquaredUp: " + output3);
    }

    private static CompletableFuture<List<Integer>> squareUp(List<Integer> input) {
        return CompletableFuture.supplyAsync(() -> input.stream().map(i -> i * i).toList());
    }
}
