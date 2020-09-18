package com.liuqi.springbootsecurity.utils;

import com.liuqi.springbootsecurity.constant.WebCommonAttribute;
import com.liuqi.springbootsecurity.core.anno.FreeMarkerStatic;
import com.liuqi.springbootsecurity.module.auth.entity.Menu;

import java.util.List;

/**
 * 类说明 <br>
 * <p>
 * 构造说明 :
 * <pre>
 *
 * </pre>
 *
 * @author : alexliu
 * @version v1.0 , Create at 4:59 下午 2020/9/18
 */
@FreeMarkerStatic
public class AppFreemarkerCommonUtil {

    /**
     * 获取 menus
     * @return menus
     */
    @SuppressWarnings("Unchecked")
    public List<Menu> getMenus(){
        return (List<Menu>)WebUtil.getSessionAttribute(WebCommonAttribute.CURRENT_USER_MENUS);
    }
}
