package com.liuqi.springbootsecurityredisnginx.security.authentication;

import com.liuqi.springbootsecurityredisnginx.security.entity.SecurityPermission;
import com.liuqi.springbootsecurityredisnginx.security.entity.SecurityUser;

import java.util.List;

/**
 * 类说明 <br>
 *     由外部实现，Security 在认证时调用
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:18 PM 2020/3/4
 */
public interface CustomerSecurityAuthenticationProcessService {

    <T extends SecurityPermission> List<T> getAllPermission();

    SecurityUser loadUserByMobile(String mobile,String mobileCode);

    SecurityUser loadUserByUsername(String userName);

    SecurityUser getUserAuthorities(SecurityUser user);
}
