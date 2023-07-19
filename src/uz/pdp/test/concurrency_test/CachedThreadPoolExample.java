package uz.pdp.test.concurrency_test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CachedThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

        ExecutorService executorService = Executors.newCachedThreadPool();

//        for (int i = 0; i < 300; i++) {
//            final int a = i;
            Runnable runnable = () -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "=>");// + a);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };
            service.scheduleAtFixedRate(runnable, 0,1, TimeUnit.SECONDS);
//        }



    }
}
