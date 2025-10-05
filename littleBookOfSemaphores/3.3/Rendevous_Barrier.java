import java.util.concurrent.CyclicBarrier;

public class Rendevous_Barrier {
    public static void main(String[] args) {
        final CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            // This task runs when both threads reach the barrier
            System.out.println("Both threads have reached the barrier. Proceeding...");
        });
        Thread a = new Thread(() -> {
            System.out.println("Executing statement a1");
            try {
                barrier.await();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Executing statement a2");
        });
        Thread b = new Thread(() -> {
            System.out.println("Executing statement b1");
            try {
                barrier.await();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Executing statement b2");
        });
        a.start();
        b.start();
    }
    
}
