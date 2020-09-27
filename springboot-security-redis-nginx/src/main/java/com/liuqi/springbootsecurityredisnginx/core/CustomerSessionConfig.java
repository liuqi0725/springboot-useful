package com.liuqi.springbootsecurityredisnginx.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
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

//    @Value("${jetcache.remote.default.host}")
//    private String redisHost;
//
//    @Value("${jetcache.remote.default.port}")
//    private int redisPort;
//
//    @Value("${jetcache.remote.default.password}")
//    private String redisPwd;

//    @Bean
//    public JedisConnectionFactory connectionFactory() {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(redisHost);
//        redisStandaloneConfiguration.setDatabase(0);
////        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPwd));
//        redisStandaloneConfiguration.setPort(redisPort);
//
//        return new JedisConnectionFactory(redisStandaloneConfiguration);
//    }

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }
}
