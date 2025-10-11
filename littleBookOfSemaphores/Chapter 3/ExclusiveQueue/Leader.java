import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Leader implements Runnable, Dancer {
    private final Semaphore followerArrived;
    private final Semaphore leaderArrived;
    private final Semaphore mutex;
    private final CyclicBarrier rendevous;
    private AtomicInteger follower;
    private AtomicInteger leader;

    public Leader(final Semaphore followerArrived, final Semaphore leaderArrived, final Semaphore mutex, AtomicInteger follower, AtomicInteger leader, final CyclicBarrier rendevous) {
        this.followerArrived = followerArrived;
        this.leaderArrived = leaderArrived;
        this.mutex = mutex;
        this.follower = follower;
        this.leader = leader;
        this.rendevous = rendevous;
    }

    @Override
    public void run() {
        try {
            System.out.println("Leader has arrived.");
            mutex.acquire();
            if (follower.get() > 0) {
                follower.decrementAndGet();
                followerArrived.release();
            } else {
                leader.incrementAndGet();
                mutex.release();
                leaderArrived.acquire();
            }
            dance("Leader");
            rendevous.await();
            mutex.release();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }

    }
}