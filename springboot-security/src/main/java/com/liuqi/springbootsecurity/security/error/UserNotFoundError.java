package com.liuqi.springbootsecurity.security.error;

/**
 * 类说明 <br>
 *  找不到用户错误
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:06 PM 2020/3/2
 */
public class UserNotFoundError extends RuntimeException {

    public UserNotFoundError(String username) {
        super("找不到用户 ["+username+"]");
    }
}
