package com.liuqi.springbootsecurity.module.adapter.impl;


import com.liuqi.springbootsecurity.constant.WebCommonAttribute;
import com.liuqi.springbootsecurity.module.adapter.AppUserSecurityAdapter;
import com.liuqi.springbootsecurity.module.auth.AuthService;
import com.liuqi.springbootsecurity.module.auth.entity.*;
import com.liuqi.springbootsecurity.module.user.User;
import com.liuqi.springbootsecurity.module.user.UserService;
import com.liuqi.springbootsecurity.security.entity.SecurityUser;
import com.liuqi.springbootsecurity.security.error.BadLoginError;
import com.liuqi.springbootsecurity.security.error.MobileCodeError;
import com.liuqi.springbootsecurity.security.error.MobileNotFoundError;
import com.liuqi.springbootsecurity.security.error.UserNotFoundError;
import com.liuqi.springbootsecurity.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 类说明 <br>
 * 系统业务与 Spring security 适配器，提过 security 调用获取资源
 *
 * @author : alexliu
 * @version v1.0 , Create at 11:00 上午 2020/9/18
 */
@Service
public class AppUserSecurityAdapterImpl implements AppUserSecurityAdapter {

    private UserService<User> userService;

    private AuthService authService;

    @Autowired
    public AppUserSecurityAdapterImpl(UserService<User> userService , AuthService authService){
        this.userService = userService;
        this.authService = authService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Permission> getAllPermission() {
        return authService.getAllPermissions();
    }

    @Override
    public SecurityUser loadUserByMobile(String mobile, String mobileCode) {

        try {
            User user = userService.selectByMobile(mobile);
            // 测试，验证码都为 123456
            if(!mobileCode.equalsIgnoreCase("123456")){
                throw new MobileCodeError(mobile , mobileCode);
            }

            return user;
        }catch (NullPointerException e){
            throw new MobileNotFoundError(mobile);
        }
    }

    @Override
    public SecurityUser loadUserByUsername(String userName) {
        try {
            return userService.selectByUsername(userName);
        }catch (NullPointerException e){
            throw new UserNotFoundError(userName);
        }
    }

    @Override
    public SecurityUser getUserAuthorities(SecurityUser securityUser) {
        User user;

        if (securityUser instanceof User) {
            user = (User) securityUser;
        } else {
            throw new BadLoginError("通过 [" + securityUser.getUsername() + "] , 获取认证失败! user 不是 User 类型");
        }

        if(user.getStatus().equals(99)){
            throw new BadLoginError("[" + user.getUsername() + "] 用户已被停用！");
        }

        List<UserRole> userRoleList = this.loadUserRoles(user);
        if(CollectionUtils.isEmpty(userRoleList)){
            throw new BadLoginError("[" + user.getUsername() + "] 用户没有角色信息！");
        }

        // 获取角色 ids
        List<Integer> roleIds = userRoleList.stream().map(UserRole::getRid).collect(Collectors.toList());

        // 是否超级管理员
        user.setSuperAdmin(isSuperAdmin(roleIds));

        // 读取用户权限集合
        this.loadUserPermission(user, roleIds);

        user.setRoleIds(roleIds);

        // 获取用户 menus
        this.loadUserMenus(user);

        return user;
    }


    private List<UserRole> loadUserRoles(User user){
        // 查询用户的角色
        UserRole userRole = new UserRole();
        userRole.setUid(user.getId());

        // 查询该用户的所有的角色
        return authService.getUserRoles(user.getId());
    }

    private boolean isSuperAdmin(List<Integer> userRoleIds){
        // 查询所有角色
        List<Role> allRoles = authService.getAllRoles();

        for(Role role : allRoles){
            // 如果用户具备超级管理员权限
            if(userRoleIds.contains(role.getId()) && role.getUnKey().equals("SUPER_ADMIN")){
                return true;
            }
        }
        return false;
    }

    private void loadUserPermission(User user , List<Integer> userRoleIds){
        // 查询所有权限
        List<Permission> allPermissions = authService.getAllPermissions();

        // spring-security 权限集合
        if(user.isSuperAdmin()){
            // 赋予所有权限
            for(Permission p : allPermissions) {
                user.setAuthorities(p);
            }
        }else{
            // 查询角色的权限
            List<RolePermission> srpList = authService.getRolePermissions(userRoleIds);

            if(CollectionUtils.isEmpty(srpList)){
                throw new BadLoginError("[" + user.getUsername() + "] 用户没有权限！");
            }

            List<Integer> rolePermissions = srpList.stream().map(RolePermission::getPid).collect(Collectors.toList());

            // 匹配权限
            for(Permission p : allPermissions) {
                if (rolePermissions.contains(p.getId())) {
                    user.setAuthorities(p);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadUserMenus(User user){

        List<Permission> userPermissions = (List<Permission>) user.getPermission();
        // 获取全部的 menus
        List<Menu> allMenus = authService.getAllMenus();
        List<Menu> userMenus = loadUserMenus(userPermissions , allMenus, null);
        // 保存到 session 中
        WebUtil.setSessionAttribute(WebCommonAttribute.CURRENT_USER_MENUS , userMenus);
    }

    private List<Menu> loadUserMenus(List<Permission> userPermissions ,
                                     List<Menu> allMenus ,
                                     Menu parentMenu){

        List<Menu> userMenuList = new ArrayList<>();
        List<Menu> tempMenus;
        for(Menu m : allMenus){

            if(parentMenu != null){
                // 如果有父节点，检查是否父节点的子节点，不是则跳出
                if(!parentMenu.getId().equals(m.getPid())){
                    continue;
                }else{
                    // pid != 0 判断子节点有无权限
                    if(userHasPermissionForMenu(m , userPermissions)){
                        userMenuList.add(m);
                    }
                }
            }

            // 多级菜单需要自己判断父节点进行递归
            if(m.getPid().equals(0)){

                // 往下找符合的子节点
                tempMenus = loadUserMenus(userPermissions , allMenus , m);
                // 判断是否有子节点
                // 1. 有子节点 ，则 pid=0 的节点可以访问
                // 2. 无子节点 ，判断pid=0的节点有无权限访问
                if(!CollectionUtils.isEmpty(tempMenus)){
                    // 有子节点 该父节点可访问
                    userMenuList.add(m);
                    // 添加子节点
                    userMenuList.addAll(tempMenus);
                }else{
                    // 判断有无权限，有则添加该 pid=0 的节点
                    if(userHasPermissionForMenu(m , userPermissions)){
                        userMenuList.add(m);
                    }
                }
            }
        }

        return userMenuList;
    }

    private boolean userHasPermissionForMenu(Menu menu , List<Permission> userPermissions){
        AntPathMatcher matcher = new AntPathMatcher();
        // 匹配权限
        for(Permission permission : userPermissions){
            if(matcher.match(permission.getUrl() , menu.getUrl())){
                // 用户有该菜单
                return true;
            }
        }
        return false;
    }
}
