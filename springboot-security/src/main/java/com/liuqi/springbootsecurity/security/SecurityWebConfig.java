package com.liuqi.springbootsecurity.security;


import com.liuqi.springbootsecurity.security.access.CustomerAccessDecisionManager;
import com.liuqi.springbootsecurity.security.access.CustomerAccessInterceptor;
import com.liuqi.springbootsecurity.security.access.CustomerAccessMetadataSource;
import com.liuqi.springbootsecurity.security.authentication.CustomerSecurityAuthenticationProcessService;
import com.liuqi.springbootsecurity.security.authentication.UserAuthenticationFilter;
import com.liuqi.springbootsecurity.security.authentication.provider.UserMobileAuthenticationProvider;
import com.liuqi.springbootsecurity.security.authentication.provider.UserNormalAuthenticationProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import java.util.Arrays;

/**
 * 类说明 <br>
 * 通过@EnableWebSecurity注解开启Spring Security的功能
 * 继承 WebSecurityConfigurerAdapter，并重写它的方法来设置一些web安全的细节
 * configure(HttpSecurity http)方法，
 *      通过authorizeRequests()定义哪些URL需要被保护、哪些不需要被保护。
 *      通过formLogin()定义当需要用户登录时候，转到的登录页面。
 *
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:13 PM 2020/3/4
 */
@EnableWebSecurity
@Log4j2
public class SecurityWebConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomerSecurityAuthenticationProcessService securityAuthenticationProcessService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 注入外部 认证业务处理类
        SecurityConfigSource.getInstance().setSecurityAuthenticationProcessService(securityAuthenticationProcessService);

        // 设置外部 密码策略，设置 null ，采用 spring security 默认
        SecurityConfigSource.getInstance().setCustomerPasswordEncoder(null);


        // 添加访问过滤器(access) ， 添加在 FilterSecurityInterceptor 之后
        http.addFilterAfter(getAccessInterceptor(), FilterSecurityInterceptor.class);

        // 添加认证过滤器(authentication) ， 替换【或者之前】 UsernamePasswordAuthenticationFilter
        http.addFilterAt(getUserAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 允许 iframe
        setFrameAllow(http);

        http.authorizeRequests()
                .antMatchers(SecurityConfigSource.getInstance().getPermitUrl()).permitAll() // 白名单
                .anyRequest().authenticated()           // 其他地址的访问均需验证权限
                .and()
                .formLogin()
                .loginPage("/login")                    // 登陆请求 URL
                .loginProcessingUrl("/login")           // 登录 action 提交的 URL
                .defaultSuccessUrl("/dashboard")        // 登录成功的响应 URL
                .failureUrl("/login?error")             // 登陆失败的 URL，spring security 默认。
                .permitAll();

        // 允许 cookie ，参数为 cookie 有效期 单位：秒
        http.rememberMe().tokenValiditySeconds(172800);

        // 允许 csrf ，允许后所有 post 请求必须带 csrf， 为了测试，关闭 csrf
        http.csrf().disable();
        // 不关闭 csrf ,也可以设置不需要 csrf 的url 白名单
        //http.csrf().ignoringAntMatchers(new String[]{});

        // 退出登录,成功的响应 URL
        http.logout().permitAll().logoutSuccessUrl("/login");
    }

    /**
     * 设置允许 iframe
     * @param http security http {@link HttpSecurity}
     * @throws Exception 异常
     */
    private void setFrameAllow(HttpSecurity http) throws Exception {

        /*
         * iframe 允许显示的方式 <br>
         * SAMEORIGIN 仅允许 frame 页面当前域名下的显示 <br>
         *
         * FROMURI 允许 frame 页面在指定域名下显示 <br>
         *     例如：
         *     <ul>
         *          <li>http://www.baidu.com 允许该域名可以嵌套我的 frame</li>
         *          <li>http://www.taobao.com 允许该域名可以嵌套我的 frame</li>
         *     </ul>
         */

        // 正式环境请配置在配置文件中。方便管理

        String xframe = "SAMEORIGIN";
        // 如果是 FROMURI 允许嵌套的外部域名白名单
        String[] frameAllowWhiteDomain = new String[]{"https://example.cn","https://example.com"};

        if(xframe.equals("SAMEORIGIN")){
            // 仅允许本域名
            http.headers().frameOptions().sameOrigin();
        }else if(xframe.equals("FROMURI")){
            //disable 默认策略。 这一句不能省。
            http.headers().frameOptions().disable();
            //新增新的策略。
            http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(
                    new WhiteListedAllowFromStrategy(Arrays.asList(frameAllowWhiteDomain))
            ));
        }else{
            throw new Exception("未知的 XFrameOptions 。仅支持 SAMEORIGIN , FROMURI");
        }
    }

    /**
     * 获取 access 访问拦截器
     * @return 拦截器 {@link CustomerAccessInterceptor}
     */
    private CustomerAccessInterceptor getAccessInterceptor(){
        log.info("SpringSecurity >> 获取 Access 拦截器");

        CustomerAccessInterceptor customerAccessInterceptor = new CustomerAccessInterceptor();
        // 设置拦截器资源， 因为资源需要提供权限，所以传入 CustomerSecurityAuthenticationProcessService 实例
        customerAccessInterceptor.setSecurityMetadataSource(new CustomerAccessMetadataSource(SecurityConfigSource.getInstance().getSecurityAuthenticationProcessService()));
        // 设置决策器
        customerAccessInterceptor.setSystemAccessDecisionManager(new CustomerAccessDecisionManager());
        return customerAccessInterceptor;
    }

    /**
     * 获取用户 authentication 过滤器
     * @return 过滤器 {@link UserAuthenticationFilter}
     */
    private UserAuthenticationFilter getUserAuthenticationFilter(){
        log.info("安全组件 >> 获取用户 authentication 过滤器");

        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter();
        // 创建处理 authentication 的 Provider
        // 创建 2 个
        // 一个普通，账号密码
        // 一个通过手机 验证码登陆
        ProviderManager providerManager = new ProviderManager(Arrays.asList(new UserNormalAuthenticationProvider() , new UserMobileAuthenticationProvider()));
        userAuthenticationFilter.setAuthenticationManager(providerManager);
        return userAuthenticationFilter;
    }

}
