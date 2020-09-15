# springboot-soft-balance-nginx

## 示例说明

+ 项目执行 clean 、 package 操作，将打包的 jar 和 application.yaml 放在一起
+ 修改 `application.yaml` 端口地址.
+ 启动多个服务
+ 启动后在不同的客户端机器上访问 [http://localhost/hello/username](http://localhost/hello/username)  username 自定义 。测试负载均衡

> 为避免迁移脚本冲突，如果不建多个数据库脚本的情况为，请先清空数据库里的内容

## 负载均衡介绍

[wiki](https://zh.wikipedia.org/wiki/负载均衡)

[百度百科](https://baike.baidu.com/item/负载均衡/932451?fr=aladdin)

简单来说就是提升吞吐量、避免服务器过载。分为**硬**（如 F5），**软**(如 apache、nginx 的反向代理，还有其他模式)

## Nginx 负载均衡网络图

![](http://pic.fangxutuwen.com/16001457299038.jpg)


## Nginx 负载均衡策略、原理

nginx 负载均衡使用 `upstream` 配置. 包括内置策略，还有通过插件方式的策略。列举经常使用的，有兴趣的可以深入学习。

#### 轮循（默认）[Round Robin]

每个请求按时间顺序逐一分配到不同的后端服务器，如果服务器shutdown 了，将不在分发请求到该服务器。 

+ servername 自定义名称
+ server 配置支持 ip/域名 + 有端口/无端口(80) 。内容根据实际配置
+ 可配置 n 个 server

```
upstream servername {
    server 192.168.0.1:8080;
    server 192.168.0.2:8080; 
}
```
 

#### 指定权重 [Least Connections]

通过权重，指定轮循的几率。weight和访问比率成正比，多用于服务器性能有差异的情况。

+ servername 自定义名称
+ server 配置支持 ip/域名 + 有端口/无端口(80) 。内容根据实际配置
+ weight 与访问量成正比，数字越大轮循几率越大
+ 可配置 n 个 server

```
upstream servername { 
    server 192.168.0.1:8080 weight=8; 
    server 192.168.0.2:8080 weight=10; 
} 
```

#### IP 绑定[IP Hash]

每个请求按根据客户端ip的hash结果分配服务器。这样可以客户端在一定时间内可以固定访问一台服务器，可以保持 Session 。 
默认执行 20（忘记了）hash 算法查找服务器，如果没有则进行轮循(算是一种高级的轮循)。

+ servername 自定义名称
+ server 配置支持 ip/域名 + 有端口/无端口(80) 。内容根据实际配置
+ 可配置 n 个 server

```
upstream servername { 
    ip_hash; 
    server 192.168.0.1:8080; 
    server 192.168.0.2:8080; 
}
```


#### Server 参数

+ down 表示当前server 不参与负载
+ weight 默认为1 数字越大 负载权重越大
+ max_fails 允许请求失败的次数 当超过最大次数 返回proxy_next_upstream模块定义的错误
+ fail_timeout:max_fails次失败后，服务端暂停的时间
+ backup: 其他所有的非backup机器down或者忙的时候 请求这台机器，这台机器的压力最小

```
upstream servername { 
    server 192.168.0.1:8080 down; 
    server 192.168.0.2:8080 weight=10 max_fails=30 fail_timeout=12; 
    server 192.168.0.3:8080 weight=8; 
    server 192.168.0.4:8080 backup; 
}
```

#### nginx Session 共享

使用反向代理负载均衡，不可避免会面对 Session 共享。一般的方式是 cookie、memcache、redis 来管理共享数据. 后面的 Springboot-security 专题，会专门讲解。

#### nginx 配置

配置采用的 ip_hash 。可以替换成其他的策略测试。

```

worker_processes  1;

events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;
    upstream springbootnginx{
       ip_hash;
       # 其实 2 个可以都在放本地，修改端口即可
       # 本地放一个
       server 192.168.1.130:8080;
       # 其他机器放一个
       server 192.168.1.120:8080;
    }

    server {
        listen       80;
        server_name  192.168.1.130;

        location / {
            root   html;
            # 指向反向代理
            proxy_pass http://springbootnginx;
            proxy_connect_timeout 3s;
            proxy_read_timeout 5s;
            proxy_send_timeout 3s;
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

}

```