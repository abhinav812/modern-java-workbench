package com.abhinav.java.concurrency.synchronizers.semaphore;

import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.concurrent.TimeUnit;

public class DelayQueueUsingTimedSemaphore {
    private final TimedSemaphore semaphore;

    public DelayQueueUsingTimedSemaphore(long period, TimeUnit unit, int slotLimit) {
        this.semaphore = new TimedSemaphore(period, unit, slotLimit);
    }

    public boolean tryAdd() {
        return semaphore.tryAcquire();
    }

    public int availableSlots() {
        return semaphore.getAvailablePermits();
    }
}
