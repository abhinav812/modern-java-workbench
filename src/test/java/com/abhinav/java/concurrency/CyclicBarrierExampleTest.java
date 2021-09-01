package com.abhinav.java.concurrency;

import com.abhinav.java.util.ExecutorsUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;


public class CyclicBarrierExampleTest {
    Logger log = LoggerFactory.getLogger(CyclicBarrierExampleTest.class);

    @Test
    public void whenCyclicBarrier_notCompleted() throws Exception {
        int COUNT = 5;
        CyclicBarrier barrier = new CyclicBarrier(COUNT);

        Thread t = new Thread(() -> {
            try {
                barrier.await();
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        t.start();

        assertFalse(barrier.isBroken());
        assertEquals(1, barrier.getNumberWaiting());
    }

    @Test
    public void whenCyclicBarrier_reset() throws Exception {
        final int count = 7;
        final int threadCount = 20;
        final AtomicInteger updateCount = new AtomicInteger(0);

        CyclicBarrier barrier = new CyclicBarrier(count);

        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            service.execute(() -> {
                try {
                    if (barrier.getNumberWaiting() > 0) {
                        updateCount.incrementAndGet();
                    }
                    if (!barrier.isBroken()) {
                        barrier.await();
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        ExecutorsUtil.smartShutDown(service, false);

        int result = updateCount.get();
        log.info("update count: {}", result);
        assertTrue(result > 7);
    }

    @Test
    public void whenCyclicBarrier_countTrips() throws Exception {
        final int count = 5;
        final int threadCount = 20;
        final AtomicInteger updateCount = new AtomicInteger(0);

        CyclicBarrier barrier = new CyclicBarrier(count, () -> {
            log.info("{} - Incrementing counter", Thread.currentThread().getName());
            updateCount.incrementAndGet();
        });

        ExecutorService service = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            service.execute(() -> {
                try {
                    log.info("{} - Calling await | {} - {}", Thread.currentThread().getName(),
                            barrier.isBroken(), barrier.getNumberWaiting());
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        ExecutorsUtil.smartShutDown(service, false);

        int result = updateCount.get();
        log.info("update count: {}", result);
        assertEquals(4, result);
    }
}
