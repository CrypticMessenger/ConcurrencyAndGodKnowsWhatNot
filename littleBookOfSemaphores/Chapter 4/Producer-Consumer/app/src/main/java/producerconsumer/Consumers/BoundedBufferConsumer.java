package producerconsumer.Consumers;

import java.util.Random;
import java.util.concurrent.Semaphore;

import producerconsumer.Buffer.Buffer;

public class BoundedBufferConsumer implements Consumer {
     private final Buffer<String> buffer;
    private final Semaphore bufferGater;
    private final Semaphore bufferCounter;
    private final Semaphore remainingCapacity;
    private final Random random = new Random();

    public BoundedBufferConsumer(Buffer<String> buffer, Semaphore bufferGater, Semaphore bufferCounter, Semaphore remainingCapacity) {
        this.buffer = buffer;
        this.bufferGater = bufferGater;
        this.bufferCounter = bufferCounter;
        this.remainingCapacity = remainingCapacity;
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
                remainingCapacity.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    

}
