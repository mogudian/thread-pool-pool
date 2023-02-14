package com.mogudiandian.tpp.configuration;

import com.mogudiandian.tpp.ThreadPoolFactory;
import com.mogudiandian.tpp.ThreadPoolPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池池配置启动类
 * @author sunbo
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolPoolProperties.class)
@ConditionalOnClass({GenericKeyedObjectPool.class, GenericKeyedObjectPoolConfig.class})
public class ThreadPoolPoolConfiguration {

    @Autowired
    private ThreadPoolPoolProperties threadPoolPoolProperties;

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(name = "tpp")
    public ThreadPoolPool threadPoolPool() {
        ThreadPoolFactory threadPoolFactory = new ThreadPoolFactory(threadPoolPoolProperties);
        GenericKeyedObjectPool<String, ThreadPoolExecutor> keyedObjectPool = new GenericKeyedObjectPool<>(threadPoolFactory, threadPoolPoolProperties);
        // 这个是为了把pool
        threadPoolFactory.setPool(keyedObjectPool);
        return new ThreadPoolPool(keyedObjectPool);
    }

}