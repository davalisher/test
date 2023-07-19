package uz.pdp.test.concurrency_test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CustomThreadPoolExample {
    public static void main(String[] args) {
        Runnable task1 = () -> {
            try {
                System.out.println("Hi");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("Task1 ended");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Runnable task2 = () -> {
            try {
                System.out.println("HEllo");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("Task2 ended");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Runnable task3 = () -> {
            try {
                System.out.println("How are yoy");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("Task3 ended");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Runnable task4 = () -> {
            try {
                System.out.println("How do you do");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("Task4 ended");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        CustomThreadPool customThreadPool = new CustomThreadPool(2);
        customThreadPool.execute(task1);
        customThreadPool.execute(task2);
        customThreadPool.execute(task3);
        customThreadPool.execute(task4);
    }
}

class CustomThreadPool implements MyExecutor {
    final int capacity;
    LinkedBlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    MyExecutor executor = new MyExecutorImpl();
    Thread[] threads;
    boolean isStarted;

    public CustomThreadPool(int capacity) {
        this.capacity = capacity;
        threads = new Thread[capacity];
    }

    @Override
    public void execute(Runnable runnable) {
        tasks.offer(runnable);
        if (!isStarted) {
            isStarted = true;
            threadPoolExecute();
        }

    }

    private synchronized void threadPoolExecute() {
        {
            while (true) {
                for (int i = 0; i < capacity; i++) {
                    if (tasks.size() > 0) {
                        executor = new MyExecutorImpl();
                        executor.execute(tasks.poll());
                    }
                }
            }
        }
    }
}