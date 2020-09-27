package com.liuqi.springbootsecurityredisnginx.security.authentication;

import com.liuqi.springbootsecurityredisnginx.security.authentication.token.UserLoginMobileAuthenticationToken;
import com.liuqi.springbootsecurityredisnginx.security.authentication.token.UserLoginNormalAuthenticationToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类说明 <br>
 *  自定义样式认证登陆的过滤器
 *
 * @author : alexliu
 * @version v1.0 , Create at 1:59 PM 2020/3/10
 */
@Log4j2
public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String SPRING_SECURITY_FORM_LOGIN_TYPE_KEY = "login_type";

    private static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    private static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

    private static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";
    private static final String SPRING_SECURITY_FORM_MOBILE_CODE_KEY = "mobile_code";

    private String loginTypeParameter = SPRING_SECURITY_FORM_LOGIN_TYPE_KEY;

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private String mobileCodeParameter = SPRING_SECURITY_FORM_MOBILE_CODE_KEY;

    private boolean postOnly = true;

    public UserAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login",HttpMethod.POST.name()));
        SimpleUrlAuthenticationFailureHandler failedHandler = (SimpleUrlAuthenticationFailureHandler)getFailureHandler();
        failedHandler.setDefaultFailureUrl("/login?error");

        SimpleUrlAuthenticationSuccessHandler successHandler = (SimpleUrlAuthenticationSuccessHandler)getSuccessHandler();
        successHandler.setDefaultTargetUrl("/dashboard");

        log.info("UserAuthenticationFilter loading ...");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        // 只允许 /login 为 post 的的请求进入
        if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // 获取登录类型
        String loginType = obtainLoginType(request);

        if(loginType.equalsIgnoreCase("mobile")){
            String mobile = trimStr(obtainMobile(request));
            String mobileCode = trimStr(obtainMobileCode(request));

            // AuthenticationProvider里面有个supports方法,主要用来验证指定的token是否符合.
            // 可以通过指定不同类型的 token 来决定使用哪个 Provider.
            UserLoginMobileAuthenticationToken authRequest = new UserLoginMobileAuthenticationToken(mobile,mobileCode);
            // 设置身份验证详情
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);

        }else{
            // 普通登陆
            String username = trimStr(obtainUsername(request));
            String password = trimStr(obtainPassword(request));

            // AuthenticationProvider 里面有个supports方法,主要用来验证指定的token是否符合.
            // 可以通过指定不同类型的 token 来决定使用哪个 Provider.
            UserLoginNormalAuthenticationToken authRequest = new UserLoginNormalAuthenticationToken(username,password);
            // 设置身份验证详情
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }

    }


    /**
     * Enables subclasses to override the composition of the username, such as by including additional values
     * and a separator.
     *
     * @param request so that request attributes can be retrieved
     *
     * @return the username that will be presented in the <code>Authentication</code> request token to the
     *         <code>AuthenticationManager</code>
     */
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(usernameParameter);
    }

    /**
     * Enables subclasses to override the composition of the password, such as by including additional values
     * and a separator.<p>This might be used for example if a postcode/zipcode was required in addition to the
     * password. A delimiter such as a pipe (|) should be used to separate the password and extended value(s). The
     * <code>AuthenticationDao</code> will need to generate the expected password in a corresponding manner.</p>
     *
     * @param request so that request attributes can be retrieved
     *
     * @return the password that will be presented in the <code>Authentication</code> request token to the
     *         <code>AuthenticationManager</code>
     */
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(passwordParameter);
    }

    protected String obtainLoginType(HttpServletRequest request) {
        return request.getParameter(loginTypeParameter);
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    protected String obtainMobileCode(HttpServletRequest request) {
        return request.getParameter(mobileCodeParameter);
    }

    private String trimStr(Object o){
        if(o == null){
            return "";
        }
        return ((String)o).trim();
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication request's details
     * property.
     *
     * @param request that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details set
     */
    protected <T extends AbstractAuthenticationToken> void setDetails(HttpServletRequest request, AbstractAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
