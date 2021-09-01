package com.abhinav.java.concurrency.synchronizers.exchanger;

import com.abhinav.java.util.ExecutorsUtil;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerDemo {
    public static void main(String[] args) {
        var exchanger = new Exchanger<Country>();

        // Starting Producer and Consumer
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(new ExchangerProducer(exchanger));
        service.submit(new ExchangerConsumer(exchanger));

        ExecutorsUtil.smartShutDown(service);
    }
}
