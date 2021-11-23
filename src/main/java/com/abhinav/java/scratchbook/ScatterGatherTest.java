package com.abhinav.java.scratchbook;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

@SuppressWarnings("java:S106") // suppressing sonarlint warning
public class ScatterGatherTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        var container = new Container(List.of("A", "B", "C"), 4);
        System.out.println(container);

        var containerOfA = new Container(container.names().stream().filter(s -> s.contains("A")).toList(),
                container.number())/*.stream()
                .map(n -> new Container(List.of(n), container.number())).findFirst()*/;
        System.out.println(containerOfA);
        var containerOfB = new Container(container.names().stream().filter(s -> !s.contains("A")).toList(),
                container.number())/*.stream()
                .map(n -> new Container(List.of(n), container.number())).findFirst()*/;
        System.out.println(containerOfB);


        /*var containers = container.names().stream().map(n -> new Container(List.of(n), container.number())).toList();
        System.out.println(containers);*/

       /* var listMap = containers.stream().collect(Collectors.partitioningBy(c -> c.names().contains("A")));
        System.out.println(listMap);*/

        /*var containerOfA = listMap.get(true);
        System.out.println(containerOfA);
        var containerOfB = listMap.get(false);
        System.out.println(containerOfB);*/

        var completableFutures = Stream.of(additionForA(containerOfA), additionForB(containerOfB)).toList();

        var result = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> completableFutures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .get(5, TimeUnit.SECONDS);
        System.out.println(result);

    }

    private static CompletableFuture<ContainerResult> additionForA(Container container) {
        return CompletableFuture.supplyAsync(() -> new ContainerResult(container.names(), container.number() + 1)).orTimeout(5, TimeUnit.SECONDS);
    }

    private static CompletableFuture<ContainerResult> additionForB(Container container) {
        return CompletableFuture.supplyAsync(() -> new ContainerResult(container.names(), container.number() + 2)).orTimeout(5, TimeUnit.SECONDS);
    }

}

record Container(List<String> names, int number) {
}

record ContainerResult(List<String> names, int result) {
}

