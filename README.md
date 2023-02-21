# thread-pool-pool

你听说过线程吗？听说过。你听说过线程池吗？听说过。你听说过线程池池吗？我？？？！！！没错，这个项目就是线程池池，适合根据业务做线程池隔离

## 使用说明

- 1、集成依赖（需先将该项目源码下载并打包）

```xml

<dependency>
    <groupId>com.mogudiandian</groupId>
    <artifactId>thread-pool-pool</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

- 2、配置

```properties
tpp.oneBiz.type=CUSTOMIZED
tpp.oneBiz.corePoolSize=4
tpp.oneBiz.maximumPoolSize=16
tpp.oneBiz.rejectedExecutionHandler=CallerRunsPolicy

tpp.twoBiz.type=FIXED
tpp.twoBiz.corePoolSize=8

tpp.threeBiz.type=CACHED
tpp.threeBiz.corePoolSize=16
tpp.threeBiz.maximumPoolSize=32
```


- 3、使用工具类

```java
@Service
public class FooService {

    private static final String THREAD_POOL_KEY = "oneBiz";
    
    @Autowired
    private ThreadPoolPool threadPoolPool;

    public void doSomethingWithThreadPool() {
        ThreadPoolExecutor executor = threadPoolPool.take(THREAD_POOL_KEY);
        executor.submit(() -> {
            ...
        });
        // 注意这里不需要手动shutdown，池池会在Spring应用结束时自己shutdown
    }
}

@Service
public class BarService {

    private static final String THREAD_POOL_KEY = "twoBiz";

    @Autowired
    private ThreadPoolPool threadPoolPool;

    public void doSomethingWithThreadPool() {
        ThreadPoolExecutor executor = threadPoolPool.take(THREAD_POOL_KEY);
        executor.submit(() -> {
            ...
        });
        // 注意这里不需要手动shutdown，池池会在Spring应用结束时自己shutdown
    }
}
```

## 依赖三方库

| 依赖            | 版本号           | 说明  |
|---------------|---------------|-----|
| spring-boot   | 2.3.4.RELEASE |     |
| commons-lang3 | 3.11          |     |
| commons-pool2 | 2.9.0         |     |
| guava         | 29.0-jre      |     |
| lombok        | 1.18.16       |     |

## 使用前准备

- [Maven](https://maven.apache.org/) (构建/发布当前项目)
- Java 8 ([Download](https://adoptopenjdk.net/releases.html?variant=openjdk8))

## 构建/安装项目

使用以下命令:

`mvn clean install`

## 发布项目

修改 `pom.xml` 的 `distributionManagement` 节点，替换为自己在 `settings.xml` 中 配置的 `server` 节点，
然后执行 `mvn clean deploy`

举例：

`settings.xml`

```xml

<servers>
    <server>
        <id>snapshots</id>
        <username>yyy</username>
        <password>yyy</password>
    </server>
    <server>
        <id>releases</id>
        <username>xxx</username>
        <password>xxx</password>
    </server>
</servers>
```

`pom.xml`

```xml

<distributionManagement>
    <snapshotRepository>
        <id>snapshots</id>
        <url>http://xxx/snapshots</url>
    </snapshotRepository>
    <repository>
        <id>releases</id>
        <url>http://xxx/releases</url>
    </repository>
</distributionManagement>
```
