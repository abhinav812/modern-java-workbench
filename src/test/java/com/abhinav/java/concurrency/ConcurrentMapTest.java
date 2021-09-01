package com.abhinav.java.concurrency;

import com.abhinav.java.util.ExecutorsUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrentMapTest {
    Logger log = LoggerFactory.getLogger(ConcurrentMapTest.class);

    @Test
    public void givenHashMap_whenSumParallel_thenError() throws Exception {
        Map<String, Integer> map = new HashMap<>();
        List<Integer> sumList = parallelSum100(map, 100);

        assertNotEquals(1, sumList.stream().distinct().count());
        long wrongResultCount = sumList.stream()
                .filter(num -> num != 100)
                .count();
        log.info("Wrong result count: {}", wrongResultCount);
        assertTrue(wrongResultCount > 0);

    }

    @Test
    public void givenConcurrentMap_whenSumParallel_thenCorrect() throws Exception{
        Map<String, Integer> map = new ConcurrentHashMap<>();
        List<Integer> sumList = parallelSum100(map, 100);

        assertEquals(1, sumList.stream().distinct().count());
        long wrongResultCount = sumList.stream()
                .filter(num -> num != 100)
                .count();
        log.info("Wrong result count: {}", wrongResultCount);
        assertEquals(0, wrongResultCount);
    }

    private List<Integer> parallelSum100(Map<String, Integer> map, int executionTime) throws InterruptedException {
        List<Integer> sumList = new ArrayList<>(1000);

        for (int i = 0; i < executionTime; i++) {
            map.put("test", 0);
            ExecutorService service = Executors.newFixedThreadPool(4);
            for (int j = 0; j < 10; j++) {
                service.execute(() -> {
                            for (int k = 0; k < 10; k++) {
                                map.computeIfPresent("test", (key, value) -> value + 1);
                            }
                        }
                );
            }
            ExecutorsUtil.smartShutDown(service);
            sumList.add(map.get("test"));
        }
        return sumList;
    }
}
