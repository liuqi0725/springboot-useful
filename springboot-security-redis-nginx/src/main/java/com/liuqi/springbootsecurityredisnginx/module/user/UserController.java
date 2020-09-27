package com.liuqi.springbootsecurityredisnginx.module.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:28 上午 2020/9/18
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService<User> userService;

    @RequestMapping("/list")
    public ModelAndView userList(ModelAndView mv){
        mv.addObject("userlist" , userService.userList());
        mv.setViewName("/user/list");
        return mv;
    }

    @RequestMapping("/info/{id}")
    public ModelAndView userInfo(@PathVariable("id")Integer id, ModelAndView mv){
        try {
            mv.addObject("user" , userService.userInfo(id));
        }catch (NullPointerException e){
            mv.addObject("user" , null);
        }
        mv.setViewName("/user/info");
        return mv;
    }

    @RequestMapping("/my")
    public ModelAndView userInfo(ModelAndView mv){
        mv.addObject("user" , userService.currentUserInfo());
        mv.setViewName("/user/info");
        return mv;
    }
}
