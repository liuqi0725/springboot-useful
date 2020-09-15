package com.liuqi.springbootflyway.app.module.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:49 上午 2020/9/15
 */
@Mapper
public interface UserDao {

    @Select("select * from sys_user where id=#{id}")
    User selectOne(int id);
}
