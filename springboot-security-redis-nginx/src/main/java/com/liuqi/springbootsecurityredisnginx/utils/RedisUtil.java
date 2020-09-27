package com.liuqi.springbootsecurityredisnginx.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 11:29 上午 2020/9/27
 */
@Component
public final class RedisUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public boolean expire(String key, long time){
        try {
            if(time > 0){
                redisTemplate.expire(key,time, TimeUnit.SECONDS);
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    public boolean hasKey(String key){
        try{
            return redisTemplate.hasKey(key);
        }catch (Exception e){
            return false;
        }
    }

    public void delete(String... keys){
        if(keys != null && keys.length >0){
            redisTemplate.delete(CollectionUtils.arrayToList(keys));
        }
    }

    public Object get(String key){
        try {
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            return null;
        }
    }

    public <T> T get(String key , Class<T> classz){
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if(o.getClass() == classz.getClass()){
                return (T)o;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    public boolean set(String key , Object val){
        try {
            redisTemplate.opsForValue().set(key,val);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean set(String key, Object val , long time){
        try {
            if(time > 0){
                redisTemplate.opsForValue().set(key,val , time, TimeUnit.SECONDS);
            }else{
                return set(key,val);
            }
        }catch (Exception e){
            set(key,val);
            return false;
        }
        return true;
    }

}
