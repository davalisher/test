package uz.pdp.test.condition;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    List<Bottle> storageCola1 = new ArrayList<>();
    static List<Bottle> storageCola15 = new ArrayList<>();
    static List<Block> blocks = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        Factory colaFactory = new Factory(BottleType.BOTTLE_1_5, 100, "Coca-cola");
        PouringConveyor pouringConveyor = new PouringConveyor(colaFactory);
        PackingConveyor packingConveyor = new PackingConveyor(colaFactory);
        new Thread(pouringConveyor).start();
        Thread.sleep(1000);
        new Thread(packingConveyor).start();
    }


}

class Factory {
    private Queue<Bottle> storage = new LinkedList<>();
    private List<Block> blocks = new ArrayList<>();
    private BottleType bottleType;
    private long sleepTime = 25;
    private double volume;
    private String drink;
    private int maxSize;
    private int defaultSize = 100;
    private int blockSize = 12;

    private Lock lock = new ReentrantLock();
    private Condition pouringCondition = lock.newCondition();
    private Condition packingCondition = lock.newCondition();


    public Factory(BottleType bottleType, double volume, String drink) {
        this.bottleType = bottleType;
        this.volume = volume;
        this.drink = drink;
        switch (bottleType) {
            case BOTTLE_0_5: {
                maxSize = defaultSize;
            }
            case BOTTLE_1_0: {
                maxSize = defaultSize * 2;
                sleepTime *= 2;
                blockSize /= 1.5;
            }
            default: {
                maxSize = defaultSize * 3;
                sleepTime *= 3;
                blockSize /= 2;
            }
        }
        System.out.println("bottle volume: " + bottleType.getVolume());
    }

    public void pouring() {
        lock.lock();
        try {
//            while (volume >= bottleType.getVolume()) {
            while (storage.size() >= maxSize) {
                System.out.printf("storage size=%d, maxSize=%d\n", storage.size(), maxSize);
                System.out.println("Not enough place");
                pouringCondition.await();
            }
            System.out.println(storage.size() + "=>bottle, volume: " + volume);
//            Thread.sleep(sleepTime);
            storage.add(new Bottle(bottleType, drink));
            volume -= bottleType.getVolume();
            packingCondition.signalAll();
        } catch (InterruptedException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        } finally {
            System.out.println("Pouring unlocking");
            lock.unlock();
        }
    }

    public void packing() {

        lock.lock();
        Block block = new Block(bottleType);
        try {
            while (storage.size() < block.getSize()) {
                System.out.printf("storage size=%d, block size=%d\n", storage.size(), block.getSize());
                System.out.println("Butilkalar yetarli emas");
                packingCondition.await();
            }
            System.out.println(blocks.size() + "=>block, sorage size: " + storage.size() + "===============");
            for (int i = 0; i < block.getBottles().length; i++) {
                block.setBottle(i, storage.poll());
            }
            blocks.add(block);
            pouringCondition.signalAll();
        } catch (InterruptedException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        } finally {
            System.out.println("Packing unlocking");
            lock.unlock();
        }
    }

    public BottleType getBottleType() {
        return bottleType;
    }

    public double getVolume() {
        return volume;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public Queue<Bottle> getStorage() {
        return storage;
    }

    public long getSleepTime() {
        return sleepTime;
    }
}

class PouringConveyor implements Runnable {

    private Factory factory;

    public PouringConveyor(Factory factory) {
        this.factory = factory;
    }

    @Override
    public void run() {
        while (factory.getVolume() >= factory.getBottleType().getVolume()) {
            try {
                Thread.sleep(factory.getSleepTime());
                factory.pouring();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class PackingConveyor implements Runnable {
    private Factory factory;

    public PackingConveyor(Factory factory) {
        this.factory = factory;
    }

    @Override
    public void run() {
        int size=factory.getStorage().size();
        while (size>0) {
            try {
                Thread.sleep(10);
                factory.packing();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

enum BottleType {
    BOTTLE_0_5(0.5),
    BOTTLE_1_0(1),
    BOTTLE_1_5(1.5);
    private double volume;

    public double getVolume() {
        return volume;
    }

    BottleType(double volume) {
        this.volume = volume;
    }
}

class Bottle {
    BottleType bottleType;
    private String drink;


    public Bottle() {
    }

    public Bottle(BottleType bottleType, String drink) {
        this.bottleType = bottleType;
        this.drink = drink;
    }


    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    @Override
    public String toString() {
        return "Bottle{" +
                "bottleType=" + bottleType +
                ", drink='" + drink + '\'' +
                '}';
    }
}

class Block {
    private Bottle[] bottles;
    private BottleType bottleType;

    public Block(BottleType bottleType) {
        this.bottleType = bottleType;
        int length = switch (bottleType) {
            case BOTTLE_0_5 -> 12;
            case BOTTLE_1_0 -> 8;
            default -> 6;
        };
        bottles = new Bottle[length];
    }

    public Bottle[] getBottles() {
        return bottles;
    }

    public void setBottle(int index, Bottle bottle) {
        bottles[index] = bottle;
    }

    public int getSize() {
        return bottles.length;
    }

    @Override
    public String toString() {
        return "Block{" +
                "bottles=" + Arrays.toString(bottles) +
                ", bottleType=" + bottleType +
                '}';
    }
}


