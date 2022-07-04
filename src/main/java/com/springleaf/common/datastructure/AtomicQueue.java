package com.springleaf.common.datastructure;

import io.ebean.Model;

import java.util.Queue;

public interface AtomicQueue<T> {
    boolean add(T o);

    T poll();

    T peek();

    int size();

    T get(int index);
}
