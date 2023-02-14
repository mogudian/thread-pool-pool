package com.mogudiandian.tpp.configuration;

import com.mogudiandian.tpp.BlockingQueueType;
import com.mogudiandian.tpp.ThreadPoolType;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author sunbo
 */
@Getter
@Setter
public class ThreadPoolProperties {

    /**
     * 线程池类型
     */
    private ThreadPoolType type;

    /**
     * 核心线程数
     */
    private int corePoolSize;

    /**
     * 最大线程数
     */
    private int maximumPoolSize;

    /**
     * 活跃时间
     */
    private long keepAliveTime;

    /**
     * 活跃时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 阻塞队列类型
     */
    private BlockingQueueType blockingQueueType;

    /**
     * 阻塞队列长度
     */
    private int queueSize;

    /**
     * 要写ThreadPoolExecutor中内部类的名称，例如CallerRunsPolicy
     */
    private String rejectedExecutionHandler;

}
