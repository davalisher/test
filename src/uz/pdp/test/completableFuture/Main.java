package uz.pdp.test.completableFuture;

import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        m4();
        for (int i = 0; i <10 ; i++) {
            Thread.sleep(100);
            System.out.println("Message "+i);

        }


    }
    static  void completableFuture1() throws ExecutionException, InterruptedException {
        ExecutorService executorService=Executors.newFixedThreadPool(10);
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(5000);
                System.out.println("M1 method is called");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },executorService).cancel(true);


    }
    static  void completableFuture2() throws ExecutionException, InterruptedException {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("M2 method is called");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }
    static  void completableFuture3() throws ExecutionException, InterruptedException {
//        ExecutorService executorService= Executors.newFixedThreadPool(10);
         CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("M3 method is called");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }
    static void future1(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);

    }
    static void m4(){
        System.out.println("M4 method is called");
    }
}
