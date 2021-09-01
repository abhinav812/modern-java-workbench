package com.abhinav.java.concurrency.synchronizers.semaphore;

import java.util.concurrent.Semaphore;

public class CounterUsingSemaphoreMutex {
    private Semaphore mutex;
    private int count;

    public CounterUsingSemaphoreMutex() {
        mutex = new Semaphore(1);
        count = 0;
    }

    public void increase() throws InterruptedException {
        mutex.acquire();
        this.count = this.count + 1;
        Thread.sleep(1000);
        mutex.release();
    }

    public int getCount() {
        return this.count;
    }

    public boolean hasQueuedThread() {
        return mutex.hasQueuedThreads();
    }
}
