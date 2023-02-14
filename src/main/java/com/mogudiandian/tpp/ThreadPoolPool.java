package com.mogudiandian.tpp;

import lombok.SneakyThrows;
import org.apache.commons.pool2.KeyedObjectPool;

import java.io.Closeable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池池
 *
 * @author sunbo
 */
public class ThreadPoolPool implements Closeable {

    private final KeyedObjectPool<String, ThreadPoolExecutor> pool;

    public ThreadPoolPool(KeyedObjectPool<String, ThreadPoolExecutor> pool) {
        this.pool = pool;
    }

    @SneakyThrows
    public ThreadPoolExecutor take(String key) {
        return pool.borrowObject(key);
    }

    @SneakyThrows
    @Override
    public void close() {
        pool.close();
    }
}
