package com.mogudiandian.tpp;

import com.mogudiandian.tpp.configuration.ThreadPoolPoolProperties;
import com.mogudiandian.tpp.configuration.ThreadPoolProperties;
import lombok.Setter;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.*;

/**
 * 线程池工厂
 *
 * @author sunbo
 */
public class ThreadPoolFactory extends BaseKeyedPooledObjectFactory<String, ThreadPoolExecutor> {

    @Setter
    private KeyedObjectPool<String, ThreadPoolExecutor> pool;

    private final ThreadPoolPoolProperties threadPoolPoolProperties;

    public ThreadPoolFactory(ThreadPoolPoolProperties threadPoolPoolProperties) {
        this.threadPoolPoolProperties = threadPoolPoolProperties;
    }

    public ThreadPoolExecutor create0(String key) throws Exception {
        ThreadPoolProperties threadPoolProperties = threadPoolPoolProperties.getPools().get(key);
        KeyedThreadFactory threadFactory = new KeyedThreadFactory(key);

        // 根据配置的线程池类型创建线程池
        switch (threadPoolProperties.getType()) {
            case SINGLE:
                return new ThreadPoolExecutor(1, 1,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(), threadFactory);
            case FIXED:
                return new ThreadPoolExecutor(threadPoolProperties.getCorePoolSize(), threadPoolProperties.getCorePoolSize(),
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(), threadFactory);
            case CACHED:
                return new ThreadPoolExecutor(threadPoolProperties.getCorePoolSize(), threadPoolProperties.getMaximumPoolSize(),
                    60L, TimeUnit.SECONDS,
                    new SynchronousQueue<>(), threadFactory);
            default:
                // 自定义线程池

                // 核心线程数
                int corePoolSize = threadPoolProperties.getCorePoolSize();
                if (corePoolSize <= 0) {
                    corePoolSize = 1;
                }

                // 最大线程数
                int maximumPoolSize = threadPoolProperties.getMaximumPoolSize();
                if (maximumPoolSize <= 0) {
                    maximumPoolSize = corePoolSize * 2;
                }

                // 非核心线程的活跃时间
                long keepAliveTime = threadPoolProperties.getKeepAliveTime();
                if (keepAliveTime < 0) {
                    keepAliveTime = 0;
                }
                TimeUnit timeUnit = threadPoolProperties.getTimeUnit();
                if (timeUnit == null) {
                    timeUnit = TimeUnit.MILLISECONDS;
                }

                // 阻塞队列
                BlockingQueue<Runnable> blockingQueue;
                if (threadPoolProperties.getBlockingQueueType() == BlockingQueueType.ARRAY) {
                    blockingQueue = new ArrayBlockingQueue<>(threadPoolProperties.getQueueSize());
                } else {
                    blockingQueue = new LinkedBlockingQueue<>();
                }

                // 拒绝策略
                RejectedExecutionHandler rejectedExecutionHandler = null;
                // 这里配的是ThreadPoolExecutor中内部类的名称
                if (threadPoolProperties.getRejectedExecutionHandler() != null) {
                    String className = ThreadPoolExecutor.CallerRunsPolicy.class.getName().replace("CallerRunsPolicy", threadPoolProperties.getRejectedExecutionHandler());
                    rejectedExecutionHandler = (RejectedExecutionHandler) Class.forName(className).newInstance();
                }
                if (rejectedExecutionHandler == null) {
                    rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
                }

                return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                        keepAliveTime, timeUnit,
                        blockingQueue, threadFactory, rejectedExecutionHandler);
        }
    }

    @Override
    public ThreadPoolExecutor create(String key) throws Exception {
        return new ThreadPoolExecutorWrapper(key, pool, create0(key));
    }

    @Override
    public PooledObject<ThreadPoolExecutor> wrap(ThreadPoolExecutor threadPoolExecutor) {
        return new DefaultPooledObject<>(threadPoolExecutor);
    }

    @Override
    public void destroyObject(String key, PooledObject<ThreadPoolExecutor> pooledObject) throws Exception {
        super.destroyObject(key, pooledObject);
        ThreadPoolExecutor threadPoolExecutor = pooledObject.getObject();
        if (threadPoolExecutor instanceof ThreadPoolExecutorWrapper) {
            ((ThreadPoolExecutorWrapper) threadPoolExecutor).getExecutor().shutdown();
        } else {
            threadPoolExecutor.shutdown();
        }
    }
}
