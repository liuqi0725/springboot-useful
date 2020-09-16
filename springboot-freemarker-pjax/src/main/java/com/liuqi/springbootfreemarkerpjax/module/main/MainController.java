package com.liuqi.springbootfreemarkerpjax.module.main;

import org.springframework.stereotype.Controller;
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
 * @version v1.0 , Create at 5:02 下午 2020/9/15
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public ModelAndView main(ModelAndView mv){
        mv.setViewName("/dashboard");
        return mv;
    }

    @RequestMapping("/dashboard")
    public ModelAndView dashboard(ModelAndView mv){
        mv.setViewName("/dashboard");
        return mv;
    }

}
