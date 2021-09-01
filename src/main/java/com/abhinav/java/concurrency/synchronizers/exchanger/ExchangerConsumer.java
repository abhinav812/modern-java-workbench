package com.abhinav.java.concurrency.synchronizers.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Exchanger;


public class ExchangerConsumer implements Runnable {
    Logger log = LoggerFactory.getLogger(ExchangerConsumer.class);

    private final Exchanger<Country> exchanger;

    public ExchangerConsumer(Exchanger<Country> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        var dummy = new Country("Dummy");

        try {
            // exchanging data with producer
            var received = (Country) exchanger.exchange(dummy);
            log.info("Got country object from Producer thread : {}", received.getCountryName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
