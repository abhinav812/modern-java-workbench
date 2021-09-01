package com.abhinav.java.concurrency.synchronizers.cyclicbarrier;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    private Logger log = LoggerFactory.getLogger(CyclicBarrierDemo.class);

    private final CyclicBarrier barrier;
    private final List<List<Integer>> partialResults;
    private final int NUM_PARTIAL_RESULTS;
    private final int NUM_WORKERS;

    public CyclicBarrierDemo(List<List<Integer>> partialResults, int numberOfPartialResults, int numWorkers, Runnable barrierAction) {
        this.partialResults = partialResults;
        this.NUM_PARTIAL_RESULTS = numberOfPartialResults;
        this.NUM_WORKERS = numWorkers;

        this.barrier = new CyclicBarrier(NUM_WORKERS, barrierAction);
    }

    public void runSimulation() {
        log.info("Spawning {} worker threads to compute {} partial results each", NUM_WORKERS, NUM_PARTIAL_RESULTS);
        for (int i = 0; i < NUM_WORKERS; i++) {
            Thread worker = new Thread(new NumberCruncherTask(NUM_PARTIAL_RESULTS, partialResults, barrier));
            worker.setName("Thread " + i);
            worker.start();
        }
    }

    public static void main(String[] args) {
        final List<List<Integer>> partialResults = Collections.synchronizedList(Lists.newArrayList());
        int NUM_PARTIAL_RESULTS = 3;
        int NUM_WORKERS = 5;
        final Thread barrierAction = new Thread(new AggregatorTask(NUM_WORKERS, NUM_PARTIAL_RESULTS, partialResults));

        CyclicBarrierDemo demo = new CyclicBarrierDemo(partialResults, NUM_PARTIAL_RESULTS, NUM_WORKERS, barrierAction);
        demo.runSimulation();
    }
}

class NumberCruncherTask implements Runnable {
    private Logger log = LoggerFactory.getLogger(NumberCruncherTask.class);

    private volatile int NUM_PARTIAL_RESULTS;
    private final Random random;
    private final List<List<Integer>> partialResults;
    private final CyclicBarrier barrier;

    public NumberCruncherTask(int numberOfPartialResults, List<List<Integer>> partialResults, CyclicBarrier barrier) {
        this.NUM_PARTIAL_RESULTS = numberOfPartialResults;
        this.partialResults = partialResults;
        this.barrier = barrier;
        this.random = new Random(10);
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        List<Integer> partialResult = new ArrayList();

        //Crunch some numbers and store the partial results
        for (int i = 0; i < NUM_PARTIAL_RESULTS; i++) {
            Integer num = random.nextInt(10);
            log.info("{}: Crunching some numbers! Final result - {}", threadName, num);
            partialResult.add(num);
        }
        partialResults.add(partialResult);

        try {
            log.info("{} - waiting for others to reach barrier", threadName);
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class AggregatorTask implements Runnable {
    private Logger log = LoggerFactory.getLogger(AggregatorTask.class);

    private volatile int NUM_WORKERS;
    private volatile int NUM_PARTIAL_RESULTS;
    private final List<List<Integer>> partialResults;

    public AggregatorTask(int numWorkers, int numberOfPartialResults, List<List<Integer>> partialResults) {
        this.NUM_WORKERS = numWorkers;
        this.NUM_PARTIAL_RESULTS = numberOfPartialResults;
        this.partialResults = partialResults;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        log.info(threadName + ": Computing sum of " + NUM_WORKERS
                + " workers, having " + NUM_PARTIAL_RESULTS + " results each.");
        int sum = 0;
        for (List<Integer> threadResult : partialResults) {
            log.info("Adding..");
            for (Integer partialResult : threadResult) {
                log.info("partialResult - {}", partialResult);
                sum += partialResult;
            }
            log.info("*********");
        }
        log.info("{} - Final result: {}", threadName, sum);
    }
}

