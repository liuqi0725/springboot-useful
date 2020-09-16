package com.liuqi.springbootfreemarkerpjax.core;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类说明 <br>
 *     Pjax 请求拦截器
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 22:41 2020-03-25
 */
@Log4j2
@Component
public class PjaxInterceptor implements HandlerInterceptor {

    private String RequestURI = "";

    private static final String PJAX_TEMPLATE_PATH = "/common/basePjax.ftl";

    private static final String NORMAL_TEMPLATE_PATH = "/common/base.ftl";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String path = request.getContextPath();
        String basePath = scheme + "://" + serverName + ":" + port + path;
        request.setAttribute("basePath", basePath);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        RequestURI = request.getRequestURI();

        if(modelAndView == null){
            // == 空 请求可能是下载文件等，或者错误。不执行模板渲染
            return;
        }
        if(isPjax(request)){
            setPjaxResult(modelAndView);
        }else if(!isAjax(request)){
            // 不是 pjax 不是 ajax
            // 返回普通视图模板
            setNormalResult(modelAndView);
        }else{
            //不是 pjax 不是 ajax 不处理
            return ;
        }

        if(log.isDebugEnabled()){
            log.debug("当前请求 {} , return ModelAndView : {}", RequestURI, modelAndView.getModelMap().get("baseTemplate"));
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

    private void setPjaxResult(ModelAndView modelAndView){
        modelAndView.addObject("baseTemplate", PJAX_TEMPLATE_PATH);
    }

    private void setNormalResult(ModelAndView modelAndView){
        modelAndView.addObject("baseTemplate", NORMAL_TEMPLATE_PATH);
    }


    private Boolean isPjax(HttpServletRequest request){
        Boolean pjax = Boolean.parseBoolean(request.getHeader("X-PJAX"));
        if(log.isDebugEnabled()){
            log.debug("当前请求 {} , is Pjax [X-PJAX] : {}",RequestURI,pjax);
        }
        return pjax;
    }

    private Boolean isAjax(HttpServletRequest request){
        Boolean ajax = Boolean.FALSE;
        // 判断是否 ajax
        if(!StringUtils.isEmpty(request.getHeader("x-requested-with")) && request.getHeader("x-requested-with").equals("XMLHttpRequest")){
            ajax = Boolean.TRUE;
        }

        if(log.isDebugEnabled()){
            log.debug("当前请求 {} , is Ajax [x-requested-with] [{}] ",RequestURI, ajax);
        }

        return ajax;
    }

}