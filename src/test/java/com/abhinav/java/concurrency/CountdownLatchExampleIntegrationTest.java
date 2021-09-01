package com.abhinav.java.concurrency;

import com.abhinav.java.concurrency.synchronizers.countdownlatch.BrokenWorker;
import com.abhinav.java.concurrency.synchronizers.countdownlatch.WaitingWorker;
import com.abhinav.java.concurrency.synchronizers.countdownlatch.Worker;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class CountdownLatchExampleIntegrationTest {
    Logger log = LoggerFactory.getLogger(CountdownLatchExampleIntegrationTest.class);

    @Test
    public void whenParallelProcessing_thenMainThreadWillBlockUntilCompletion() throws Exception {
        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(5);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new Worker(outputScraper, latch)))
                .limit(5)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        latch.await();
        outputScraper.add("Latch released");
        log.info("{}", outputScraper);
        assertThat(outputScraper).containsExactly(
                "Counted down",
                "Counted down",
                "Counted down",
                "Counted down",
                "Counted down",
                "Latch released"
        );
    }

    @Test
    public void whenDoingLotsOfThreadsInParallel_thenStartThemAtTheSameTime() throws Exception {
        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        var latch = new CountDownLatch(5);
        var callingThreadBlocker = new CountDownLatch(1);
        var completedThreadCounter = new CountDownLatch(5);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new WaitingWorker(outputScraper,
                        latch,
                        callingThreadBlocker,
                        completedThreadCounter)))
                .limit(5)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        latch.await(); // Wait till all the threads are created
        outputScraper.add("Workers ready");
        callingThreadBlocker.countDown(); // Signal all the waiting workers that, all worker creation DONE.
        completedThreadCounter.await(); // Wait till all the workers done their JOB
        outputScraper.add("Workers complete");

        log.info("{}", outputScraper);
        assertThat(outputScraper)
                .containsExactly(
                        "Workers ready",
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Counted down",
                        "Workers complete"
                );
    }

    @Test
    public void whenFailingToParallelProcess_thenMainThreadShouldGetNotGetStuck() throws Exception{
        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(5);

        List<Thread> workers = Stream
                .generate(() -> new Thread(new BrokenWorker(outputScraper, latch)))
                .limit(5)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        boolean completed = latch.await(3L, TimeUnit.SECONDS);
        assertThat(completed).isFalse();
    }
}
