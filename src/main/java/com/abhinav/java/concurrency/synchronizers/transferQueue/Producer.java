package com.abhinav.java.concurrency.synchronizers.transferQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable{
    Logger log = LoggerFactory.getLogger(Producer.class);

    private final TransferQueue<String> transferQueue;
    private final String name;
    private final Integer numberOfMessageToProduce;
    public final AtomicInteger numberOfProduceMessage = new AtomicInteger();

    public Producer(TransferQueue<String> transferQueue, String name, Integer numberOfMessageToProduce) {
        this.transferQueue = transferQueue;
        this.name = name;
        this.numberOfMessageToProduce = numberOfMessageToProduce;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfMessageToProduce; i++) {
            try{
               log.info("Producer: " + name + " is waiting to transfer...");
               boolean added = transferQueue.tryTransfer("A" + i, 2000, TimeUnit.MILLISECONDS);

               if(added){
                   numberOfProduceMessage.incrementAndGet();
                   log.info("Producer: " + name + " transferred element: A" + i);
               } else {
                   log.info("Producer: " + name + " can not add an element due to the timeout");
               }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
