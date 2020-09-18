package com.liuqi.springbootsecurity.core.anno;

import java.lang.annotation.*;

/**
 * 类说明 <br>
 *     Freemarker 静态类资源加载注解，待该注解的类会以 类名为 key 放入 spring mvc 中
 * <pre>
 *  例如：
 *  {@code @FreeMarkerStatic}
 *  public class TestUtil {
 *      public static String hello(){
 *          return "hello";
 *      }
 *  }
 *
 *  在 freemarker 页面
 *  {@code <div>${TestUtil.hello}</div>}
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:24 AM 2020/3/6
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FreeMarkerStatic {
}
