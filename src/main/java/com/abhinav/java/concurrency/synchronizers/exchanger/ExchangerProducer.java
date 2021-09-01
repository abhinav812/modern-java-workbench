package com.abhinav.java.concurrency.synchronizers.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Exchanger;

public class ExchangerProducer implements Runnable {
    Logger log = LoggerFactory.getLogger(ExchangerProducer.class);
    private final Exchanger<Country> exchanger;

    public ExchangerProducer(Exchanger<Country>exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        Country country;
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) country = new Country("India");
            else country = new Country("Bhutan");

            try {
                // exchanging data with consumer
                Country received = (Country) exchanger.exchange(country);
                log.info("Got country object from Consumer thread : {}", received.getCountryName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
