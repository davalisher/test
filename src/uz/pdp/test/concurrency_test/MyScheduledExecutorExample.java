package uz.pdp.test.concurrency_test;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class MyScheduledExecutorExample {
    public static void main(String[] args) {

       /* Runnable task1 = () -> {
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
        };*/
        Runnable task1 = () -> System.out.println("Hi");
        Runnable task2 = () -> System.out.println("Hello");
        Runnable task3 = () -> System.out.println("How are you");
        Runnable task4 = () -> System.out.println("Hw do you do");
        MyScheduledExecutor myScheduledExecutor = new MyScheduledExecutor(3);
//        Executor myScheduledExecutor = Executors.newSingleThreadExecutor();
        myScheduledExecutor.execute(task1);
        myScheduledExecutor.execute(task2);
        myScheduledExecutor.execute(task3);
        myScheduledExecutor.execute(task4);

    }
}

class MyScheduledExecutor implements MyExecutor {
    final Queue<Runnable> tasks = new ArrayDeque<>();
    final MyExecutor executor = new MyExecutorImpl();
    Runnable current;
    final long delay;

    public MyScheduledExecutor(long delay) {
        this.delay = delay;
    }

    @Override
    public void execute(Runnable runnable) {
        tasks.offer(runnable);
        if (current == null) {
            scheduledNext();
        }
    }


    protected synchronized void scheduledNext() {
        try {
            while ((current = tasks.poll()) != null) {
                TimeUnit.SECONDS.sleep(delay);
                executor.execute(current);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
