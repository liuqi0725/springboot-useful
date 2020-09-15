package com.liuqi.springbootflyway.app.module.user;
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
public interface UserService<T> {

    T selectUser(Integer id);
}
