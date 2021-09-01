package com.abhinav.java.concurrency.synchronizers.semaphore;

import java.util.concurrent.Semaphore;

public class LoginQueueUsingSemaphore {
    private final Semaphore semaphore;

    public LoginQueueUsingSemaphore(int slotLimit) {
        this.semaphore = new Semaphore(slotLimit);
    }

    public boolean tryLogin() {
        return semaphore.tryAcquire();
    }

    public void logout() {
        semaphore.release();
    }

    public int availableSlots() {
        return semaphore.availablePermits();
    }
}
