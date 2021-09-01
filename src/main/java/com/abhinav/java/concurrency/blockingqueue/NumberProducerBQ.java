package com.abhinav.java.concurrency.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class NumberProducerBQ implements Runnable {
    private final BlockingQueue<Integer> numbersQueue;
    private final int poisonPill;
    private final int poisonPillCount;

    public NumberProducerBQ(BlockingQueue<Integer> numbersQueue, int poisonPill, int poisonPillCount) {
        this.numbersQueue = numbersQueue;
        this.poisonPill = poisonPill;
        this.poisonPillCount = poisonPillCount;
    }

    @Override
    public void run() {
        try {
            generateNumbers();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void generateNumbers() throws InterruptedException {
        // Generate 100 numbers and put those on queue
        for (int i = 0; i < 100; i++) {
            int number = ThreadLocalRandom.current().nextInt(100);
            System.out.println("Putting "+ number + " in queue");
            numbersQueue.put(number);
        }

        // Generate poison pill for each consumer
        for (int i = 0; i < poisonPillCount; i++) {
            System.out.println("Putting poisonPill in queue");
            numbersQueue.put(poisonPill);
        }
    }
}