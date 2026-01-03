package producerconsumer.Buffer;

/**
 * Interface for the shared buffer.
 * Implementations should handle synchronization.
 */
public interface Buffer<T> {
    void add(T event);

    T get();
}
