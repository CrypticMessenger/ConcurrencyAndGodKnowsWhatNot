package producerconsumer;

import java.util.concurrent.Semaphore;

import producerconsumer.Buffer.Buffer;
import producerconsumer.Buffer.BasicBuffer;
import producerconsumer.Consumers.Consumer;
import producerconsumer.Consumers.BasicConsumer;
import producerconsumer.Producers.Producer;
import producerconsumer.Producers.BasicProducer;

/**
 * Main class to orchestrate the Producer and Consumer threads.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Producer-Consumer Puzzle ===");
        System.out.println("Synchronization is NOT implemented yet.");
        System.out.println("Expected behavior: Race conditions or NullPointerExceptions.");
        System.out.println("--------------------------------");

        Buffer<String> buffer = new BasicBuffer<>(10);
        final Semaphore bufferGater = new Semaphore(1);
        final Semaphore bufferCounter = new Semaphore(0);
        Thread producerThread = new Thread(new BasicProducer(buffer, bufferGater, bufferCounter), "Producer-Thread");
        Thread consumerThread = new Thread(new BasicConsumer(buffer, bufferGater, bufferCounter), "Consumer-Thread");

        producerThread.start();
        consumerThread.start();

        // Allow it to run for a bit
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Stopping threads...");
        producerThread.interrupt();
        consumerThread.interrupt();
    }
}
