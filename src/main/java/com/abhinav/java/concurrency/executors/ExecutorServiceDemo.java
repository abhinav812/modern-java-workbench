package com.abhinav.java.concurrency.executors;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

import static com.abhinav.java.util.ExecutorsUtil.smartShutDown;

public class ExecutorServiceDemo {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(ExecutorServiceDemo.class);
        ExecutorService service = Executors.newFixedThreadPool(10);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        Runnable runnableTask = () -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1500);
                log.info("Running task");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Callable<String> callabaleTask = () -> {
            TimeUnit.MILLISECONDS.sleep(500);
            return "Task's execution" + LocalDateTime.now();
        };

        List<Callable<String>> callabaleTasks = Lists.newArrayList(callabaleTask, callabaleTask, callabaleTask);

        try {
            ScheduledFuture<?> future = scheduledExecutorService.scheduleWithFixedDelay(runnableTask, 5, 1, TimeUnit.SECONDS);

            log.info("Result: "+ future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        smartShutDown(service);
        smartShutDown(scheduledExecutorService);
    }




}
