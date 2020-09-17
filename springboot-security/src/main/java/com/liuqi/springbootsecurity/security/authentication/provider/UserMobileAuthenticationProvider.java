package com.liuqi.springbootsecurity.security.authentication.provider;

import com.liuqi.springbootsecurity.security.SecurityConfigSource;
import com.liuqi.springbootsecurity.security.authentication.CustomerSecurityAuthenticationProcessService;
import com.liuqi.springbootsecurity.security.authentication.token.UserLoginMobileAuthenticationToken;
import com.liuqi.springbootsecurity.security.entity.SecurityUser;
import com.liuqi.springbootsecurity.security.error.MobileCodeError;
import com.liuqi.springbootsecurity.security.error.MobileNotFoundError;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 类说明 <br>
 *     1. 用户登录的自定义认证的处理<br>
 *     2. 获取 Filter 封装的 token 信息<br>
 *     3. 通过 CustomerSecurityAuthenticationProcessService 获取用户信息【数据库】<br>
 *     4. 判断是否通过验证<br>
 *     5. 通过则重新创建一个新的 token ，设置已经通过验证，并返回给 manager<br>
 *
 * @author : alexliu
 * @version v1.0 , Create at 2:11 PM 2020/3/10
 */
@Log4j2
public class UserMobileAuthenticationProvider implements AuthenticationProvider {

    private CustomerSecurityAuthenticationProcessService authenticationProcessService;

    /**
     * 构造函数传第 自定义 userDetailService
     */
    public UserMobileAuthenticationProvider(){
        authenticationProcessService = SecurityConfigSource.getInstance().getSecurityAuthenticationProcessService();
    }

    /**
     * 认证
     * @param authentication 用户登录的认证信息
     * @return 认证结果
     * @throws AuthenticationException 认证异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取过滤器封装的token信息
        UserLoginMobileAuthenticationToken authenticationToken = (UserLoginMobileAuthenticationToken) authentication;

        SecurityUser user = null;
        String mobile = (String) authenticationToken.getPrincipal();
        String mobileCode = (String)authenticationToken.getCredentials();
        try {
            user = authenticationProcessService.loadUserByMobile(mobile , mobileCode);
        } catch (Exception e){
            String message = "登陆错误，请稍后再试。";
            if(e instanceof MobileCodeError || e instanceof  MobileNotFoundError){
                message = e.getMessage();
            }
            throw new BadCredentialsException(message);
        }

        // 获取用户权限组成认证用户
        // 获取用户信息（数据库认证）
        UserDetails userDetails = null;
        try {
            userDetails = authenticationProcessService.getUserAuthorities(user);
        } catch (Exception e){
            throw new BadCredentialsException(e.getMessage() , e);
        }

        //通过
        UserLoginMobileAuthenticationToken authenticationResult = new UserLoginMobileAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    /**
     * 由 提供给 spring-security AuthenticationManager <br>
     * 根据 token 类型，判断使用那个 Provider
     * @param authentication 提供manager 识别认证器
     * @return 是否匹配
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UserLoginMobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
