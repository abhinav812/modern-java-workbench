package com.abhinav.java.concurrency.threadpool;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class CountingTask extends RecursiveTask<Integer> {
    private TreeNode node;

    public CountingTask(TreeNode treeNode) {
        this.node = treeNode;
    }

    @Override
    protected Integer compute() {
        System.out.println("Compute called with node "+ node.getValue());
        int sum = node.getValue() + node.getChildren().stream()
                .map(childNode -> new CountingTask(childNode).fork())
                .mapToInt(ForkJoinTask::join)
                .sum();
        System.out.println("intermediate sum: "+ sum);
        return sum;
    }
}
