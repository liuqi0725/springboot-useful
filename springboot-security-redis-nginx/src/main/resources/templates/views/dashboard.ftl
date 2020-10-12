<#include baseTemplate>
<@htmlHead>
    <!-- 添加 style -->
</@htmlHead>


<@htmlBody>
    <h3>
    Springboot Nginx + Spring-Security + Spring-Session + Redis Demo 。<br>
    </h3>

    <h5>
        webSessionId: ${webSessionId!""}
    </h5>
    <h5>
        SecuritySessionId: ${SecuritySessionId!""}
    </h5>
    <h5>
        loginTime: ${loginTime?string("yyyy-MM-dd HH:mm:ss")}
    </h5>
</@htmlBody>


<@htmlBottom>
    <!-- 添加 js -->
</@htmlBottom>