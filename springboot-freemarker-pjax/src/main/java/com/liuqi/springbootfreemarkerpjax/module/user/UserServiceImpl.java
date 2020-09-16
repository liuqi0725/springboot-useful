package com.liuqi.springbootfreemarkerpjax.module.user;

import org.springframework.stereotype.Service;

import java.util.Arrays;
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
 * @version v1.0 , Create at 10:50 上午 2020/9/15
 */
@Service
public class UserServiceImpl implements UserService<User>{

    private final List<User> userList = Arrays.asList(
            new User(1,"liuqi", "35Liu" , "13980000000", "liuqi_0725@aliyun.com"),
            new User(1,"zhangsan", "张三" , "13980000001", "zhangsan@aliyun.com"),
            new User(1,"lisi", "李四" , "13980000002", "lisi@aliyun.com")
    );

    @Override
    public List<User> userList() {
        return userList;
    }

    @Override
    public User userInfo(Integer id) {

        for(User user : userList){
            if(user.getId().equals(id)){
                return user;
            }
        }
        throw new NullPointerException("ID：["+id+"] 用户不存在");
    }
}
