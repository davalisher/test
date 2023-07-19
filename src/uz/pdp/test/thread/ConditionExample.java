package uz.pdp.test.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionExample {
    static BoundedBuffer<Integer> boundedBuffer = new BoundedBuffer<>();

    public static void main(String[] args) {
        new Thread(new A(boundedBuffer)).start();
        new Thread(new B(boundedBuffer)).start();


    }

    static class BoundedBuffer<E> {
        final Lock lock = new ReentrantLock();
        final Condition notFull = lock.newCondition();
        final Condition notEmpty = lock.newCondition();

        final Object[] items = new Object[10];
        int putptr, takeptr, count;

        public void put(E x) throws InterruptedException {
            lock.lock();
            try {
                while (count == items.length)
                    notFull.await();
                items[putptr] = x;
                if (++putptr == items.length) putptr = 0;
                ++count;
                notEmpty.signal();
            } finally {
                lock.unlock();
            }
        }

        public E take() throws InterruptedException {
            lock.lock();
            try {
                while (count == 0)
                    notEmpty.await();
                E x = (E) items[takeptr];
                if (++takeptr == items.length) takeptr = 0;
                --count;
                notFull.signal();
                return x;
            } finally {
                lock.unlock();
            }
        }
    }

    static class A implements Runnable {
        BoundedBuffer<Integer> boundedBuffer;

        public A(BoundedBuffer<Integer> boundedBuffer) {
            this.boundedBuffer = boundedBuffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 30; i++) {
                    boundedBuffer.put(i);
                    System.out.println("addin =>" + i);
                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class B implements Runnable {
        BoundedBuffer<Integer> boundedBuffer;

        public B(BoundedBuffer<Integer> boundedBuffer) {
            this.boundedBuffer = boundedBuffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 30; i++) {
                    System.out.println("taking =>" + boundedBuffer.take());
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
