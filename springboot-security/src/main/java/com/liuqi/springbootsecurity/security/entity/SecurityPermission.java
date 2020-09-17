package com.liuqi.springbootsecurity.security.entity;

import java.io.Serializable;

/**
 * 类说明 <br>
 * <p>
 *
 *
 *
 *
 *
 * @author : alexliu
 * @version v1.0 , Create at 9:28 PM 2020/3/4
 */
public abstract class SecurityPermission implements Serializable{

    private static final long serialVersionUID = -3686110239939092731L;

    /**
     * 唯一的权限 key
     */
    private String unKey;

    /**
     * 权限提交 url，有 url 属性的权限会加入安全验证
     */
    private String url;

    public String getUnKey() {
        return unKey;
    }

    public void setUnKey(String unKey) {
        this.unKey = unKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
