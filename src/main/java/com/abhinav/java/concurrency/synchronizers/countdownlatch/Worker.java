package com.abhinav.java.concurrency.synchronizers.countdownlatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {
    private Logger log = LoggerFactory.getLogger(Worker.class);

    private final List<String> outputScrapper;
    private final CountDownLatch latch;

    public Worker(List<String> outputScrapper, CountDownLatch latch) {
        this.outputScrapper = outputScrapper;
        this.latch = latch;
    }

    @Override
    public void run() {
        doSomeWork();
        outputScrapper.add("Counted down");
        latch.countDown();
    }

    private void doSomeWork() {
        log.info("Waving magic wand!!!");
    }
}
