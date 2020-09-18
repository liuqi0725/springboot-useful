package com.liuqi.springbootsecurity;

import com.liuqi.springbootsecurity.core.anno.FreeMarkerStatic;
import com.liuqi.springbootsecurity.core.freemarker.FreeMarkerSpringConfig;
import com.liuqi.springbootsecurity.utils.ClassUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.liuqi.springbootsecurity.core.PjaxInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Log4j2
public class AppCustomerRun implements WebMvcConfigurer, ApplicationRunner {

    @Autowired
    private PjaxInterceptor pjaxInterceptor;

    @Autowired
    private FreeMarkerSpringConfig freeMarkerSpringConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String noInterceptorStr = "/ ,/error,  /login, /logout,/login/error , /druid/**, /assets/**/vendor/**, /assets/**/css/**, /assets/**/js/** , /images/favicon.ico ,/images/logo.jpg, /test/**";
        String[] noInterceptor = noInterceptorStr.split(",");
        registry.addInterceptor(pjaxInterceptor).excludePathPatterns(noInterceptor);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("----------------------------------");
        log.info("初始化系统.");
        log.info("----------------------------------");

        this.setFreemarkerInit();

        log.info("----------------------------------");
        log.info("初始化系统.完毕.");
        log.info("----------------------------------");
    }

    private void setFreemarkerInit(){
        freeMarkerSpringConfig.setFreemarkerTagExt();
        freeMarkerSpringConfig.setStaticSource(findFreemarkerStaticSource());
    }

    private Map<String,String> findFreemarkerStaticSource(){
        log.info("查找带有 FreeMarkerStatic 注解的类。注册为 Freemarker 静态类。");
        Map<String,String> staticMap = new HashMap<>();

        List<Class<?>> classes = ClassUtil.getInstance().getClasses("com.liuqi.springbootsecurity", FreeMarkerStatic.class);
        log.info("查找带有 FreeMarkerStatic 注解的类。找到 {} 个",classes.size());

        if(CollectionUtils.isEmpty(classes)){
            log.warn("APP 中没有找到带 FreeMarkerStatic 注解的类。请确认配置。");
        }else {
            if (!CollectionUtils.isEmpty(classes)) {
                for (Class c : classes) {
                    log.info("设置 freemarker 静态资源 name : {} , package : {}", c.getSimpleName(), c.getName());
                    staticMap.put(c.getSimpleName(), c.getName());
                }
            }
        }

        return staticMap;

    }
}
