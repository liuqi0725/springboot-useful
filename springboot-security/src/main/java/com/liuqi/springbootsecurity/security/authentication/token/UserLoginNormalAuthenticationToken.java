package com.liuqi.springbootsecurity.security.authentication.token;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 类说明 <br>
 *     自定义用户 login token。
 *
 *     参考 UsernamePasswordAuthenticationToken 写即可
 *
 *     下一步进入 filter ，由 filter 组装认证数据传第给 认证处理器(Provider)
 *
 * @author : alexliu
 * @version v1.0 , Create at 11:20 2020-03-16
 */
@Log4j2
public class UserLoginNormalAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 520L;

    private final Object principal;

    private Object credentials;

    private Object vc;

    /**
     * 当进入 filter 后，创建了不同的 token 的构造函数
     * @param principal username
     * @param credentials password
     */
    public UserLoginNormalAuthenticationToken(Object principal, Object credentials) {
        super((Collection)null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false); // 是否已经认证，false  , 在后续provider 中认证通过设置为 true
        log.info("UserLoginNormalAuthenticationToken setAuthenticated ->false loading ...");
    }

    /**
     * 当 token 在 provider 中通过验证，调用该构造函数创建新的 token
     * @param principal 用户信息 {@link UserDetails}
     * @param authorities 用户的权限集合
     */
    public UserLoginNormalAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // 是否已经认证，true
        log.info("VanasUserAuthenticationToken setAuthenticated ->true loading ...");
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
