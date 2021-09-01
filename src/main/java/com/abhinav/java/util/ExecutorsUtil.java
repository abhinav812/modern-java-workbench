package com.abhinav.java.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorsUtil {
    public static List<Runnable> smartShutDown(ExecutorService service){
        List<Runnable> notExecutedTasks = new ArrayList<>();
        service.shutdown();
        try{
            if(!service.awaitTermination(1, TimeUnit.SECONDS)){
                notExecutedTasks = service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            notExecutedTasks = service.shutdownNow();
        }
        return notExecutedTasks;
    }

    public static List<Runnable> smartShutDown(ExecutorService service, long timeout, TimeUnit unit){
        List<Runnable> notExecutedTasks = new ArrayList<>();
        service.shutdown();
        try{
            if(!service.awaitTermination(timeout, unit)){
                notExecutedTasks = service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            notExecutedTasks = service.shutdownNow();
        }
        return notExecutedTasks;
    }

    public static List<Runnable> smartShutDown(ExecutorService service, Boolean forceShutdown){
        List<Runnable> notExecutedTasks = new ArrayList<>();
        service.shutdown();
        try{
            boolean shutdownStatus = service.awaitTermination(1, TimeUnit.SECONDS);
            if(!shutdownStatus && forceShutdown){
                notExecutedTasks = service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            if(forceShutdown){
                notExecutedTasks = service.shutdownNow();
            }
        }
        return notExecutedTasks;
    }
}
