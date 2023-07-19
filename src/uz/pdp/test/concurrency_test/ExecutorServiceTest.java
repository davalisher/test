package uz.pdp.test.concurrency_test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceTest {
    public static void main(String[] args) {
        Runnable task1 = () -> {
            try {
                System.out.println("Hi");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Task1 ended");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Runnable task2 = () -> {
            try {
                System.out.println("HEllo");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Task2 ended");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        ExecutorService executor=null;
        try {
            executor = Executors.newSingleThreadExecutor();

            executor.execute(task1);
            executor.execute(task2);
        } finally {
            executor.shutdown();
        }


    }
}
