package uz.pdp.test.concurrency_test;

import java.util.concurrent.TimeUnit;

public class MyExecutorTest {
    public static void main(String[] args) {

        MyExecutor executor = new MyExecutorImpl();
        Runnable task1 = () -> {
            try {
                System.out.println("Hi");
                TimeUnit.SECONDS.sleep(1);
//                System.out.println("Task1 ended");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Runnable task2 = () -> {
            try {
                System.out.println("HEllo");
                TimeUnit.SECONDS.sleep(1);
//                System.out.println("Task2 ended");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        executor.execute(task1);
        executor.execute(task2);

    }
}

class MyExecutorImpl implements MyExecutor {
    @Override
    public synchronized void execute(Runnable runnable) {
        new Thread(runnable).start();

    }
}
