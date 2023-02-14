package com.mogudiandian.tpp.configuration;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池池配置
 *
 * @author sunbo
 */
@ConfigurationProperties(prefix = "tpp")
@Getter
@Setter
public class ThreadPoolPoolProperties extends GenericKeyedObjectPoolConfig<ThreadPoolExecutor> {

    /**
     * 线程池配置
     */
    private Map<String, ThreadPoolProperties> pools;

}
