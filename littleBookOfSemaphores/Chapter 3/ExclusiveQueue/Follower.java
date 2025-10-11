import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Follower implements Runnable, Dancer {
    private final Semaphore leaderArrived;
    private final Semaphore followerArrived;
    private final Semaphore mutex;
    private final CyclicBarrier rendevous;
    private AtomicInteger follower;
    private AtomicInteger leader;

    public Follower(final Semaphore leaderArrived, final Semaphore followerArrived, final Semaphore mutex, AtomicInteger follower, AtomicInteger leader, final CyclicBarrier rendevous) {
        this.leaderArrived = leaderArrived;
        this.followerArrived = followerArrived;
        this.mutex = mutex;
        this.follower = follower;
        this.leader = leader;
        this.rendevous = rendevous;
    }

    @Override
    public void run() {
        try {
            System.out.println("Follower has arrived.");
            mutex.acquire();
            if (leader.get() > 0) {
                leader.decrementAndGet();
                leaderArrived.release();
            } else {
                follower.incrementAndGet();
                mutex.release();
                followerArrived.acquire();
            }
            dance("Follower");
            rendevous.await();
        } catch (InterruptedException
        | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }

    }
}