package com.liuqi.springbootsecurityredisnginx.module.user;

import com.liuqi.springbootsecurityredisnginx.security.SecurityUtil;
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
 * @version v1.0 , Create at 10:50 上午 2020/9/15
 */
@Service
public class UserServiceImpl implements UserService<User> {

    @Autowired(required = false)
    private UserDao userDao;

    @Override
    public List<User> userList() {

        return userDao.selectList();
    }

    @Override
    public User userInfo(Integer id) {
        return makeUserRet(userDao.selectOne(id));
    }

    @Override
    public User currentUserInfo() {
        return SecurityUtil.getUserInfo(User.class);
    }

    @Override
    public User selectByUsername(String username) {
        return makeUserRet(userDao.selectByUsername(username));
    }

    @Override
    public User selectByMobile(String mobile) {
        return makeUserRet(userDao.selectByMobile(mobile));
    }

    private User makeUserRet(User user){
        if(user == null){
            throw new NullPointerException();
        }
        return user;
    }
}
