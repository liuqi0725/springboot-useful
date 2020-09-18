package com.liuqi.springbootsecurity.module.auth;

import com.liuqi.springbootsecurity.constant.WebCommonAttribute;
import com.liuqi.springbootsecurity.module.auth.dao.*;
import com.liuqi.springbootsecurity.module.auth.entity.*;
import com.liuqi.springbootsecurity.module.user.User;
import com.liuqi.springbootsecurity.security.SecurityUtil;
import com.liuqi.springbootsecurity.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 11:15 上午 2020/9/18
 */
@Service
public class AuthServiceImpl implements AuthService{

    /*
        正式环境建议逻辑分开，每个独立一个模块，通过 adapter 访问。降低耦合
     */

    private MenuDao menuDao;

    private PermissionDao permissionDao;

    private RoleDao roleDao;

    private RolePermissionDao rolePermissionDao;

    private UserRoleDao userRoleDao;

    @Autowired
    public AuthServiceImpl(MenuDao menuDao,
                           PermissionDao permissionDao,
                           RoleDao roleDao,
                           RolePermissionDao rolePermissionDao,
                           UserRoleDao userRoleDao){

        this.menuDao = menuDao;
        this.permissionDao = permissionDao;
        this.roleDao = roleDao;
        this.rolePermissionDao = rolePermissionDao;
        this.userRoleDao = userRoleDao;

    }



    @Override
    public List<Role> getAllRoles() {
        Role role = new Role();
        role.setStatus(1);
        return roleDao.selectListSelective(role);
    }

    @Override
    public List<Integer> getMyRoles() {
        // spring security 获取
        return SecurityUtil.getUserInfo(User.class).getRoleIds();
    }

    @Override
    public List<Permission> getAllPermissions() {
        Permission permission = new Permission();
        permission.setStatus(1);
        return permissionDao.selectListSelective(permission);
    }

    @Override
    public List<Permission> getMyPermissions() {
        // spring security 获取
        return (List<Permission>) SecurityUtil.getUserInfo(User.class).getPermission();
    }

    @Override
    public List<Menu> getMyMenus() {
        // session 中获取
        return (List<Menu>) WebUtil.getSessionAttribute(WebCommonAttribute.CURRENT_USER_MENUS);
    }

    @Override
    public List<Menu> getAllMenus() {
        Menu menu = new Menu();
        menu.setStatus(1);
        return menuDao.selectListSelective(menu);
    }

    @Override
    public List<UserRole> getUserRoles(Integer userId) {
        UserRole userRole = new UserRole();
        userRole.setUid(userId);
        return userRoleDao.selectListSelective(userRole);
    }

    @Override
    public List<RolePermission> getRolePermissions(List<Integer> roleIds) {
        return rolePermissionDao.selectRolePermissionsWithRoleIds(roleIds);
    }
}
