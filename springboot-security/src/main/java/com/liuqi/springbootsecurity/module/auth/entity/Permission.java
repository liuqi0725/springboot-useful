package com.liuqi.springbootsecurity.module.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liuqi.springbootsecurity.security.entity.SecurityPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class Permission extends SecurityPermission implements Serializable {

    private static final long serialVersionUID = -7860762534066325913L;

    private Integer id;

    private String name;

    private Integer pid;

    private String nodepath;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getNodepath() {
        return nodepath;
    }

    public void setNodepath(String nodepath) {
        this.nodepath = nodepath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
