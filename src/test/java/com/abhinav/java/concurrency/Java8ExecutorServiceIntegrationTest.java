package com.abhinav.java.concurrency;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class Java8ExecutorServiceIntegrationTest {
    Logger log = LoggerFactory.getLogger(Java8ExecutorServiceIntegrationTest.class);

    private Runnable runnableTask;
    private Callable<String> callableTask;
    private List<Callable<String>> callabaleTasks;

    @BeforeEach
    public void init() {
        runnableTask = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                log.info("Running task");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        callableTask = () -> {
            TimeUnit.MILLISECONDS.sleep(500);
            return "Task's execution";
        };

        callabaleTasks = Lists.newArrayList(callableTask, callableTask, callableTask);
    }

    @Test
    public void creationSubmittingTaskShuttingDown_whenShutDown_thenCorrect(){
        ExecutorService service = Executors.newFixedThreadPool(10);

        service.submit(runnableTask);
        service.submit(callableTask);
        service.shutdown();

        assertTrue(service.isShutdown());
    }

    @Test
    public void creationSubmittingTasksShuttingDownNow_whenShutDownAfterAwating_thenCorrect(){
        ExecutorService service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            service.submit(callableTask);
        }

        List<Runnable> notExecitedTasks = smartShutDown(service);

        assertTrue(service.isShutdown());
        assertFalse(notExecitedTasks.isEmpty());
        assertTrue(notExecitedTasks.size() < 100);
    }

    private List<Runnable> smartShutDown(ExecutorService service){
        List<Runnable> notExecutedTasks = new ArrayList<>();
        service.shutdown();
        try{
            if(!service.awaitTermination(800, TimeUnit.MILLISECONDS)){
                notExecutedTasks = service.shutdownNow();
            };
        } catch (InterruptedException e) {
            e.printStackTrace();
            notExecutedTasks = service.shutdownNow();
        }
        return notExecutedTasks;
    }
}
