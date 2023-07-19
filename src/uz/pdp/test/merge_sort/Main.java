package uz.pdp.test.merge_sort;

import java.util.concurrent.RecursiveTask;

public class Main {
    public static void main(String[] args) {

    }

    static class MyRecursiveTask extends RecursiveTask<int[]> {
        int[] ints;
        boolean isSorted;

        public MyRecursiveTask(int[] ints, boolean isSorted) {
            this.ints = ints;
            this.isSorted = isSorted;
        }

        @Override
        protected int[] compute() {

            return new int[0];

        }
    }
}
