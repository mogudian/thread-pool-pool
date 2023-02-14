package com.mogudiandian.tpp;

/**
 * 阻塞队列类型
 *
 * @author sunbo
 */
public enum BlockingQueueType {

    /**
     * 数组 需要配合queueSize使用
     */
    ARRAY,

    /**
     * 链表
     */
    LINKED;

}
