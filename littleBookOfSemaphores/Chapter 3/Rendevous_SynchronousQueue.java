import java.util.concurrent.SynchronousQueue;

public class Rendevous_SynchronousQueue {
    public static void main(String[] args) {
        final SynchronousQueue<String> queue = new SynchronousQueue<>();

        Thread a = new Thread(() -> {
            try {
                System.out.println("Executing statement a1");
                queue.put("a1 done");
                System.out.println("Executing statement a2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread b = new Thread(() -> {
            try {
                System.out.println("Executing statement b1");
                queue.take();
                System.out.println("Executing statement b2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        a.start();
        b.start();

    }
    
}
