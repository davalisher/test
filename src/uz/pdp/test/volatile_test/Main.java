package uz.pdp.test.volatile_test;


public class Main {

    private  static volatile  int increment = 0;

    public static void main(String[] args)    {
        new ThreadA().start();
        new ThreadB().start();
    }
    static class ThreadB extends Thread {
        @Override
        public void run(){
            while (increment < 10) {
                System.out.println(   "O’zgaruvchi qiymatini 1 ga oshiramiz va u  "+ ++increment + " ga teng");
                try {
                    Thread.sleep(600);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static class ThreadA extends Thread {
        @Override
        public void run(){
            int x = increment ;
            while (true ) {
                if (x != increment) {
                    System.out.println("O’zgaruvchining qiymati o’zgardi:  " +  increment);
                    x = increment;
                }
            }
        }
    }

}