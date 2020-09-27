package com.liuqi.springbootsecurityredisnginx.core.freemarker;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.io.Serializable;
import java.util.Map;

/**
 * 类说明 <br>
 * freemarker 在 spring 中的配置
 *
 * @author : alexliu
 * @version v1.0 , Create at 4:37 下午 2020/9/18
 */
@Component
@Log4j2
public class FreeMarkerSpringConfig implements Serializable {
    private static final long serialVersionUID = 7225569316034483810L;

    private freemarker.template.Configuration configuration;

    private FreeMarkerViewResolver freeMarkerViewResolver;

    public FreeMarkerSpringConfig(freemarker.template.Configuration configuration , FreeMarkerViewResolver freeMarkerViewResolver){
        this.configuration = configuration;
        this.freeMarkerViewResolver = freeMarkerViewResolver;
    }

    /**
     * 设置 freemarker 标签扩展
     */
    public void setFreemarkerTagExt(){
        log.info("加载 Freemarker 配置 >>> Freemarker set Tag [Extends , Override , Super , Block]");

        // 添加对 override block extends super 支持 , 需要引入 rapid_framework 的支持
//        configuration.setSharedVariable("block" , new BlockDirective());
//        configuration.setSharedVariable("override" , new OverrideDirective());
//        configuration.setSharedVariable("extends" , new ExtendsDirective());
//        configuration.setSharedVariable("super", new SuperDirective());
    }

    /**
     * 设置静态资源
     * @param staticClass 静态类
     */
    public void setStaticSource(Map<String,String> staticClass) {
        log.info("加载 Freemarker 配置 >>> Freemarker set templates used Java static class and enum.");
        freeMarkerViewResolver.setAttributesMap(new FreemarkerStaticSource(staticClass));
    }

}
