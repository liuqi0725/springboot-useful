# springboot-security

## 示例说明

+ 修改 `application.yaml` 端口地址，如不修改保持默认即可
+ 启动后在不同的客户端机器上访问 [http://localhost:8080](http://localhost:8080)
+ 测试用2 种登陆方式:用户密码、手机验证码
+ 查看不同用户菜单是否不一样
+ 访问不存在的路由地址，查看返回值

> 为避免迁移脚本冲突，如果不建多个数据库脚本的情况为，请先清空数据库里的内容

## Spring-security 介绍

[Spring Security 官方说明](https://spring.io/projects/spring-security)

[Spring Security 官方文档](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)

[Spring Security 参考手册-中文](https://www.springcloud.cc/spring-security-zhcn.html)

## 为什么不用 shiro

之前SpringMVC 一直用 shiro。后面慢慢过渡到 Springboot 后，改用了 security。然后就没有然后了。Spring 真的好用，教程也全。
再说中型项目后续会涉及集群或微服务，security、cloud 打包用一套不香么。

## Spring-Security 做用户认证、URL 鉴权

以下是我对这两点肤浅的认识。不包含 SSO、OAuth 等。后续会在其他文章中介绍。

分为 2 大块 **access(访问)**、**authenticaton(认证)**

+ access 会放过白名单，对非白名单的请求进行认证，认证的依据来源（权限、角色...），没有则返回 403
+ authenticaton 用户访问一个需要授权的 URL 时，会进行用户认证，认证通过后再通过 access 去判断是否可以访问

![](http://pic.fangxutuwen.com/16003563830451.jpg)


## 更多

更多说明请参考代码中的注释。要点太多，Readme 写的话，又要写一次代码。