package com.liuqi.springbootsecurityredisnginx.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:00 上午 2020/9/27
 */
@Configuration
@EnableRedisHttpSession
public class CustomerSessionConfig {

//    @Bean
//    public LettuceConnectionFactory connectionFactory() {
//        return new LettuceConnectionFactory();
//    }
}
