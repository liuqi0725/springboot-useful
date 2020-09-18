package com.liuqi.springbootsecurity.module.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liuqi.springbootsecurity.security.entity.SecurityUser;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 10:49 上午 2020/9/15
 */
@Data
public class User extends SecurityUser implements Serializable {

    private static final long serialVersionUID = -4226376261800658360L;

    private Integer id;

    private String nickname;

    private String mobile;

    private String email;

    private String headUrl;

    private int gender;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_at;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date update_at;

}
