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
 * @version v1.0 , Create at 11:16 上午 2020/9/18
 */
@Data
public class Role implements Serializable {
    private static final long serialVersionUID = -5797849503818694952L;

    private Integer id;

    private String name;

    private String unKey;

    private Integer status;

    private Integer edit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
