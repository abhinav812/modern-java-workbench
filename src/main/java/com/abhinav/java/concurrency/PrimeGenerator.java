package com.abhinav.java.concurrency;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("java:S106")
public class PrimeGenerator {
    public static void main(String[] args) {
        Thread task = new PrimeGeneratorTask();
        task.start();

        // Wait 5 seconds
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }

        // Interrupt the prime number generator
        task.interrupt();

        // Write information about the status of the Thread
        System.out.printf("Main: Status of the Thread: %s%n", task.getState());
        System.out.printf("Main: isInterrupted: %s%n", task.isInterrupted());
        System.out.printf("Main: isAlive: %s%n", task.isAlive());
    }
}

@SuppressWarnings("java:S106")
class PrimeGeneratorTask extends Thread {

    @Override
    public void run() {
        long number = 1L;

        // This bucle never ends... until is interrupted
        while (true) {
            if (isPrime(number)) {
                System.out.printf("Number %d is Prime%n", number);
            }
            // When is interrupted, write a message and ends
            if (isInterrupted()) {
                System.out.printf("The Prime Generator has been Interrupted%n");
                return;
            }
            number++;
        }
    }

    /**
     * Method that calculate if a number is prime or not
     *
     * @param number : The number
     * @return A boolean value. True if the number is prime, false if not.
     */
    private boolean isPrime(long number) {
        if (number <= 2) {
            return true;
        }
        for (long i = 2; i < number; i++) {
            if ((number % i) == 0) {
                return false;
            }
        }
        return true;
    }
}
