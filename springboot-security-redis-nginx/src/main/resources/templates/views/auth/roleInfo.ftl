<#include baseTemplate>
<@htmlHead>
    <!-- 添加 style -->
</@htmlHead>


<@htmlBody>
    Role Ids :
    <#list roles as id>
        ${id} ,
    </#list>
</@htmlBody>


<@htmlBottom>
    <!-- 添加 js -->
</@htmlBottom>