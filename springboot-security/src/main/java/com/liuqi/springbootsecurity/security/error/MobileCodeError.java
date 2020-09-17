package com.liuqi.springbootsecurity.security.error;

/**
 * 类说明 <br>
 *  找不到手机用户
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:06 PM 2020/3/2
 */
public class MobileCodeError extends RuntimeException {

    public MobileCodeError(String mobile , String mobileCode) {
        super("手机: ["+mobile+"] 登陆验证码:["+mobileCode+"]错误.");
    }
}
