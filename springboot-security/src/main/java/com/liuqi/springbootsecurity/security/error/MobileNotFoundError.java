package com.liuqi.springbootsecurity.security.error;

/**
 * 类说明 <br>
 *  找不到手机用户
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:06 PM 2020/3/2
 */
public class MobileNotFoundError extends RuntimeException {

    public MobileNotFoundError(String mobile) {
        super("找不到手机为 ["+mobile+"] 的用户.");
    }
}
