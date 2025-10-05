import java.util.concurrent.Semaphore;

public class Follower implements Runnable, Dancer {
    private final Semaphore leaderArrived;

    public Follower(final Semaphore leaderArrived) {
        this.leaderArrived = leaderArrived;
    }

    @Override
    public void run() {
        try {
            System.out.println("Follower has arrived.");
            leaderArrived.acquire();
            dance("Follower");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}