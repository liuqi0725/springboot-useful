package com.liuqi.springbootsecurity.module.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
 * @version v1.0 , Create at 10:44 上午 2020/9/18
 */
@Mapper
public interface UserDao {

    @Select("select * from sys_user")
    List<User> selectList();

    @Select("select * from sys_user where id=#{id}")
    User selectOne(int id);

    @Select("select * from sys_user where username=#{username}")
    User selectByUsername(String username);

    @Select("select * from sys_user where mobile=#{mobile}")
    User selectByMobile(String mobile);
}
