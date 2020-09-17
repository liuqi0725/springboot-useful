package com.liuqi.springbootsecurity.security.access;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.*;
import java.io.IOException;

/**
 * 类说明 <br>
 *     系统安全拦截器
 * <p>
 *
 *
 *
 *
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:25 PM 2020/3/2
 */
public class CustomerAccessInterceptor extends AbstractSecurityInterceptor implements Filter {

    /**
     * 注入 获取 url 权限的过滤器
     */
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    /**
     * 注入 决策器，决策器全局只能一个，通过 set 方式注入
     * @param customerAccessDecisionManager 决策器
     */
    public void setSystemAccessDecisionManager(CustomerAccessDecisionManager customerAccessDecisionManager) {
        super.setAccessDecisionManager(customerAccessDecisionManager);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);

        // fi 里面有一个被拦截的url
        // 1. 调用 SystemInvocationSecurityMetadataSource 的 getAttributes(Object object)这个方法获取fi对应的所有权限
        // 2. 调用 SystemAccessDecisionManager  的decide方法来校验用户的权限是否足够
        //super.setRejectPublicInvocations(true);
        InterceptorStatusToken token = super.beforeInvocation(fi);

        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    /**
     * 把 url 所需的权限类设置到拦截器中
     * @return {@link SecurityMetadataSource} 资源
     */
    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    /**
     * 注入获取系统权限资源的适配器
     * @param securityMetadataSource 适配器
     */
    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }
}
