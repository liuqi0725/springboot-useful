package com.liuqi.springbootsecurityredisnginx.utils;

import com.liuqi.springbootsecurityredisnginx.security.SecurityConfigSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 3:54 下午 2020/9/27
 */
public class SessionUtil {

    private SessionUtil(){

    }

    public static SessionUtil getInstance(){
        return SessionUtilHook.instance;
    }

    private static class SessionUtilHook{
        private static final SessionUtil instance = new SessionUtil();
    }

    private static HttpServletRequest getRequest(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
        assert requestAttributes != null;
        return requestAttributes.getRequest();
    }
}
