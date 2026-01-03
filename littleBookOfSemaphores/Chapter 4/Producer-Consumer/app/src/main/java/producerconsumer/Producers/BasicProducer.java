package producerconsumer.Producers;

import java.util.Random;
import java.util.concurrent.Semaphore;

import producerconsumer.Buffer.Buffer;

/**
 * Basic implementation of a Producer thread.
 */
public class BasicProducer implements Producer {
    private final Buffer<String> buffer;
    private final Semaphore bufferGater;
    private final Semaphore bufferCounter;
    private final Random random = new Random();
    private static int eventCount = 0;

    public BasicProducer(Buffer<String> buffer, Semaphore bufferGater, Semaphore bufferCounter) {
        this.buffer = buffer;
        this.bufferGater = bufferGater;
        this.bufferCounter = bufferCounter;
    }

    private String waitForEvent() {
        // Simulate waiting for an external event
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Event-" + (++eventCount);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String event = waitForEvent();
            try {
                bufferGater.acquire();
                buffer.add(event);
                bufferCounter.release();
                bufferGater.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
