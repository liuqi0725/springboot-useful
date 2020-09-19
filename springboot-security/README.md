# springboot-security

SpringSecurity 是 Spring 提供安全管理框架。核心内容包含**认证**、**授权**、**攻击防护**。实际上SpringSecurity 已经发展了多年了，但是在 SSM/SSH 中整合 SpringSecurity 相较于 Shiro 来说显得要麻烦很多，所以在安全管理框架这块一直都是 Shiro 的天下。

自从有了 SpringBoot ，SpringSecurity 的完美兼容让其价值完整的体现了出来。在 SpringBoot 中基本零配置就可以使用 SpringSecurity了。

## 示例说明

### 版本
+ SpringBoot 2.3.3
+ JDK 1.8
+ Mybatis 2.1.3
+ Mysql 5.7+

### 示例安装

+ 修改 `application.yaml` **端口**、**数据库** 等配置
+ 启动后在不同的客户端机器上访问 [http://localhost:8080](http://localhost:8080) （根据自己配置的端口访问）
+ 测试用2 种登陆方式: 用户密码、手机验证码
+ 查看不同用户菜单是否不一样（测试授权）
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

## CSRF 跨站请求伪造防护

Demo 中没有开启 csrf。 如果需要可以自行开启。

开启后，通过如下代码获取

```html
<input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}">
或者放在head 中
<head> 
	<meta  name = “_csrf” content = “${_csrf.token}” /> 
	<meta  name = “_csrf_header”  content = “${_csrf.headerName}” /> 
</head> 
```

## iframe 嵌入

SpringSecurity 默认是关闭了 frame 的嵌入的，可以开启。

参考 Demo 代码 `SecurityWebConfig` 中 `setFrameAllow(http)` 方法

```java
private void setFrameAllow(HttpSecurity http) throws Exception {

        /*
         * iframe 允许显示的方式 <br>
         * SAMEORIGIN 仅允许 frame 页面当前域名下的显示 <br>
         *
         * FROMURI 允许 frame 页面在指定域名下显示 <br>
         *     例如：
         *     <ul>
         *          <li>http://www.baidu.com 允许该域名可以嵌套我的 frame</li>
         *          <li>http://www.taobao.com 允许该域名可以嵌套我的 frame</li>
         *     </ul>
         */

        // 正式环境请配置在配置文件中。方便管理

        String xframe = "SAMEORIGIN";
        // 如果是 FROMURI 允许嵌套的外部域名白名单
        String[] frameAllowWhiteDomain = new String[]{"https://example.cn","https://example.com"};

        if(xframe.equals("SAMEORIGIN")){
            // 仅允许本域名
            http.headers().frameOptions().sameOrigin();
        }else if(xframe.equals("FROMURI")){
            //disable 默认策略。 这一句不能省。
            http.headers().frameOptions().disable();
            //新增新的策略。
            http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(
                    new WhiteListedAllowFromStrategy(Arrays.asList(frameAllowWhiteDomain))
            ));
        }else{
            throw new Exception("未知的 XFrameOptions 。仅支持 SAMEORIGIN , FROMURI");
        }
    }
```

## 提升用户、编码体验

1. 异常处理

	Demo 中并没有做全局的统一异常处理，在正式项目中，可以通过全局的异常处理来提升错误后的用户体验。通过以下 2 点可以实现，view 访问返回错误 view，json 访问返回错误 json
	
	```java
    	// 处理 spring `/error` 
        @Controller
        @RequestMapping({"${server.error.path:${error.path:/error}}"})
        public class SpringErrorProcessController extends AbstractErrorController {
            ...
        }
        
        // 处理 Controller 层错误
        @ControllerAdvice
        public class ControllerExceptionHandler{
        
        }
	
	```

2. 自定义配置
    SpringSecurity 中还是有很多参数的，比如白名单，开启 csrf、iframe 都是可以通过配置实现，这样换一个项目不用把代码又改一堆。可以通过 `@ConfigurationProperties` 实现 spring 配置，在 spring 加载时读取。
    
3. 灵活的设计
    SpringSecurity 只提供了工具，具体的实现还是要在项目（可以理解为第三方）中去实现，比如 Demo 中的`CustomerSecurityAuthenticationProcessService`,`CustomerPasswordService` 等。这些都是需要在启动时注入到SpringSecurity 中的，如何将这些类在启动时获取到实体，或者根据获取内容让 SpringSecurity 实现其他的逻辑。 
    
> 以上几点只是我的肤浅建议，相信大多数人都是这么做的，只是不提不爽啊，公司有些人老是硬编码，换个项目改一堆，有时候还找不到错在哪里。这种重复劳动实在是太费力了。

## 更多

更多说明请参考代码中的注释。要点太多，Readme 写的话，又要写一次代码。