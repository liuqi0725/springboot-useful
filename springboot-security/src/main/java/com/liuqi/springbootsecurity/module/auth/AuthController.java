package com.liuqi.springbootsecurity.module.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 类说明 <br>
 * 权限\角色\菜单\中间表 controller  正式编码中，应该分为多个模块用适配器连接
 *
 * @author : alexliu
 * @version v1.0 , Create at 11:13 上午 2020/9/18
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping("/role-info")
    public ModelAndView roleInfo(ModelAndView mv){
        mv.addObject("roles" , authService.getMyRoles());
        mv.setViewName("/auth/roleInfo");
        return mv;
    }

    @RequestMapping("/menu-list")
    public ModelAndView menuList(ModelAndView mv){
        mv.addObject("menuList" , authService.getMyMenus());
        mv.setViewName("/auth/menuList");
        return mv;
    }

    @RequestMapping("/perm-list")
    public ModelAndView permissionList(ModelAndView mv){
        mv.addObject("permissionList" , authService.getMyPermissions());
        mv.setViewName("/auth/permissionList");
        return mv;
    }

}
