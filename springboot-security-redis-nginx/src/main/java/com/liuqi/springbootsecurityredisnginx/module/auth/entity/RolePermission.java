package com.liuqi.springbootsecurityredisnginx.module.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @version v1.0 , Create at 11:16 上午 2020/9/18
 */
@Data
public class RolePermission implements Serializable {

    private static final long serialVersionUID = -5565917615734630044L;

    private Integer id;

    private Integer rid;

    private Integer pid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
