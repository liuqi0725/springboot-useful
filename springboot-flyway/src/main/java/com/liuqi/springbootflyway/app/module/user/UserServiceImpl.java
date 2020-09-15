package com.liuqi.springbootflyway.app.module.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired(required = false)
    private UserDao userDao;

    @Override
    public User selectUser(Integer id) {

        User user = userDao.selectOne(id);

        if(user == null){
            throw new NullPointerException("ID：["+id+"] 用户不存在");
        }
        return user;
    }
}
