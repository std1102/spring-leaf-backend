package com.springleaf.common.datastructure;

import com.springleaf.object.entity.Subject;
import io.ebean.Model;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class WrapperQueue <T extends Model> implements AtomicQueue{
    private AtomicReference<Queue<T>> dataSource;

    public WrapperQueue(Queue<T> queue) {
        dataSource = new AtomicReference<>();
        dataSource.set(queue);
    }

    @Override
    public boolean add(Object o) {
        return dataSource.get().add((T) o);
    }

    @Override
    public Object poll() {
        return dataSource.get().poll();
    }

    @Override
    public Object peek() {
        return dataSource.get().peek();
    }

    @Override
    public int size() {
        return dataSource.get().size();
    }

    @Override
    public Object get(int index) {
        return 0;
    }
}
