package com.mogudiandian.tpp;

import lombok.Getter;
import org.apache.commons.pool2.KeyedObjectPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池包装
 *
 * @author sunbo
 */
public class ThreadPoolExecutorWrapper extends ThreadPoolExecutor {

    private final String key;

    private final KeyedObjectPool<String, ThreadPoolExecutor> pool;

    @Getter
    private final ThreadPoolExecutor executor;

    public ThreadPoolExecutorWrapper(String key, KeyedObjectPool<String, ThreadPoolExecutor> pool, ThreadPoolExecutor executor) {
        super(executor.getCorePoolSize(), executor.getMaximumPoolSize(), executor.getKeepAliveTime(TimeUnit.NANOSECONDS), TimeUnit.NANOSECONDS, executor.getQueue(), executor.getThreadFactory(), executor.getRejectedExecutionHandler());
        this.key = key;
        this.pool = pool;
        this.executor = executor;
    }

    @Override
    public void shutdown() {
        try {
            pool.returnObject(this.key, this);
        } catch (Exception e) {
            try {
                pool.invalidateObject(this.key, this);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        BlockingQueue<Runnable> queue = executor.getQueue();
        ArrayList<Runnable> taskList = new ArrayList<>();
        queue.drainTo(taskList);
        if (!queue.isEmpty()) {
            for (Runnable r : queue.toArray(new Runnable[0])) {
                if (queue.remove(r)) {
                    taskList.add(r);
                }
            }
        }
        shutdown();
        return taskList;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }
}
