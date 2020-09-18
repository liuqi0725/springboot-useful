<#include baseTemplate>
<@htmlHead>

</@htmlHead>


<@htmlBody>

    <#if user??>
        <form>
            <div class="form-group">
                <label for="exampleInputEmail1">Username</label>
                <input type="text" class="form-control" value="${user.username}" name="username" placeholder="username">
            </div>
            <div class="form-group">
                <label for="exampleInputEmail1">Nickname</label>
                <input type="text" class="form-control" value="${user.nickname}" name="nickname" placeholder="nickname">
            </div>
            <div class="form-group">
                <label for="exampleInputEmail1">Mobile</label>
                <input type="text" class="form-control" value="${user.mobile}" name="mobile" placeholder="mobile">
            </div>
            <div class="form-group">
                <label for="exampleInputEmail1">Nickname</label>
                <input type="email" class="form-control" value="${user.email}" name="email" placeholder="email">
            </div>
            <button type="submit" class="btn btn-default">修改</button>
        </form>
    <#else >
        没有该用户
    </#if>
</@htmlBody>


<@htmlBottom>
    <!-- 添加 js -->
</@htmlBottom>