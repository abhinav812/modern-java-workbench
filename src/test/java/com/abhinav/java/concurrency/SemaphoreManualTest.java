package com.abhinav.java.concurrency;

import com.abhinav.java.concurrency.synchronizers.semaphore.CounterUsingSemaphoreMutex;
import com.abhinav.java.concurrency.synchronizers.semaphore.DelayQueueUsingTimedSemaphore;
import com.abhinav.java.concurrency.synchronizers.semaphore.LoginQueueUsingSemaphore;
import com.abhinav.java.util.ExecutorsUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


public class SemaphoreManualTest {
    Logger log = LoggerFactory.getLogger(SemaphoreManualTest.class);
    @Test
    public void givenLoginQueue_whenReachLimit_thenBlocked() {
        int slot = 10;
        ExecutorService service = Executors.newFixedThreadPool(slot);

        LoginQueueUsingSemaphore loginQueue = new LoginQueueUsingSemaphore(slot);
        IntStream.range(0, slot)
                .forEach(user -> service.execute(loginQueue::tryLogin));
        ExecutorsUtil.smartShutDown(service);

        assertEquals(0, loginQueue.availableSlots());
        assertFalse(loginQueue.tryLogin());
    }

    @Test
    public void givenLoginQueue_whenLogout_thenSlotsAvailable() {
        int slot = 10;
        ExecutorService service = Executors.newFixedThreadPool(slot);

        LoginQueueUsingSemaphore loginQueue = new LoginQueueUsingSemaphore(slot);
        IntStream.range(0, slot)
                .forEach(user -> service.execute(() -> {
                    loginQueue.tryLogin();
                    loginQueue.logout();
                }));
        ExecutorsUtil.smartShutDown(service);

        assertEquals(10, loginQueue.availableSlots());
        assertTrue(loginQueue.tryLogin());
    }

    @Test
    public void givenDelayQueue_whenReachLimit_thenBlocked() {
        int slots = 50;
        ExecutorService service = Executors.newFixedThreadPool(slots);

        DelayQueueUsingTimedSemaphore semaphore = new DelayQueueUsingTimedSemaphore(1, TimeUnit.SECONDS, slots);

        IntStream.range(0, slots)
                .forEach(user -> service.execute(semaphore::tryAdd));
        ExecutorsUtil.smartShutDown(service);

        assertEquals(0, semaphore.availableSlots());
        assertFalse(semaphore.tryAdd());
    }

    @Test
    public void givenDelayQueue_whenTimePass_thenSlotsAvailable() throws Exception {
        int slots = 50;
        ExecutorService service = Executors.newFixedThreadPool(slots);

        DelayQueueUsingTimedSemaphore semaphore = new DelayQueueUsingTimedSemaphore(1, TimeUnit.SECONDS, slots);

        IntStream.range(0, slots)
                .forEach(user -> service.execute(semaphore::tryAdd));
        ExecutorsUtil.smartShutDown(service);

        TimeUnit.SECONDS.sleep(1);

        assertEquals(50, semaphore.availableSlots());
        assertTrue(semaphore.tryAdd());
    }

    @Test
    public void whenMutexAndMultipleThreads_thenBlocked() throws InterruptedException {
        int count = 5;
        ExecutorService service = Executors.newFixedThreadPool(count);
        CounterUsingSemaphoreMutex counter = new CounterUsingSemaphoreMutex();

        IntStream.range(0, count)
                .forEach(term -> service.execute(() -> {
                    try {
                        counter.increase();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }));
        //ExecutorsUtil.smartShutDown(service);
        service.shutdown();

        log.info("{}",counter.hasQueuedThread());
        Thread.sleep(5000);
        log.info("count: {}", counter.getCount());
    }
}
