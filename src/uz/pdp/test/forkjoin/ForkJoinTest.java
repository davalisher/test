package uz.pdp.test.forkjoin;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForkJoinTest {
    static int[] nums;
    static int[] nums1;
    static int[] nums2;
    static int[] nums3;
    static int[] nums4;
    static int[][] pieces;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long startTime, finishTime, time;
        int arrayLength = 1_000;
        int coreThreads = Runtime.getRuntime().availableProcessors();


        nums = initializingArray(arrayLength);
        nums1 = Arrays.copyOfRange(nums, 0, 250);
        nums2 = Arrays.copyOfRange(nums, 250, 500);
        nums3 = Arrays.copyOfRange(nums, 500, 750);
        nums4 = Arrays.copyOfRange(nums, 750, nums.length);
        pieces = cutInTopieces(coreThreads);

        test(ForkJoinTest::arraySum, "Single Thread Array Sum");
        test(ForkJoinTest::arraySumWithFixedThreadPool, "arraySumWithFixedThreadPool");
        test(ForkJoinTest::arraySumWithFixedThreadPoolWithoutPieces, "arraySumWithFixedThreadPoolWithoutPieces");
        test(ForkJoinTest::arraySumWithCachedhreadPool, "arraySumWithCachedhreadPool");
        test(ForkJoinTest::arraySumWithForkJoinPool, "arraySumWithForkJoinPool");
        test(ForkJoinTest::arraySumWithForkJoinPoolWith4Task, "arraySumWithForkJoinPoolWith4Task");

    }

    static void test(Supplier<Integer> methodForArraySum, String message) {
        long startTime = System.currentTimeMillis();
        System.out.println(" sum => " + methodForArraySum.get());
        long finishTime = System.currentTimeMillis();
        long time = finishTime - startTime;
        System.out.println(message + " time => " + time);

        System.out.println("=============================================");
    }

    private static int[][] cutInTopieces(int threadNumbers) {
        int length = (int) Math.ceil(1.0 * nums.length / threadNumbers);
        int[][] result = new int[length][12];
        int index;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 12; j++) {
                index = i * 12 + j;
                if (index < nums.length) {
                    result[i][j] = nums[index];
                }
            }
        }
        return result;
    }


    private static int[] initializingArray(int length) {
        int[] nums = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            nums[i] = random.nextInt(10);
        }
        return nums;
    }

    private static int arraySum() {
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            try {
                Thread.sleep(1);
                sum += nums[i];
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return sum;
    }

    private static int arraySumWithFixedThreadPoolWithoutPieces() {

        int coreThreadNumbers = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(coreThreadNumbers);


        Callable<Integer> callable = () -> {
            int sum = 0;
            for (int j = 0; j < nums.length; j++) {
                Thread.sleep(1);
                sum += nums[j];
            }
            return sum;
        };
        Future<Integer> future = executorService.submit(callable);
        executorService.shutdown();
        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static int arraySumWithFixedThreadPool() {
        int coreThreadNumbers = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(coreThreadNumbers);
        Collection<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < pieces.length; i++) {
            final int index = i;
            Callable<Integer> callable = () -> {
                int sum = 0;
                for (int j = 0; j < pieces[index].length; j++) {
                    Thread.sleep(1);
                    sum += pieces[index][j];
                }
                return sum;
            };
            tasks.add(callable);
        }
        List<Future<Integer>> futures = null;
        int sum = 0;
        try {
            futures = executorService.invokeAll(tasks);
            for (int i = 0; i < futures.size(); i++) {
                sum += futures.get(i).get();
            }
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return sum;
    }

    private static int arraySumWithCachedhreadPool() {
        int coreThreadNumbers = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newCachedThreadPool();
        Collection<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < pieces.length; i++) {
            final int index = i;
            Callable<Integer> callable = () -> {
                int sum = 0;
                for (int j = 0; j < pieces[index].length; j++) {
                    Thread.sleep(1);
                    sum += pieces[index][j];
                }
                return sum;
            };
            tasks.add(callable);
        }
        List<Future<Integer>> futures = null;
        int sum = 0;
        try {
            futures = executorService.invokeAll(tasks);
            for (int i = 0; i < futures.size(); i++) {
                sum += futures.get(i).get();
            }
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return sum;
    }

    private static int arraySumWithForkJoinPool() {
        int coreThreadNumbers = Runtime.getRuntime().availableProcessors();
        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(nums);
        myRecursiveTask.fork();
        return myRecursiveTask.join();
    }

    private static int arraySumWithForkJoinPoolWith4Task() {
        int sum = 0;
        int coreThreadNumbers = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyCallableTask myCallableTask1 = new MyCallableTask(nums1);
        MyCallableTask myCallableTask2 = new MyCallableTask(nums2);
        MyCallableTask myCallableTask3 = new MyCallableTask(nums3);
        MyCallableTask myCallableTask4 = new MyCallableTask(nums4);
        List<Future<Integer>> futures = forkJoinPool.invokeAll(List.of(myCallableTask1, myCallableTask2, myCallableTask3, myCallableTask4));
        for (int i = 0; i < futures.size(); i++) {
            try {
                sum += futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        forkJoinPool.shutdown();
        return sum;
    }


}

class MyRecursiveTask extends RecursiveTask<Integer> {
    int[] array;

    public MyRecursiveTask(int[] array) {
        this.array = array;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        if (array.length <= 2) {
            for (int i = 0; i < array.length; i++) {
                try {
                    Thread.sleep(1);
                    sum += array[i];
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return sum;
        }

        MyRecursiveTask recursiveTask1 = new MyRecursiveTask(Arrays.copyOfRange(array, 0, array.length / 2));
        MyRecursiveTask recursiveTask2 = new MyRecursiveTask(Arrays.copyOfRange(array, array.length / 2, array.length));
        invokeAll(recursiveTask1, recursiveTask2);
//        recursiveTask1.fork();
//        recursiveTask2.fork();
        return recursiveTask1.join() + recursiveTask2.join();
        // Create a CompletableFuture
        


    }
}

class MyCallableTask implements Callable<Integer> {
    int[] array;

    public MyCallableTask(int[] array) {
        this.array = array;
    }

    @Override
    public Integer call() {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            try {
                Thread.sleep(1);
                sum += array[i];
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return sum;
    }


}
