package uz.pdp.test.thread;

public class JoinExample {
    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().join();
        MyRunnable runnable1 = new MyRunnable(3000);
        MyRunnable runnable2 = new MyRunnable(0);
        MyRunnable runnable3 = new MyRunnable(0);
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        Thread thread3 = new Thread(runnable3);
        thread1.start();
        thread1.join(1000);
        thread2.start();
        thread3.start();
        System.out.println("tugadi");


    }

    static class MyRunnable implements Runnable {
        int time;

        public MyRunnable(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "=>Start");
                Thread.sleep(time);
                System.out.println(Thread.currentThread().getName() + "=>End");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

}
