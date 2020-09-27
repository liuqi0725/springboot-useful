<#include baseTemplate>
<@htmlHead>
    <!-- 添加 style -->
</@htmlHead>


<@htmlBody>
    My Menus :
    <table class="table table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>name</th>
            <th>unKey</th>
            <th>pid</th>
            <th>nodepath</th>
            <th>updateAt</th>
        </tr>
        </thead>
        <tbody>
        <#list menuList as menu>
            <tr>
                <th scope="row">${menu.id}</th>
                <td>${menu.name}</td>
                <td>${menu.unKey}</td>
                <td>${menu.pid}</td>
                <td>${menu.nodepath}</td>
                <td>${(menu.updateAt?string("yyyy-MM-dd HH:mm:ss"))!}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@htmlBody>


<@htmlBottom>
    <!-- 添加 js -->
</@htmlBottom>