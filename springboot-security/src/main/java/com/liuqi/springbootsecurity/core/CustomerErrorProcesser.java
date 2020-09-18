package com.liuqi.springbootsecurity.core;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
 * @version v1.0 , Create at 11:40 下午 2020/9/18
 */
//给容器中加入我们自己定义的ErrorAttributes
@Component
public class CustomerErrorProcesser extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        errorAttributes.put("personal", "houchen");
        Map<String,Object> etx = (Map<String,Object>)webRequest.getAttribute("etx", 0);
        errorAttributes.put("etx", etx);
        return errorAttributes;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("error", ex.getMessage());
        mv.addObject("exception", ex);
        mv.setViewName("error");

        return mv;
    }
}
