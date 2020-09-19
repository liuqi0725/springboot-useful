<div class="col-md-2 leftM">
    <div class="list-group" id="ADMIN_SIDE_MENU">

        <#assign haschild=false>
        <#assign childHtmlOpen=false>

        <ul>

            <#list AppFreemarkerCommonUtil.getMenus() as item>

                <#-- pid=0 一级菜单 -->
                <#if item.pid == 0>

                    <#-- 如果之前有子菜单，关闭子菜单,并把haschild 改为 false -->
                    <#if haschild == true>
                        <#assign childHtmlOpen = false>
                        <#assign haschild = false>
                        <#-- 关闭之前 pid 为 0 的 li -->
                        </dl>
                    </#if>

                    <#-- 关闭之前 pid 为 0 的 li -->
                    </li>
                    <#-- 添加一级菜单 -->
                    <li>
                        <a href="javascript:void(0);" id="${item.unKey}" onclick="admin.chooseMenu('${item.unKey}')" r="${item.url!''}" class="list-group-item">${item.name}</a>

                <#else >
                    <#-- 二级菜单 -->
                    <#if childHtmlOpen == false>
                        <dl>
                        <#assign childHtmlOpen = true>
                        <#assign haschild = true>
                    </#if>

                        <dd class="left-menu">
                            <a href="javascript:void(0);" id="${item.unKey}" onclick="admin.chooseMenu('${item.unKey}')" r="${item.url!''}" class="list-group-item">${item.name}</a>
                        </dd>
                </#if>


            </#list>

        </ul>



<#--        <a href="javascript:void(0);" id="MENU_1" onclick="admin.chooseMenu('MENU_1')" r="/dashboard" class="list-group-item active">-->
<#--            首页-->
<#--        </a>-->
<#--        <a href="javascript:void(0);" id="MENU_2" onclick="admin.chooseMenu('MENU_2')" r="/user/list" class="list-group-item">用户列表</a>-->
<#--        <a href="javascript:void(0);" id="MENU_3" onclick="admin.chooseMenu('MENU_3')" r="/user/info/1" class="list-group-item">用户信息</a>-->
    </div>
</div>