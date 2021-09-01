package com.abhinav.java.concurrency;

import com.abhinav.java.concurrency.synchronizers.phaser.LongRunningAction;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import static org.junit.jupiter.api.Assertions.*;


public class PhaserUnitTest {
    Logger log = LoggerFactory.getLogger(PhaserUnitTest.class);

    @Test
    public void givenPhaser_whenCoordinateWorksBetweenThreads_thenShouldCoordinateBetweenMultiplePhases() {
        //given
        ExecutorService service = Executors.newCachedThreadPool();
        Phaser ph = new Phaser(1);
        int currentPhase = ph.getPhase();
        assertEquals(0, currentPhase);

        //when
        service.submit(new LongRunningAction("thread-1", ph));
        service.submit(new LongRunningAction("thread-2", ph));
        service.submit(new LongRunningAction("thread-3", ph));

        //then
        currentPhase = ph.getPhase();
        ph.arriveAndAwaitAdvance();
        log.info("Phase: {} ended", currentPhase);
        assertEquals(1, ph.getPhase());

        //and
        service.submit(new LongRunningAction("thread-4", ph));
        service.submit(new LongRunningAction("thread-5", ph));
        currentPhase = ph.getPhase();
        ph.arriveAndAwaitAdvance();
        log.info("Phase: {} ended", currentPhase);
        assertEquals(2, ph.getPhase());

        ph.arriveAndAwaitAdvance();
    }
}
