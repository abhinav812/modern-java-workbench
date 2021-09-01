package com.abhinav.java.concurrency.synchronizers.countdownlatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WaitingWorker implements Runnable{
    private final Logger log = LoggerFactory.getLogger(WaitingWorker.class);

    private final List<String> outputScrapper;
    private final CountDownLatch latch;
    private final CountDownLatch callingThreadBlocker;
    private final CountDownLatch completedThreadCounter;

    public WaitingWorker(List<String> outputScrapper,
                         CountDownLatch latch,
                         CountDownLatch callingThreadBlocker,
                         CountDownLatch completedThreadCounter) {
        this.outputScrapper = outputScrapper;
        this.latch = latch;
        this.callingThreadBlocker = callingThreadBlocker;
        this.completedThreadCounter = completedThreadCounter;
    }

    @Override
    public void run() {
        latch.countDown(); // Signal that worker creation DONE.
        try {
            callingThreadBlocker.await(); // Wait for the Signal, till all the workers created.
            doSomeWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            outputScrapper.add("Counted down");
            completedThreadCounter.countDown(); // Always down latch when job DONE
        }
    }

    private void doSomeWork() {
        log.info("Waving magic wand!!!");
    }
}
