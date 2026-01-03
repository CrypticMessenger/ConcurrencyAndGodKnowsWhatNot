package producerconsumer.Producers;

import java.util.Random;
import java.util.concurrent.Semaphore;

import producerconsumer.Buffer.Buffer;

public class BoundedBufferProducer implements Producer {
 private final Buffer<String> buffer;
    private final Semaphore bufferGater;
    private final Semaphore bufferCounter;
    private final Semaphore remainingCapacity;
    private final Random random = new Random();
    private static int eventCount = 0;

    public BoundedBufferProducer(Buffer<String> buffer, Semaphore bufferGater, Semaphore bufferCounter, Semaphore remainingCapacity) {
        this.buffer = buffer;
        this.bufferGater = bufferGater;
        this.bufferCounter = bufferCounter;
        this.remainingCapacity = remainingCapacity;
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
                remainingCapacity.acquire();
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
