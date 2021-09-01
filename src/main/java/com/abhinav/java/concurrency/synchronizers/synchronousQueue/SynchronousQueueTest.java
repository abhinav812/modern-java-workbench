package com.abhinav.java.concurrency.synchronizers.synchronousQueue;


import com.abhinav.java.util.ExecutorsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SynchronousQueueTest {
    static Logger log = LoggerFactory.getLogger(SynchronousQueueTest.class);

    public static void main(String[] args) {
        testDataExchangeWithSharedState();

        testDataExchangeWithSynchronousQueue();
    }

    public static void testDataExchangeWithSharedState() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        AtomicInteger sharedState = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);

        Runnable producerWithSharedState = () -> {
            Integer producedElement = ThreadLocalRandom.current().nextInt();
            log.info("Saving an element: " + producedElement + " to the exchange point");
            sharedState.set(producedElement);
            latch.countDown();
        };
        Runnable consumerWithSharedState = () -> {
            try {
                latch.await();
                Integer consumedElement = sharedState.get();
                log.info("consumed an element: " + consumedElement + " from the exchange point");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        service.execute(producerWithSharedState);
        service.execute(consumerWithSharedState);

        ExecutorsUtil.smartShutDown(service);
    }

    public static void testDataExchangeWithSynchronousQueue() {
        ExecutorService service = Executors.newFixedThreadPool(2);
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();

        Runnable producerWithQueue = () -> {
            try {
                Integer producedElement = ThreadLocalRandom.current().nextInt();
                log.info("Saving an element: " + producedElement + " to the exchange point");
                queue.put(producedElement);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Runnable consumerWithQueue = () -> {
            try {
                Integer consumedElement = queue.take();
                log.info("consumed an element: " + consumedElement + " from the exchange point");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        service.execute(producerWithQueue);
        service.execute(consumerWithQueue);

        ExecutorsUtil.smartShutDown(service);
    }


}
