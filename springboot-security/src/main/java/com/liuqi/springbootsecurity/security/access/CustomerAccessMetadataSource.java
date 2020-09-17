package com.liuqi.springbootsecurity.security.access;

import com.liuqi.springbootsecurity.security.SecurityConfigSource;
import com.liuqi.springbootsecurity.security.authentication.CustomerSecurityAuthenticationProcessService;
import com.liuqi.springbootsecurity.security.entity.SecurityPermission;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类说明 <br>
 *     自定义过滤规则,将用户request请求的url 与数据库权限进行，
 *     有匹配则将该 url 及 url所需要的权限 返回给 decide()【决策】方法，不存在则返回空
 *
 * @author : alexliu
 * @version v1.0 , Create at 7:56 PM 2020/3/2
 */
public class CustomerAccessMetadataSource implements FilterInvocationSecurityMetadataSource {

    private Map<AntPathRequestMatcher,Collection<ConfigAttribute>> permissionMap;

    private CustomerSecurityAuthenticationProcessService customerSecurityAuthenticationProcessService;

    public CustomerAccessMetadataSource(CustomerSecurityAuthenticationProcessService customerSecurityAuthenticationProcessService){
        Assert.notNull(customerSecurityAuthenticationProcessService,"In CustomerAccessMetadataSource , CustomerSecurityAuthenticationProcessService is require!");
        this.customerSecurityAuthenticationProcessService = customerSecurityAuthenticationProcessService;
    }

    private void loadPermissionMap(){
        List<? extends SecurityPermission> list = customerSecurityAuthenticationProcessService.getAllPermission();

        permissionMap = new HashMap<>();
        for(SecurityPermission p : list){
            // 如果权限 url 不为空 添加 url
            if(!StringUtils.isEmpty(p.getUrl())){
                permissionMap.put(new AntPathRequestMatcher(p.getUrl()) , SecurityConfig.createList(p.getUnKey()));
            }
        }

    }

    /**
     * 获取对比结果
     * @param o 可以包含了本次请求的所有内容，包含 HttpRequest 等
     * @return URL访问需要的认证集合
     * @throws IllegalArgumentException 参数错误
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        if(permissionMap == null){
            // 读取所有权限
            loadPermissionMap();
        }
        FilterInvocation fi = (FilterInvocation)o;

        // 查看是否白名单
        if(isPermitUrl(fi.getRequest())){
            // 不用通过决策器，直接访问
            return null;
        }

        Assert.notNull(permissionMap , "PermissionMap can`t be Null.check your code");

        for(AntPathRequestMatcher permissionURLMatcher : permissionMap.keySet()){

            if(permissionURLMatcher.matches(fi.getRequest())){
                // 返回 要访问此 URL 的权限及其他判断条件
                return permissionMap.get(permissionURLMatcher);
            }
        }

        // 不是白名单，没有权限  抛出异常
        throw new AccessDeniedException("No Permission");

        // 没匹配到返回 null
        // return null;
    }

    private boolean isPermitUrl(HttpServletRequest request){
        String[] permitUrl = SecurityConfigSource.getInstance().getPermitUrl();

        AntPathRequestMatcher matcher;
        for(String url : permitUrl){
            matcher = new AntPathRequestMatcher(url);
            if(matcher.matches(request)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
