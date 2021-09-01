package com.abhinav.java.concurrency.synchronizers.transferQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    Logger log = LoggerFactory.getLogger(Consumer.class);

    private final TransferQueue<String> transferQueue;
    private final String name;
    public final  Integer numberOfMessagesToConsume;
    final AtomicInteger numberOfConsumedMessages = new AtomicInteger();

    public Consumer(TransferQueue<String> transferQueue, String name, Integer numberOfMessagesToConsume) {
        this.transferQueue = transferQueue;
        this.name = name;
        this.numberOfMessagesToConsume = numberOfMessagesToConsume;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfMessagesToConsume; i++) {
            try{
                log.info("Consumer: " + name + " is waiting to take element...");
                String element = transferQueue.take();
                longProcessing(element);
                log.info("Consumer: " + name + " received element: " + element);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void longProcessing(String element) throws InterruptedException {
        numberOfConsumedMessages.incrementAndGet();
        Thread.sleep(500);
    }
}
