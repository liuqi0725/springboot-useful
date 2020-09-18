package com.liuqi.springbootsecurity.module.auth.entity;

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
 * @version v1.0 , Create at 11:18 上午 2020/9/18
 */
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 5834573132445702193L;

    private Integer id;

    private String name;

    private String unKey;

    private Integer pid;

    private String nodepath;

    private String url;

    private String icon;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
