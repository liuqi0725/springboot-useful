package com.liuqi.springbootsecurityredisnginx.security.authentication.provider;

import com.liuqi.springbootsecurityredisnginx.security.SecurityConfigSource;
import com.liuqi.springbootsecurityredisnginx.security.authentication.CustomerSecurityAuthenticationProcessService;
import com.liuqi.springbootsecurityredisnginx.security.authentication.token.UserLoginNormalAuthenticationToken;
import com.liuqi.springbootsecurityredisnginx.security.entity.SecurityUser;
import com.liuqi.springbootsecurityredisnginx.security.error.PasswordError;
import com.liuqi.springbootsecurityredisnginx.security.error.UserNotFoundError;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 类说明 <br>
 *     1. 用户登录的自定义认证的处理<br>
 *     2. 获取 Filter 封装的 token 信息<br>
 *     3. 通过 CustomerSecurityAuthenticationProcessService 获取用户信息【数据库】<br>
 *     4. 判断是否通过验证<br>
 *     5. 通过则重新创建一个新的 token ，设置已经通过验证，并返回给 manager<br>
 *
 *     正式环境如果有多个 provider 可以通过抽象类来实现。示例中为了速度，复制了 2 个，简单修改了下。
 *
 * @author : alexliu
 * @version v1.0 , Create at 2:11 PM 2020/3/10
 */
@Log4j2
public class UserNormalAuthenticationProvider implements AuthenticationProvider {

    private CustomerSecurityAuthenticationProcessService authenticationProcessService;

    private PasswordEncoder passwordEncoder;

    /**
     * 构造函数传第 自定义 userDetailService
     */
    public UserNormalAuthenticationProvider(){
        authenticationProcessService = SecurityConfigSource.getInstance().getSecurityAuthenticationProcessService();
        passwordEncoder = SecurityConfigSource.getInstance().getPasswordEncoder();
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
        UserLoginNormalAuthenticationToken authenticationToken = (UserLoginNormalAuthenticationToken) authentication;

        String username = (String) authenticationToken.getPrincipal();
        SecurityUser user = null;
        try {
            user = authenticationProcessService.loadUserByUsername(username);
        }catch (Exception e){
            String message = "登陆错误，请稍后再试。";
            if(e instanceof UserNotFoundError){
                message = e.getMessage();
            }
            throw new BadCredentialsException(message);
        }

        // 检验密码
        String inputPassoword = authenticationToken.getCredentials().toString();

        if(!passwordEncoder.matches(inputPassoword , user.getPassword())){
            throw new PasswordError(user.getUsername());
        }

        // 密码通过 获取用户权限组成认证用户
        // 获取用户信息（数据库认证）
        UserDetails userDetails = null;
        try {
            userDetails = authenticationProcessService.getUserAuthorities(user);
        } catch (Exception e){
            throw new BadCredentialsException(e.getMessage() , e);
        }

        //通过
        UserLoginNormalAuthenticationToken authenticationResult = new UserLoginNormalAuthenticationToken(userDetails, userDetails.getAuthorities());

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
        return UserLoginNormalAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
