package com.liuqi.springbootsecurityredisnginx.core;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.log4j.Log4j2;

import java.io.Writer;

/**
 * 类说明 <br>
 *     freemarker 异常统一处理，拦截加载 freemarker 页面错误的所有异常，让页面不崩溃，并抛出异常，由 springboot Error 统一处理。
 * @author : alexliu
 * @version v1.0 , Create at 10:15 PM 2020/3/7
 */
@Log4j2
public class FreemarkerExceptionHandler implements TemplateExceptionHandler {

    @Override
    public void handleTemplateException(TemplateException e, Environment environment, Writer writer) throws TemplateException {
        log.error("Freemarker load Page Error: ",e);
        log.error("[出错了，请联系网站管理员]", e);
        // 抛出异常，由 springboot 接管统一处理
        throw e;
    }
}
