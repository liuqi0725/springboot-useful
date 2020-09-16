package com.liuqi.springbootfreemarkerpjax.module.user;

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
public interface UserService<T> {

    List<T> userList();

    T userInfo(Integer id);

}
