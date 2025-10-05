

import java.util.concurrent.Semaphore;

public class Rendevous_Semaphore {

    public static void main(String[] args) {
        final Semaphore a1Completed = new Semaphore(0);
        final Semaphore b1Completed = new Semaphore(0);

        Thread a = new Thread(() -> {
            System.out.println("Executing statement a1");
            a1Completed.release();
            try {
                b1Completed.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Executing statement a2");
        });

        Thread b = new Thread(() -> {
            System.out.println("Executing statement b1");
            b1Completed.release();
            try {
                a1Completed.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Executing statement b2");
        });

        a.start();
        b.start();
    }

}
