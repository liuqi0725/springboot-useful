package com.liuqi.springbootsecurity.security.error;


/**
 * 类说明 <br>
 *  密码错误
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:06 PM 2020/3/2
 */
public class UserAuthenticationPermissionError extends RuntimeException {

    public UserAuthenticationPermissionError(String username) {
        super("账号:["+username+"] , 认证错误，权限获取失败.");
    }
}
