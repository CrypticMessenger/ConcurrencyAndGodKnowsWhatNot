import java.util.concurrent.Phaser;

public class Rendevous_Phaser {
    public static void main(String[] args) {
        final Phaser barrier = new Phaser(2);
        Thread a = new Thread(() -> {
            System.out.println("Executing statement a1");
            try {
                barrier.arriveAndAwaitAdvance();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Executing statement a2");
        });
        Thread b = new Thread(() -> {
            System.out.println("Executing statement b1");
            try {
                barrier.arriveAndAwaitAdvance();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Executing statement b2");
        });
        a.start();
        b.start();
    }
    
}
