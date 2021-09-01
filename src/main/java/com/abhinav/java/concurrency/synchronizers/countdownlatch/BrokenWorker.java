package com.abhinav.java.concurrency.synchronizers.countdownlatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BrokenWorker implements Runnable{
    private Logger log = LoggerFactory.getLogger(BrokenWorker.class);

    private final List<String> outputScraper;
    private final CountDownLatch latch;

    public BrokenWorker(List<String> outputScraper, CountDownLatch latch) {
        this.outputScraper = outputScraper;
        this.latch = latch;
    }

    @Override
    public void run() {
        if (true) {
            throw new RuntimeException("Oh dear, I'm a BrokenWorker");
        }
        latch.countDown();
        outputScraper.add("Counted down");
    }
}
