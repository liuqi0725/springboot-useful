package com.liuqi.springbootsecurityredisnginx.security.error;


/**
 * 类说明 <br>
 *  登陆异常，[系统级错误]，比如 数据库异常、io 异常等，会统一处理返回给用户的信息
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:06 PM 2020/3/2
 */
public class BadLoginError extends RuntimeException {

    public BadLoginError(String msg) {
        super(msg);
    }

    public BadLoginError(String msg , Throwable t) {
        super(msg, t);
    }

}
