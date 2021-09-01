package com.abhinav.java.concurrency.blockingqueue;

import com.abhinav.java.util.ExecutorsUtil;

import java.util.concurrent.*;

public class BlockingQueueDemo {
    public static void main(String[] args) {
        int BOUNDS = 10;
        int N_PRODUCERS = Runtime.getRuntime().availableProcessors();
        int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
        int POISON_PILL = Integer.MAX_VALUE;
        int POISON_PILL_COUNT = N_PRODUCERS / N_CONSUMERS;
        int mod = N_CONSUMERS % N_PRODUCERS;

        BlockingQueue<Integer> numbersQueue = new LinkedBlockingQueue<>(BOUNDS);
        ExecutorService service = Executors.newFixedThreadPool(10);

        // START the producers
        for (int i = 0; i < N_PRODUCERS; i++) {
            //new Thread(new NumberProducerBQ(numbersQueue, POISON_PILL, POISON_PILL_COUNT)).start();
            service.submit(new NumberProducerBQ(numbersQueue, POISON_PILL, POISON_PILL_COUNT));
        }

        // START the consumers
        for (int i = 0; i < N_CONSUMERS; i++) {
            //new Thread(new NumberConsumerBQ(numbersQueue, POISON_PILL)).start();
            service.submit(new NumberConsumerBQ(numbersQueue, POISON_PILL));
        }

        service.submit(new NumberProducerBQ(numbersQueue, POISON_PILL, POISON_PILL_COUNT + mod));

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ExecutorsUtil.smartShutDown(service);
    }
}

