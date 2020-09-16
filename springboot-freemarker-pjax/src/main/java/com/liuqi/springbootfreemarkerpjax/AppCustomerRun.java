package com.liuqi.springbootfreemarkerpjax;

import com.liuqi.springbootfreemarkerpjax.core.PjaxInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:47 上午 2020/9/16
 */
@Component
@Order(4)
public class AppCustomerRun implements WebMvcConfigurer {

    @Autowired
    private PjaxInterceptor pjaxInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String noInterceptorStr = "/ ,/error,  /login, /logout,/login/error , /druid/**, /assets/**/vendor/**, /assets/**/css/**, /assets/**/js/** , /images/favicon.ico ,/images/logo.jpg, /test/**";
        String[] noInterceptor = noInterceptorStr.split(",");
        registry.addInterceptor(pjaxInterceptor).excludePathPatterns(noInterceptor);
    }
}
