package producerconsumer.Buffer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The basic, unsafe implementation of the buffer.
 */
public class BasicBuffer<T> implements Buffer<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    public BasicBuffer(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void add(T event) {
        // TODO: Add synchronization here to protect the buffer
        // and to signal consumers.

        System.out.println("[Buffer] Adding event: " + event);
        queue.add(event);

        // TODO: Signal that an item is available.
    }

    @Override
    public T get() {
        // TODO: Add synchronization here to protect the buffer
        // and to signal producers if it was full.

        T event = queue.poll();
        System.out.println("[Buffer] Got event: " + event);
        return event;
    }
}
