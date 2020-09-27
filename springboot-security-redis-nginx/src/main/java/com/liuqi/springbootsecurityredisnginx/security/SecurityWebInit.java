package com.liuqi.springbootsecurityredisnginx.security;


import com.liuqi.springbootsecurityredisnginx.core.CustomerSessionConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import sun.security.krb5.Config;

/**
 * 类说明 <br>
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:13 PM 2020/3/4
 */
@Log4j2
public class SecurityWebInit extends AbstractHttpSessionApplicationInitializer {

    public SecurityWebInit(){
        super(CustomerSessionConfig.class, Config.class);
    }

}
