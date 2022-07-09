package com.springleaf.common;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadSystem {
    private static ExecutorService executor;

    public static void init() {
        executor = Executors.newFixedThreadPool(DefaultValues.THREAD_POOL, new ThreadFactory() {
            @Override
            public Thread newThread(@NotNull Runnable r) {
                return new Thread();
            }
        });
    }

    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

}
