import java.util.concurrent.Semaphore;

/**
 * Solves multiplex solution to mutual exclusion solution. - just increment initialization number 
 * of permits to allow more than one thread to access critical section at a time.
 * Puzzle: Generalize the solution so that it allows multiple threads to
run in the critical section at the same time, but it enforces an upper limit on
the number of concurrent threads. In other words, no more than n threads can
run in the critical section at the same time.
This pattern is called a multiplex. In real life, the multiplex problem occurs
at busy nightclubs where there is a maximum number of people allowed in the 
building at a time, either to maintain fire safety or to create the illusion of
exclusivity.
At such places a bouncer usually enforces the synchronization constraint by
keeping track of the number of people inside and barring arrivals when the room
is at capacity. Then, whenever one person leaves another is allowed to enter.

 */
public class Mutex {
    static class UnsafeCounter {
        int count = 10;
    }

    
    public static void main(String[] args) {
        UnsafeCounter counter = new UnsafeCounter();
        final Semaphore mutex = new Semaphore(1);

        final Thread t1 = new Thread (() -> {
            try {
                mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.count++;
            mutex.release();
        });

        final Thread t2 = new Thread (() -> {
            try {
                mutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.count++;
            mutex.release();
        });

        t1.start();
        t2.start();
    }
    
}
