package producerconsumer.Consumers;

import java.util.Random;
import java.util.concurrent.Semaphore;

import producerconsumer.Buffer.Buffer;

/**
 * Basic implementation of a Consumer thread.
 */
public class BasicConsumer implements Consumer {
    private final Buffer<String> buffer;
    private final Semaphore bufferGater;
    private final Semaphore bufferCounter;
    private final Random random = new Random();

    public BasicConsumer(Buffer<String> buffer, Semaphore bufferGater, Semaphore bufferCounter) {
        this.buffer = buffer;
        this.bufferGater = bufferGater;
        this.bufferCounter = bufferCounter;
    }

    private void process(String event) {
        // Simulate event processing
        System.out.println("[Consumer] Processing " + event + "...");
        try {
            Thread.sleep(random.nextInt(1500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("[Consumer] Finished processing " + event);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                bufferCounter.acquire();
                bufferGater.acquire();
                String event = buffer.get();
                bufferGater.release();
                if (event != null) {
                    process(event);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
