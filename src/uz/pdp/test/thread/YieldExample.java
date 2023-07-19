package uz.pdp.test.thread;

public class YieldExample {
    public static void main(String[] args) {
        MyRunnable  runnable1=new MyRunnable();
        MyRunnable  runnable2=new MyRunnable();
        MyRunnable  runnable3=new MyRunnable();
        Thread thread1=new Thread(runnable1);
        Thread thread2=new Thread(runnable2);
        Thread thread3=new Thread(runnable3);
        thread1.setPriority(Thread.MAX_PRIORITY);
        thread2.setPriority(Thread.MIN_PRIORITY);
        thread3.setPriority(Thread.MIN_PRIORITY);
        thread1.start();
        thread2.start();
        thread3.start();

    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            int counter=0;
           while (counter<5) {
               try {
                   Thread.sleep(500);
                   System.out.println(Thread.currentThread().getName() + "=>"+counter);
                   counter++;
                   Thread.yield();
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }


        }
    }
}
