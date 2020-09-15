package com.liuqi.springbootflyway.app.module.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:46 上午 2020/9/15
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/info/{id}")
    @ResponseBody
    public Object getUserInfo(@PathVariable("id")Integer id){

        Map<String,Object> data = new HashMap<>();
        try {
            data.put("success",1);
            data.put("user", userService.selectUser(id));
        }catch (NullPointerException e){
            data.put("success",0);
            data.put("msg", e.getMessage());
        }
        return data;
    }
}
