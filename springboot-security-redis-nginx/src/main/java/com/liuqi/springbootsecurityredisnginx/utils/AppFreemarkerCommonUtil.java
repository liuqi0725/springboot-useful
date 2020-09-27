package com.liuqi.springbootsecurityredisnginx.utils;

import com.liuqi.springbootsecurityredisnginx.constant.WebCommonAttribute;
import com.liuqi.springbootsecurityredisnginx.core.anno.FreeMarkerStatic;
import com.liuqi.springbootsecurityredisnginx.module.auth.entity.Menu;

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
    public static List<Menu> getMenus(){
        return (List<Menu>)WebUtil.getSessionAttribute(WebCommonAttribute.CURRENT_USER_MENUS);
    }
}
