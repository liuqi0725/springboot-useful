package com.liuqi.springbootsecurityredisnginx.security.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 类说明 <br>
 * <p>
 *
 *
 *
 *
 *
 * @author : alexliu
 * @version v1.0 , Create at 11:37 PM 2020/3/1
 */
public abstract class SecurityUser implements UserDetails {

    protected String username;

    protected String password;

    protected String mobile;

    protected List<Integer> roleIds;

    protected boolean isSuperAdmin;

    protected Collection<? extends GrantedAuthority> authorities;

    protected Collection<? extends SecurityPermission> permissions;


    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public void setAuthorities(List<SecurityPermission> authorities) {

        List<SimpleGrantedAuthority> list = new ArrayList<>();
        List<SecurityPermission> pList = new ArrayList<>();

        for(SecurityPermission p : authorities) {
            list.add(new SimpleGrantedAuthority(p.getUnKey()));
            pList.add(p);
        }
        this.permissions = pList;
        this.authorities = list;
    }

    @SuppressWarnings("unchecked")
    public void setAuthorities(SecurityPermission permission) {
        List<SimpleGrantedAuthority> list;
        List<SecurityPermission> pList;

        if(getAuthorities() == null){
            list = new ArrayList<>();
            pList = new ArrayList<>();
        }else{
            list = (List<SimpleGrantedAuthority>)getAuthorities();
            pList = (List<SecurityPermission>)getPermission();
        }
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority(permission.getUnKey());
        list.add(auth);
        pList.add(permission);

        this.authorities = list;
        this.permissions = pList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public Collection<? extends SecurityPermission> getPermission(){
        return permissions;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 账号是否 未过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 账号是否 未锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 密码是 否未过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 是否激活
        return true;
    }
}
