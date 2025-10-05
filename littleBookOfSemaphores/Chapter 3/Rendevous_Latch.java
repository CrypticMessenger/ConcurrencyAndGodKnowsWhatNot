import java.util.concurrent.CountDownLatch;

class Rendevous_Latch {
    public static void main(String[] args) {
        final CountDownLatch latchA1Completed = new CountDownLatch(1);
        final CountDownLatch latchB1Completed = new CountDownLatch(1);
        Thread a = new Thread(() -> {
            try {
                System.out.println("Executing statement a1");
                latchA1Completed.countDown();
                latchB1Completed.await();
                System.out.println("Executing statement a2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread b = new Thread(() -> {
            try {
                System.out.println("Executing statement b1");
                latchB1Completed.countDown();
                latchA1Completed.await();
                System.out.println("Executing statement b2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        a.start();
        b.start();
    }
}