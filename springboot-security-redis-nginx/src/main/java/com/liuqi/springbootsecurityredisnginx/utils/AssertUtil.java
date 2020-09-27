package com.liuqi.springbootsecurityredisnginx.utils;

/**
 * 类说明 <br>
 *    断言工具
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:02 PM 2019/8/12
 */
public class AssertUtil {

    /**
     * 不能为空
     * @param str 字符串
     * @return 不为空返回对象，为空抛出 AppException
     */
    public static String notNull(String str){
        return notNull(str, "String类型 参数为空。");
    }

    /**
     * 不能为空
     * @param var 字符串
     * @param errorMsg 错误信息
     * @return 不为空返回对象，为空抛出 AppException
     */
    public static String notNull(String var, String errorMsg){
        if(var == null || var.equals("")){
            throw new NullPointerException(errorMsg);
        }
        return var;
    }

    /**
     * 不能为空
     * @param var integer 对象
     * @return 不为空返回对象，为空抛出 AppException
     */
    public static Integer notNull(Integer var){
        return notNull(var, "Integer类型 参数为空。");
    }

    /**
     * 不能为空
     * @param var  integer 对象
     * @param errorMsg 错误信息
     * @return 不为空返回对象，为空抛出 AppException
     */
    public static Integer notNull(Integer var, String errorMsg){
        if(var == null){
            throw new NullPointerException(errorMsg);
        }

        return var;
    }

    /**
     * 不能为空
     * @param obj 对象
     * @param <T> 任意类型
     * @return 不为空返回对象，为空抛出 AppException
     */
    public static <T> T notNull(T obj){
        return notNull(obj," 对象不能为空。");
    }

    /**
     * 不能为空
     * @param obj 对象
     * @param errorMsg 错误信息
     * @param <T> 任意类型
     * @return 不为空返回对象，为空抛出 AppException
     */
    public static <T> T notNull(T obj , String errorMsg){
        if (obj == null)
            throw new NullPointerException(errorMsg);
        return obj;
    }



}
