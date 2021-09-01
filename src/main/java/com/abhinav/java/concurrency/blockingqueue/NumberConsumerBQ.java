package com.abhinav.java.concurrency.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class NumberConsumerBQ implements Runnable {
    private final BlockingQueue<Integer> numbersQueue;
    private final int poisonPill;

    public NumberConsumerBQ(BlockingQueue<Integer> numbersQueue, int poisonPill) {
        this.numbersQueue = numbersQueue;
        this.poisonPill = poisonPill;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //Integer number = numbersQueue.take();
                Integer number = numbersQueue.poll(5, TimeUnit.SECONDS);

                if (number != null && number.equals(poisonPill)) {
                    System.out.println(Thread.currentThread().getName() + " *** Poison Pill *** ");
                    return;
                }
                System.out.println(Thread.currentThread().getName() + " result: " + number);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
