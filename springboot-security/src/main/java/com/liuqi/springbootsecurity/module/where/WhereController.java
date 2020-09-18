package com.liuqi.springbootsecurity.module.where;

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
 * @version v1.0 , Create at 10:22 上午 2020/9/18
 */
@Controller
@RequestMapping("/where")
public class WhereController {

    @RequestMapping("/github")
    public ModelAndView github(ModelAndView mv){
        mv.addObject("name","GitHub");
        mv.addObject("path","https://github.com/liuqi0725/springboot-useful");
        mv.setViewName("/where/sourcePath");
        return mv;
    }

    @RequestMapping("/gitee")
    public ModelAndView gitee(ModelAndView mv){
        mv.addObject("name","Gitee");
        mv.addObject("path","https://gitee.com/alexliu0725/springboot-useful");
        mv.setViewName("/where/sourcePath");
        return mv;
    }
}
