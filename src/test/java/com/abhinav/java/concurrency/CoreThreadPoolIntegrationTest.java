package com.abhinav.java.concurrency;

import com.abhinav.java.concurrency.threadpool.CountingTask;
import com.abhinav.java.concurrency.threadpool.TreeNode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;


public class CoreThreadPoolIntegrationTest {
    Logger log = LoggerFactory.getLogger(CoreThreadPoolIntegrationTest.class);

    @Test
    public void whenUsingFixedThreadPool_thenCoreAndMaximumThreadSizeAreTheSame() throws Exception {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            Thread.sleep(1000);
            return null;
        });
        executor.submit(() -> {
            Thread.sleep(1000);
            return null;
        });
        executor.submit(() -> {
            Thread.sleep(1000);
            return null;
        });

        assertEquals(2, executor.getPoolSize());
        assertEquals(1, executor.getQueue().size());
    }

    @Test
    public void whenUsingCachedThreadPool_thenPoolSizeGrowsUnbounded() throws Exception {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        executor.submit(() -> {
            Thread.sleep(1000);
            return null;
        });
        executor.submit(() -> {
            Thread.sleep(1000);
            return null;
        });
        executor.submit(() -> {
            Thread.sleep(1000);
            return null;
        });

        assertEquals(3, executor.getPoolSize());
        assertEquals(0, executor.getQueue().size());
    }

    @Test
    public void whenUsingSingleThreadPool_thenTasksExecuteSequentially() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger counter = new AtomicInteger();

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(() -> {
            counter.set(1);
            latch.countDown();
        });
        service.execute(() -> {
            counter.compareAndSet(1, 2);
            latch.countDown();
        });

        latch.await(1000, TimeUnit.MILLISECONDS);
        assertEquals(2, counter.get());
    }

    @Test
    public void whenSchedulingTask_thenTaskExecutesWithinGivenPeriod() throws Exception {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        CountDownLatch latch = new CountDownLatch(1);
        log.info("Scheduling task at {}", new Date(System.currentTimeMillis()));
        service.schedule(() -> {
            log.info("Hello World!!");
            latch.countDown();
        }, 2000, TimeUnit.MILLISECONDS);

        latch.await(5000, TimeUnit.MILLISECONDS);
        log.info("task ended at {}", new Date(System.currentTimeMillis()));
    }

    @Test
    public void whenSchedulingTaskWithFixedPeriod_thenTaskExecutesMultipleTimes() throws Exception {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        CountDownLatch latch = new CountDownLatch(3);
        log.info("Scheduling task at {}", new Date(System.currentTimeMillis()));

        /*ScheduledFuture<?> future = service.scheduleAtFixedRate(() -> {
            log.info("Hello World!! @ {}", new Date(System.currentTimeMillis()));
            latch.countDown();
        }, 2000, 1000, TimeUnit.MILLISECONDS);*/

        ScheduledFuture<?> future = service.scheduleWithFixedDelay(() -> {
            log.info("Hello World!! @ {}", new Date(System.currentTimeMillis()));
            latch.countDown();
        }, 2000, 1000, TimeUnit.MILLISECONDS);

        latch.await(5000, TimeUnit.MILLISECONDS);
        log.info("task ended at {}", new Date(System.currentTimeMillis()));
        future.cancel(true);
    }

    @Test
    public void whenUsingForkJoinPool_thenSumOfTreeElementsIsCalculatedCorrectly() {
        TreeNode tree = new TreeNode(5, new TreeNode(3), new TreeNode(2, new TreeNode(2), new TreeNode(8)));

        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        int sum = forkJoinPool.invoke(new CountingTask(tree));

        assertEquals(20, sum);
    }
}
