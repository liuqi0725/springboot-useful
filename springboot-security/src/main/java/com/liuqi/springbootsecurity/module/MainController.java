package com.liuqi.springbootsecurity.module;

import com.liuqi.springbootsecurity.security.SecurityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
