<#include baseTemplate>
<@htmlHead>

</@htmlHead>


<@htmlBody>

    <table class="table table-hover">
        <thead>
        <tr>
            <th>#</th>
            <th>UserName</th>
            <th>NickName</th>
            <th>Mobile</th>
            <th>Email</th>
            <th>Operation</th>
        </tr>
        </thead>
        <tbody>
        <#list userlist as user>
            <tr>
                <th scope="row">${user.id}</th>
                <td>${user.username}</td>
                <td>${user.nickname}</td>
                <td>${user.mobile}</td>
                <td>${user.email}</td>
                <td>无</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@htmlBody>


<@htmlBottom>
    <!-- 添加 js -->
</@htmlBottom>