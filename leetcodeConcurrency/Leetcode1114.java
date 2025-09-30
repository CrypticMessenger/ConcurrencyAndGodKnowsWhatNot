package leetcodeConcurrency;

import java.util.concurrent.CountDownLatch;

// https://leetcode.com/problems/print-in-order/
// Solution: https://leetcode.com/problems/print-in-order/solutions/7189213/java-solution-using-latches/

import java.util.concurrent.Semaphore;

public class Leetcode1114 {
    class Foo {
        private final Semaphore sem1, sem2;

        public Foo() {
            sem1 = new Semaphore(0);
            sem2 = new Semaphore(0);
        }

        public void first(Runnable printFirst) throws InterruptedException {
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            sem1.release();

        }

        public void second(Runnable printSecond) throws InterruptedException {
            sem1.acquire();
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();

            sem2.release();
        }

        public void third(Runnable printThird) throws InterruptedException {
            sem2.acquire();
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
        }
    }

    class Foo_Latch {
        private final CountDownLatch latch1, latch2;

        public Foo_Latch() {
            latch1 = new CountDownLatch(1);
            latch2 = new CountDownLatch(1);
        }

        public void first(Runnable printFirst) throws InterruptedException {
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            latch1.countDown();

        }

        public void second(Runnable printSecond) throws InterruptedException {
            latch1.await();
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();

            latch2.countDown();
        }

        public void third(Runnable printThird) throws InterruptedException {
            latch2.await();
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
        }
    }
}
