# springboot+Spring-security+Spring-Session+Redis+nginx 实现 Session 共享

`HttpSession`是通过Servlet容器创建和管理的，像Tomcat/Jetty都是保存在内存中的。
而如果我们把web服务器搭建成分布式的集群，然后利用Nginx或其他代理做负载均衡，**那么来自同一用户的Http请求将有可能被分发到两个不同的web站点中去**。那么问题就来了，如何保证不同的web站点能够共享同一份session数据呢？

最简单的想法就是把session数据保存到内存以外的一个统一的地方，例如Memcached/Redis等数据库中。那么问题又来了，**如何替换掉Servlet容器创建和管理HttpSession的实现呢**？

+ 设计一个Filter，利用HttpServletRequestWrapper，实现自己的 getSession()方法，**接管创建和管理Session数据的工作**。spring-session就是通过这样的思路实现的。
+ 利用Servlet容器提供的插件功能，自定义HttpSession的创建和管理策略，并通过配置的方式替换掉默认的策略。开源项目有memcached-session-manager，以及tomcat-redis-session-manager。暂时都只支持Tomcat6/Tomcat7。
+ 或者通过nginx之类的负载均衡做ip_hash，路由到特定的服务器上。 如果服务器挂了，同样也有问题。

## 示例说明

### 版本
+ SpringBoot 2.3.3
+ JDK 1.8
+ Mybatis 2.1.3
+ Mysql 5.7+
+ Redis 5.0.4

### 示例安装

+ 修改 `application.yaml` **Redis**、**端口**、**数据库** 等配置
+ 使用命令行 `mvn clean package` 打包应用，注意请勿使用 IDEA 自带的 maven 工具打包，会造成反射无法读取 jar 中 class 的错误。
+ 复制`target`目录下 jar 包、配置。目录结构如下

    ![](http://pic.fangxutuwen.com/16024919316533.jpg)
+ 为测试 nginx 代理，可以多复制几个目录，修改 `application.yaml` 中的端口号
+ 启动多个不同端口的应用
+ 启动 nginx
+ 访问 nginx 暴露的地址。
+ 打开多个浏览器，或在多台机器上访问，查看 session 是否共享

> 为避免迁移脚本冲突，如果不建多个数据库脚本的情况为，请先清空数据库里的内容

### Spring-Session:

![](http://pic.fangxutuwen.com/16024898081084.jpg)

* `传统的部署方式`，只有一台服务器，Session 可以保持。用户可以在同一个 Session 中保持住自己的状态内容
* `nginx 代理部署方式`，有多台服务器，就不会同一个 client 一直访问同一台 Server，如何保持 Session，维护用户的状态就是 Spring-Session的作用。


![](http://pic.fangxutuwen.com/16024910174865.jpg)

* Spring-session 负责保存、读取、转化Servlet 实现的 HttpSession.
* 采用 Redis 来存储 Session 数据，解决 Session 共享的问题。 Spring-Session 框架提供了redis、jvm的map、mongo、gemfire、hazelcast、jdbc等多种存储session的容器的方式。
* 其他：WebSocket和spring-session结合，同步生命周期管理。当用户使用WebSocket发送请求的时候，能够保持HttpSession处于活跃状态。

**最后**：Spring-session的核心项目并不依赖于Spring框架，所以，我们甚至能够将其应用于不使用Spring框架的项目中。

#### Springboot 中开启 Spring-session

```java
@SpringBootApplication
// 添加 spring-session redis 保存
@EnableRedisHttpSession 
public class SpringbootSecurityRedisNginxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSecurityRedisNginxApplication.class, args);
    }

}

```

##### Springboot 中自定义 Session

```java
// 继承 AbstractHttpSessionApplicationInitializer session 初始化配置
public class MyWebappSessionInit extends AbstractHttpSessionApplicationInitializer {

    public MyWebappSessionInit(){
        // 将 session 的自定义配置传递给Spring-session 初始化
        super(CustomerSessionConfig.class, Config.class);
    }

}
```


### 注意

请用命令行 `mvn clean package` 打包项目，不要使用 IDEA 自带 maven 工具打包。会造成反射获取 class 文件失败的错误。