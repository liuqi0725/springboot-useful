<#include baseTemplate>
<@htmlHead>
    <!-- 添加 style -->
</@htmlHead>


<@htmlBody>
    My Permission :
    <table class="table table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>name</th>
            <th>unKey</th>
            <th>pid</th>
            <th>nodepath</th>
            <th>url</th>
            <th>updateAt</th>
        </tr>
        </thead>
        <tbody>
        <#list permissionList as permission>
            <tr>
                <th scope="row">${permission.id}</th>
                <td>${permission.name}</td>
                <td>${permission.unKey}</td>
                <td>${permission.pid}</td>
                <td>${permission.nodepath}</td>
                <td>${permission.url}</td>
                <td>${(permission.updateAt?string("yyyy-MM-dd HH:mm:ss"))!}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@htmlBody>


<@htmlBottom>
    <!-- 添加 js -->
</@htmlBottom>