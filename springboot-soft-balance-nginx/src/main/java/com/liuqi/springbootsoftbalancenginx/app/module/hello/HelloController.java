package com.liuqi.springbootsoftbalancenginx.app.module.hello;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 1:31 下午 2020/9/15
 */
@RestController
public class HelloController {

    @RequestMapping("/hello/{username}")
    @ResponseBody
    public Object sayHello(@PathVariable("username")String username , HttpServletRequest request){

        System.out.println(username + ", 正在访问.");
        return "Hello , "+username;
    }
}
