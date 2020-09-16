# springboot-freemarker-pjax

## 示例说明

+ 修改 `application.yaml` 配置，不改保持默认也可以.
+ 启动后访问 [http://localhost:8080](http://localhost:8080)

## 为什么用 freemarker

虽然 [thymeleaf](https://www.thymeleaf.org) 是 springboot 官方推荐的模板，优点一大堆。
如果你是（研发、UI）一起使用，建议使用[thymeleaf](https://www.thymeleaf.org),毕竟可以直接运行显示静态数据，UI、研发互补干扰。这一块就吊打了 freemaker。

其他情况我会选择 freemarker。不为其他的，写起来方便，随便百度一篇文章，语法这一块就基本明白了。而且 freemarker 的语法跟我们平时的编码语法很接近，
你就不用在写的过程去考虑语法该怎么写。

比如常用的 if、for：

```html
<#assign a = 1 />
<#assign b = 2 />

<#if a == b >
    ...do something
<#elseif a == 1 >
    ...do something
<#else>
    ...do something
</#if>

<#list userlist as user>
    ...do something
</#list>
```

总之还没入坑[thymeleaf](https://www.thymeleaf.org)的同学，有这个空闲时间学习[thymeleaf](https://www.thymeleaf.org)还不如学个前端框架来的实际。

> 题外话:用[thymeleaf](https://www.thymeleaf.org)实现局部刷新，比 freemaker 简单

## 工作原理

+ 定义 2 套模板。 

    + 一套用于初始化、页面刷新
    + 一套 pjax 模板，普通请求

+ 添加 Pjax 拦截器
    
    + 拦截器中判断是否 pjax，并根据结果返回指定模板
    + 由于 pjax 请求不能继承父页面，只能刷新局部，所以如果需要对父页面的一些动态内容进行指定，需要在拦截器中完成

+ 页面渲染原理

    + 普通请求：利用 Freemarker 的 macro 在子页面进行替换操作
    + pjax 请求：利用 Freemarker 的 macro 在子页面进行替换操作，并动态添加到指定的 div 中，实现局部刷新
    
    > 我测试下，不能使用 freemarker 插件的 extend override等，如果能使用，请高手指教。



## 扩展

#### 菜单选中

为了保证页面刷新后，菜单保持不变。建议菜单放在 **cookie** 中。也可以利用缓存保持。

#### 重定向

头部添加
request.headers['X-PJAX-URL'] = "http://35liuqi.com/redirect"


