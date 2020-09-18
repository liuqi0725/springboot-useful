package com.liuqi.springbootsecurity.security;

import com.liuqi.springbootsecurity.security.authentication.CustomerSecurityAuthenticationProcessService;
import com.liuqi.springbootsecurity.security.crypto.CustomerPasswordEncoder;
import com.liuqi.springbootsecurity.security.crypto.CustomerPasswordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * 类说明 <br>
 *     安全组件与第三方链接 source 单例类
 *
 * @author : alexliu
 * @version v1.0 , Create at 15:04 2020-03-16
 */
@Log4j2
public class SecurityConfigSource {

    /**
     * 允许的 URL
     */
    private String[] permitUrl;


    /**
     * 密码加解密方式
     */
    private PasswordEncoder passwordEncoder;

    /**
     * 外部实现的认证 service
     */
    private CustomerSecurityAuthenticationProcessService securityAuthenticationProcessService;


    /**
     * 不能通过 new 创建实例
     */
    private SecurityConfigSource(){

    }

    public static SecurityConfigSource getInstance(){
        return SecurityConfigSourceHook.instance;
    }

    private static class SecurityConfigSourceHook{
        private static final SecurityConfigSource instance = new SecurityConfigSource();
    }

    /**
     * 设置用户认证 Service
     * @param securityAuthenticationProcessService {@link CustomerSecurityAuthenticationProcessService} 由外部实现
     */
    public void setSecurityAuthenticationProcessService(CustomerSecurityAuthenticationProcessService securityAuthenticationProcessService) {
        log.info("安全组件配置 >> 获取登陆认证客户 与 VanasSecurity 的数据库实现层");
        Assert.notNull(securityAuthenticationProcessService , "CustomerSecurityAuthenticationProcessService 不能为空。请在系统中实现 CustomerSecurityAuthenticationProcessService，并在 SecurityWebConfig 初始化中注入。");
        this.securityAuthenticationProcessService = securityAuthenticationProcessService;
    }

    /**
     * 获取用户认证 Service
     * @return securityAuthenticationProcessService {@link CustomerSecurityAuthenticationProcessService}
     */
    public CustomerSecurityAuthenticationProcessService getSecurityAuthenticationProcessService() {
        return securityAuthenticationProcessService;
    }

    /**
     * 获取密码验证工具
     * @return PasswordEncoder {@link PasswordEncoder}
     */
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * 设置密码生成策略
     * @param customerPasswordService {@link CustomerPasswordService} 由第三方实现，可为空
     */
    public void setCustomerPasswordEncoder(CustomerPasswordService customerPasswordService){

        log.info("安全组件配置 >> 获取登陆安全认证的密码认证器");

        if(customerPasswordService == null || !customerPasswordService.enabled()){
            log.info("安全组件配置 >> 获取登陆安全认证的密码认证器 ， 使用系统默认 BCryptPasswordEncoder");

            /*
             * Spring Security 提供了BCryptPasswordEncoder类,实现Spring的PasswordEncoder接口使用BCrypt强哈希方法来加密密码。
             * 即 密码 + 盐
             */
            this.passwordEncoder = new BCryptPasswordEncoder();
        }else{
            log.info("安全组件配置 >> 获取登陆安全认证的密码认证器 ， 使用系统自定义 CustomerPasswordEncoder");
            // 自定义密码加密方式
            this.passwordEncoder = new CustomerPasswordEncoder(customerPasswordService);
        }
    }

    /**
     * 获取允许访问的 URL
     * @return String[]
     */
    public String[] getPermitUrl(){
        // 正式编码可以写在配置文件中 ,
        // '/error' 必须配置， 当错误时，会调用/error，不过不配置，会一直匹配不到/error 的权限，导致一直redirect 到/login
        return new String[]{"/","/login","/error","/hello","/logout", "/login/error" , "/assets/**/vendor/**","/assets/**/css/**", "/assets/**/js/**"};
    }
}
