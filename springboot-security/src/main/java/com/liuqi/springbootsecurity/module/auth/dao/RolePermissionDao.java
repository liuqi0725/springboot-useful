package com.liuqi.springbootsecurity.module.auth.dao;

import com.liuqi.springbootsecurity.module.auth.entity.RolePermission;
import com.liuqi.springbootsecurity.core.BaseDao;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
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
 * @version v1.0 , Create at 3:23 下午 2020/9/18
 */
@Repository
public class RolePermissionDao extends BaseDao<RolePermission, Serializable> {

    public List<RolePermission> selectRolePermissionsWithRoleIds(List<Integer> roleIds){
        return this.getSqlSession().selectList("selectRolePermissionListWithManyRoleId" , roleIds);
    }
}
