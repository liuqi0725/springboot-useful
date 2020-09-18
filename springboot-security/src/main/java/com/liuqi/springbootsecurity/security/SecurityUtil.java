package com.liuqi.springbootsecurity.security;

import com.liuqi.springbootsecurity.security.entity.SecurityUser;
import com.liuqi.springbootsecurity.security.error.PasswordError;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * 类说明 <br>
 * spring security 的一些工具
 *
 * @author : alexliu
 * @version v1.0 , Create at 3:59 PM 2020/3/2
 */
public class SecurityUtil {

    /**
     * 用 spring-security BCryptPasswordEncoder 加密密码
     * @param var1 密码
     * @return 加密后的密码
     */
    public static String encode(String var1){
        BCryptPasswordEncoder bcr = new BCryptPasswordEncoder();
        return bcr.encode(var1);
    }

    /**
     * 用 spring-security BCryptPasswordEncoder 比较密码
     * @param var1 新密码 (明文)
     * @param var2 原密码 (密文)
     * @return 比较结果
     */
    public static boolean matches(String var1 , String var2){
        BCryptPasswordEncoder bcr = new BCryptPasswordEncoder();
        return bcr.matches(var1, var2);
    }

    /**
     * 是否登陆
     * @return 是否登陆
     */
    public static boolean isLogin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return !(auth instanceof AnonymousAuthenticationToken);
    }

    /**
     * 获取用户信息
     * @return User 对象 extends SecurityUser
     */
    public static Object getUserInfo(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取用户信息
     * @param classz 用户信息类 必须继承自 {@link SecurityUser}
     * @param <T> 必须继承自 {@link SecurityUser}
     * @return SecurityUser {@link SecurityUser}
     */
    @SuppressWarnings("Unchecked")
    public static <T extends SecurityUser> T getUserInfo(Class<T> classz){
        return (T) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取用户 sessionID
     * @return 用户 sessionID
     */
    public static String getUserSessionId(){
        return ((WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getSessionId();
    }

    /**
     * 修改用户普通信息，从新获取用户令牌 <br>
     * username 、 password 作为用户验证条件，不能此函数中修改.<br>
     * <b>修改 username</b> 使用 <br>
     * <b>修改 password</b> 使用 {@link #updateUserPassword}<br>
     * @param user 继承自 {@link SecurityUser}
     * @param <T> 继承自 {@link SecurityUser}
     */
    public static <T extends SecurityUser> void updateUserGeneralInfo(T user){

        // 修改用户信息，需要重新生成令牌

        // 获取当前用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 获取当前认证的用户
        SecurityUser sessionUser = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 重新 new 一个【通过验证】的token（传入 UserDetails , Authorities）
        // UserLoginNormalAuthenticationToken authenticationToken = new UserLoginNormalAuthenticationToken(user, user.getAuthorities());
        // 沿用上个令牌的 session 数据
        // authenticationToken.setDetails(authentication.getDetails());
    }


    /**
     * 修改用户密码 <br>
     * @param oldPassword 旧密码（密文）
     * @param newPassowrd 新密码（密文）
     */
    public static void updateUserPassword(String oldPassword , String newPassowrd){

        // 修改用户信息，需要重新生成令牌

        // 获取当前用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 获取当前认证的用户
        SecurityUser sessionUser = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 验证密码
        if(!oldPassword.equals(sessionUser.getPassword())){
            throw new PasswordError(sessionUser.getUsername(), "修改密码，原始密码 与Token中的密码 不符! ");
        }
        // 修改密码
        sessionUser.setPassword(newPassowrd);
        // 重新 new 一个【通过验证】的token（传入 UserDetails , Authorities）
        // UserLoginNormalAuthenticationToken authenticationToken = new UserLoginNormalAuthenticationToken(user, user.getAuthorities());
        // 沿用上个令牌的 session 数据
        // authenticationToken.setDetails(authentication.getDetails());
    }



    /**
     * 用户是否有权限
     * @param permissionKey 权限 key
     * @return 是否有权限
     */
    public static boolean hasPermission(String permissionKey){

        for (GrantedAuthority authority :SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if(authority.getAuthority().equals(permissionKey)){
                return true;
            }
        };

        return false;
    }
}
