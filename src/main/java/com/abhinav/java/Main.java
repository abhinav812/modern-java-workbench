package com.abhinav.java;

import com.abhinav.java.util.CompletablePromise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        testFutureToCompletableFuture();
    }

    private static void testFutureToCompletableFuture() {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        final Future<String> future = service.submit(() -> "success");
        final var completableFuture = new CompletablePromise<>(future);

        completableFuture.whenComplete((result, failure) -> System.out.println(result));
    }
}
