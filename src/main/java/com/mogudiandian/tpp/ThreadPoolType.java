package com.mogudiandian.tpp;

/**
 * 线程池类型
 *
 * @author sunbo
 */
public enum ThreadPoolType {

    /**
     * 单线程
     */
    SINGLE,

    /**
     * 固定线程数 需要配合corePoolSize使用
     */
    FIXED,

    /**
     * 动态线程数 需要配合corePoolSize和maximumPoolSize使用
     */
    CACHED,

    /**
     * 自定义线程类型 需要设置其它参数
     */
    CUSTOMIZED;

}
