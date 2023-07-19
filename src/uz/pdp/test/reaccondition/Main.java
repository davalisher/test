package uz.pdp.test.reaccondition;

import java.util.LinkedList;

public class Main {
    static LinkedList<Integer> linkedList = new LinkedList<>();

    public static void main(String[] args) throws InterruptedException {
        Integer a = 1;
       /* linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        Runnable task1 = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    linkedList.add(i + 10);
                    System.out.print("adding:" + (i + 10));
                    System.out.println("=> " + linkedList);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Runnable task2 = () -> {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.print("removing:" + linkedList.get(i));
                    linkedList.remove(i);
                    System.out.println("=> " + linkedList);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(task1).start();
        new Thread(task2).start();
        Thread.sleep(2000);
        System.out.println(linkedList);*/
      /* MyRunnable runnable= new MyRunnable();
        new Thread(runnable).start();
        new Thread(runnable).start();*/
        new Thread(() -> {
            Singleton singleton;
            for (int i = 0; i < 100; i++) {
                try {
                    singleton = Singleton.getInstance();
                    System.out.println(Thread.currentThread().getName() + "=>" + System.identityHashCode(singleton));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Thread-1").start();
        new Thread(() -> {
            Singleton singleton;
            for (int i = 0; i < 100; i++) {
                try {
                    singleton = Singleton.getInstance();
                    System.out.println(Thread.currentThread().getName() + "=>" + System.identityHashCode(singleton));
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Thread-2").start();
    }


}

class MyRunnable implements Runnable {
    Integer count = 1;

    public static void main(String[] args) {

    }

//    public MyRunnable(Integer count) {
//        this.count = count;
//    }

    @Override
    public synchronized void run() {
        if (count > 0) {
            try {
                System.out.println(Thread.currentThread().getName() + "=>count = " + count);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            count--;
            System.out.println(Thread.currentThread().getName() + "=>count after incrementing: " + count);
        } else {
            System.out.println(Thread.currentThread().getName() + "=>Value of count less than 0: count=" + count);
        }
    }
}

class Singleton {
    public static int count = 0;

    private static Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {
            count++;
            instance = new Singleton();
        }
        return instance;
    }

}
