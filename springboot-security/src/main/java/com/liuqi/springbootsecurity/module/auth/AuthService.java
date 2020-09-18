package com.liuqi.springbootsecurity.module.auth;

import com.liuqi.springbootsecurity.module.auth.entity.*;

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
public interface AuthService {


    List<Role> getAllRoles();

    List<Integer> getMyRoles();


    List<Permission> getAllPermissions();

    List<Permission> getMyPermissions();



    List<Menu> getMyMenus();

    List<Menu> getAllMenus();



    List<UserRole> getUserRoles(Integer userId);



    List<RolePermission> getRolePermissions(List<Integer> roleIds);

}
