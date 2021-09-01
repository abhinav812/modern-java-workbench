package com.abhinav.java.concurrency;

import com.abhinav.java.concurrency.synchronizers.transferQueue.Consumer;
import com.abhinav.java.concurrency.synchronizers.transferQueue.Producer;
import com.abhinav.java.util.ExecutorsUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransferQueueIntegrationTest {
    Logger log = LoggerFactory.getLogger(TransferQueueIntegrationTest.class);

    @Test
    public void whenUseOneProducerAndNoConsumers_thenShouldFailWithTimeout() throws Exception {
        //given
        TransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        ExecutorService service = Executors.newFixedThreadPool(2);
        Producer producer = new Producer(transferQueue, "1", 3);

        //when
        service.execute(producer);

        //then
        ExecutorsUtil.smartShutDown(service, 5000, TimeUnit.MILLISECONDS);

        assertEquals(producer.numberOfProduceMessage.intValue() , 0);
    }

    @Test
    public void whenUseOneConsumerAndOneProducer_thenShouldProcessAllMessages() throws Exception {
        // given
        TransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        ExecutorService service = Executors.newFixedThreadPool(2);
        Producer producer = new Producer(transferQueue, "1", 3);
        Consumer consumer = new Consumer(transferQueue, "1", 3);

        // when
        service.execute(producer);
        service.execute(consumer);

        // then
        ExecutorsUtil.smartShutDown(service, 5000, TimeUnit.MILLISECONDS);

        assertEquals(producer.numberOfProduceMessage.intValue(), 3);
        assertEquals(consumer.numberOfMessagesToConsume.intValue(), 3);
    }

    @Test
    public void whenMultipleConsumersAndProducers_thenProcessAllMessages() throws Exception {
        // given
        TransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        ExecutorService service = Executors.newFixedThreadPool(3);
        Producer producer1 = new Producer(transferQueue, "1", 3);
        Consumer consumer1 = new Consumer(transferQueue, "1", 3);
        Producer producer2 = new Producer(transferQueue, "2", 3);
        Consumer consumer2 = new Consumer(transferQueue, "2", 3);

        // when
        service.execute(producer1);
        service.execute(consumer1);
        service.execute(producer2);
        service.execute(consumer2);

        // then
        ExecutorsUtil.smartShutDown(service, 15_000, TimeUnit.MILLISECONDS);

        assertEquals(producer1.numberOfProduceMessage.intValue(), 3);
        assertEquals(consumer1.numberOfMessagesToConsume.intValue(), 3);
        assertEquals(producer2.numberOfProduceMessage.intValue(), 3);
        assertEquals(consumer2.numberOfMessagesToConsume.intValue(), 3);
    }
}
