package uz.pdp.test.concurrency_collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrencyHashSetExample {
    public static void main(String[] args) throws InterruptedException {
//        CopyOnWriteArrayList<Integer> integers=new CopyOnWriteArrayList<>();
//        Vector<Integer> integers=new Vector<>();
        List<Integer> integerArrayList=new ArrayList<>() ;
        List<Integer> integers= Collections.synchronizedList(integerArrayList);
        Runnable task1=() -> {
            for (int i = 0; i <1000 ; i++) {
                integers.add(i);
            }
        };
        Runnable task2=() -> {
            for (int i = 1000; i <2000 ; i++) {
                integers.add(i);
            }
        };
        new Thread(task1).start();
        new Thread(task2).start();
        Thread.sleep(1000);
        System.out.println("integers.size() = " + integers.size());

    }
}
