package com.liuqi.springbootsecurityredisnginx.module;

import com.liuqi.springbootsecurityredisnginx.module.user.User;
import com.liuqi.springbootsecurityredisnginx.security.SecurityUtil;
import com.liuqi.springbootsecurityredisnginx.utils.WebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;


/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 3:56 下午 2020/9/18
 */
@Controller
public class MainController {

    /**
     * 欢迎页
     * @return 字符串
     */
    @RequestMapping("/")
    public ModelAndView index(ModelAndView mv) {
        return checkLogin(mv , true);
    }

    /**
     * 登陆页响应
     * @param mv 视图对象
     * @return 视图
     */
    @RequestMapping(value = "/login" , method=RequestMethod.GET)
    public ModelAndView loginPage(ModelAndView mv) {
        return checkLogin(mv,false);
    }

    /**
     * 首页
     * @param mv 视图对象
     * @return 视图
     */
    @GetMapping(value = "/dashboard")
    public ModelAndView dashboard (ModelAndView mv){
        mv.setViewName("dashboard");
        mv.addObject("webSessionId",WebUtil.getSessionId());
        mv.addObject("SecuritySessionId",SecurityUtil.getUserSessionId());
        mv.addObject("loginTime",WebUtil.getSessionAttribute("USER_LOGIN_TIME_"+SecurityUtil.getUserInfo(User.class).getId()));
        System.out.println(SecurityUtil.getUserInfo(User.class).getUsername() + "访问进来了。");
        return mv ;
    }

    private ModelAndView checkLogin(ModelAndView mv , boolean redirect){
        if(SecurityUtil.isLogin()){
            mv.setViewName("redirect:/dashboard");
        }else{
            if(redirect){
                mv.setViewName("redirect:/login");
            }else{
                mv.setViewName("login");
            }
        }
        return mv;
    }

}
