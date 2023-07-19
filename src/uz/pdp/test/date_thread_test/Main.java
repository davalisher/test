package uz.pdp.test.date_thread_test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        MyRunnable runnable = new MyRunnable(dateFormat);
        ExecutorService executorService=Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            executorService.execute(runnable);
        }

    }
}

class MyRunnable implements Runnable {
    private SimpleDateFormat dateFormat;

    public MyRunnable(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100);
            System.out.println(dateFormat.format(new Date()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
