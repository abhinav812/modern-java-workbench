package com.abhinav.java.concurrency.threadpool;

import com.google.common.collect.Sets;

import java.util.Set;

public class TreeNode {
    private int value;

    private Set<TreeNode> children;

    public TreeNode(int value, TreeNode... children) {
        this.value = value;
        this.children = Sets.newHashSet(children);
    }

    public int getValue() {
        return value;
    }

    public Set<TreeNode> getChildren() {
        return children;
    }
}
