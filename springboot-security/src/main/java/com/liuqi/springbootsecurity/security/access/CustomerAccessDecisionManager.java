package com.liuqi.springbootsecurity.security.access;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 类说明 <br>
 *     决策器，通过{@link FilterInvocationSecurityMetadataSource}
 *     获取用户当前访问的 url，url 需要访问的权限。
 *     在决策器中决定是否可以让用户的请求通过认证
 * <p>
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:14 PM 2020/3/2
 */
public class CustomerAccessDecisionManager implements AccessDecisionManager{

    private static final String CURRENT_AUTHENTICATION_ATTR = "CURRENT_AUTHENTICATION_ATTR";

    /**
     * @param authentication        用户认证通过后的认证对象，里面包含用户的权限列表
     * @param o                     包含客户端发起的请求的request信息，可以转换为HttpServletRequest
     * @param urlAuthAttribute      通过 {@link CustomerAccessMetadataSource} 中获取的访问 url 所需的权限及其他条件
     * @throws AccessDeniedException 访问异常
     * @throws InsufficientAuthenticationException 权限异常
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> urlAuthAttribute) throws AccessDeniedException, InsufficientAuthenticationException {

        // 如果 url 需要验证的规则为空，表示该 url 不需要拦截，直接放行
        if (CollectionUtils.isEmpty(urlAuthAttribute)) {
            return;
        }

        // url 验证
        String attr;
        for (ConfigAttribute c : urlAuthAttribute) {
            // 因为在 SystemFilterInvocationSecurityMetadataSource 中我们只是以权限作为验证 url 的唯一条件
            // 所以 urlAuthAttribute 中，只有`权限`一个规则
            // 可以添加其他规则
            attr = c.getAttribute();

            // 遍历当前登录用户的权限，查看是否有该权限
            // 也可以通过角色认证 角色认证 必须有前缀 `ROLE_`
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (attr.equals(grantedAuthority.getAuthority())){
                    // 记录当前访问权限在 session
                    // 可以用其他缓存对象保存
                    getRequest().getSession().setAttribute(CURRENT_AUTHENTICATION_ATTR ,attr);
                    return;
                }
            }
        }
        // 没有权限抛出决策失败的异常
        throw new AccessDeniedException("No Permission");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    /**
     * AbstractSecurityInterceptor 调用，来决定 AccessDecisionManager 是否可以执行传递ConfigAttribute。
     * 包含安全拦截器将显示的AccessDecisionManager支持安全对象的类型。
     * @param aClass class
     * @return 支持安全对象的类型
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    private static HttpServletRequest getRequest(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
        assert requestAttributes != null;
        return requestAttributes.getRequest();
    }
}
