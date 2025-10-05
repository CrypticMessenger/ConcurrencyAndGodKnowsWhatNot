import java.util.concurrent.Semaphore;

public class Leader implements Runnable, Dancer {
    private final Semaphore followerArrived;

    public Leader(final Semaphore followerArrived) {
        
        this.followerArrived = followerArrived;
    }

    @Override
    public void run() {
        try {
            System.out.println("Leader has arrived.");
            followerArrived.acquire();
            dance("Leader");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}