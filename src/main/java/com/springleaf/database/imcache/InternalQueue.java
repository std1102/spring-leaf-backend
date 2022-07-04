package com.springleaf.database.imcache;

import com.springleaf.common.DefaultValues;
import com.springleaf.common.datastructure.AtomicQueue;
import com.springleaf.common.datastructure.WrapperQueue;
import com.springleaf.object.entity.Subject;
import io.ebean.Model;

import java.util.LinkedList;
import java.util.Queue;

public class InternalQueue {

    private AtomicQueue<Subject> queue;

    private static InternalQueue singleton = new InternalQueue();

    public InternalQueue() {
        queue = new WrapperQueue<>(new LinkedList<>());
    }

    public static InternalQueue getInstance(){
        return singleton;
    }

    boolean add(Subject o){
        return singleton.add(o);
    }

    // Using cache instead
//    public Subject poll(){
//        Subject subject = singleton.poll();
//        if (subject == null) {
//            return null;
//        }
//        long insertTime = subject.getCreatetime();
//        if ((System.currentTimeMillis() - insertTime) > DefaultValues.CACHE_TIME_ALIVE) {
//            return poll();
//        }
//        return subject;
//    }
//
//    @Deprecated
//    public Subject peek(){
//        Subject subject = singleton.peek();
//        if (subject == null) {
//            return null;
//        }
//        long insertTime = subject.getCreatetime();
//        if ((System.currentTimeMillis() - insertTime) > DefaultValues.CACHE_TIME_ALIVE) {
//            return peek();
//        }
//        return peek();
//    }

    // Shouldn't do like this
//    public Subject get(Long id) {
//        synchronized (queue) {
//            if (queue.size() == 0) {
//                return null;
//            }
//            T element = null;
//            for (int i = 0; i < size; i++) {
//                if (i == index) {
//                    element = queue.remove();
//                } else {
//                    queue.add(queue.remove());
//                }
//            }
//
//            return element;
//        }
//    }

}
