<#-- 管理台 页面 base -->
<!DOCTYPE html>
<#setting number_format="#">
<#macro htmlHead>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

        <link rel="stylesheet" href="/assets/vendor/bootstrap/css/bootstrap.css">
        <link rel="stylesheet" href="/assets/vendor/nprogress/nprogress.css">
        <style>
            .navM0 {
                margin: 0 0 0 0 !important
            }

            .leftM {
                padding: 0 10px 0 0;
                -webkit-border-radius: 0 !important;
                -moz-border-radius: 0 !important;
                border-radius: 0 !important;
            }
        </style>
        <#nested >

        <title>Springboot+Freemarker+Pjax 单页 Demo </title>
    </head>
</#macro>

<#macro htmlBody>

    <body>

        <div class="container-fluid">


            <!-- header -->
            <#include "/common/top.ftl">

            <div class="row">
                <!-- left -->
                <#include "/common/left.ftl">

                <!-- content -->
                <div class="col-md-10">
                    <div class="row" id="content_container">
                        <#nested >
                    </div>
                </div>
                <!-- content end -->
            </div>
        </div>

    </body>

</#macro>

<#macro htmlBottom>
    <#-- jQuery -->
    <script type="text/javascript" src="/assets/vendor/jquery-1.11.1.min.js"></script>
    <!-- json2 -->
    <script type="text/javascript" src="/assets/vendor/JSON-js-master/json2.js"></script>
    <!-- pjax -->
    <script type="text/javascript" src="/assets/vendor/jquery-pjax/jquery.pjax.js"></script>
    <!-- nprogress -->
    <script type="text/javascript" src="/assets/vendor/nprogress/nprogress.js"></script>
    <!-- bootstrap -->
    <script type="text/javascript" src="/assets/vendor/bootstrap/js/bootstrap.js"></script>
    <!-- js -->
    <script type="text/javascript" src="/assets/js/main.js"></script>

    <!-- customer -->
    <script>

        // 动作条
        // 设置请求开始的动作
        $(window).ajaxStart(function () {
            NProgress.start();
        });

        // 设置请求结束的动作
        $(window).ajaxStop(function () {
            NProgress.done();
        });

        /**
         * pjax 说明 https://www.cnblogs.com/telwanggs/p/7136716.html
         *
         * @param selector  触发点击事件的按钮
         * @param container 展示刷新内容的容器，也就是会被替换的部分
         * @param options   参数
         *  - 参数名称          默认值             说明
         *  - timeout           650             ajax超时时间（毫秒），超时后强制刷新整个页面
         *  - push	            true	        使用 pushState 在浏览器中添加历史记录
         *  - replace	        false	        替换URL地址但不添加浏览器历史记录
         *  - maxCacheLength	20	            容器元素缓存内容的最大值（次）
         *  - version		string或function     返回当前pjax版本
         *  - scrollTo	        0	            浏览器滚动条的垂直滚动位置。设为false时禁止滚动
         *  - type	            GET	            参考 $.ajax
         *  - dataType	        html	        参考 $.ajax
         *  - container		                    被替换内容元素的CSS选择器
         *  - url	            link.href	    string或function，返回ajax请求响应的URL
         *  - target	        link	        pjax 事件 中relatedTarget属性的最终值
         *  - fragment		    css选择器       提取ajax响应内容中指定的内容片段
         *
         * a[data-pjax] 代表使用 data-pjax属性值查找容器
         */
        $(document).pjax('[data-pjax] a, a[data-pjax]', '#content_container');

        // 初始化菜单
        admin.menuInit();

    </script>

    <#nested >
</html>
</#macro>