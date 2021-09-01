package com.abhinav.java.concurrency.synchronizers.phaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class LongRunningAction implements Runnable {
    private Logger log = LoggerFactory.getLogger(LongRunningAction.class);

    private final String threadName;
    private final Phaser ph;

    public LongRunningAction(String threadName, Phaser ph) {
        this.threadName = threadName;
        this.ph = ph;
        ph.register();
    }

    @Override
    public void run() {
        log.info("This is phase {}.", ph.getPhase());
        log.info("Thread {} before long running action", threadName);
        ph.arriveAndAwaitAdvance();
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ph.arriveAndDeregister();
    }
}
