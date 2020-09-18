<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="x-ua-compatible" content="ie=edge">

        <title>Spring-Security</title>
    </head>
<body>

    <form id="loginform" method="POST" action="/login">

        <select name="login_type" id="loginTypeSelect">
            <option value="normal">账号密码</option>
            <option value="mobile">手机验证码</option>
        </select>

        <div id="normalLogin">
            <h4>账号有： admin、test1、test2、test3 密码均为 123456 </h4><br>
            UserName: <input type="text" name="username" value="test1" placeholder="test1" />
            Password: <input type="password" name="password" />
        </div>

        <div id="mobileLogin" style="display: none;">
            <h4>手机号有： 13987654321,13987654322,13987654323,13987654324  验证码均为 123456 </h4><br>
            Mobile: <input type="text" name="mobile" value="13987654321" />
            VeriCode: <input type="text" name="mobile_code" value="123456" />
        </div>

        <button type="submit" >Login</button>
    </form>

</body>
    <script type="text/javascript" src="${basePath}/assets/vendor/jquery-1.11.1.min.js"></script>
    <script>
        $(() => {
            $('#loginTypeSelect').change(() => {
                let val = $('#loginTypeSelect').val();
                if(val === "normal"){
                    $("#normalLogin").show();
                    $("#mobileLogin").hide();
                }else if (val === "mobile"){
                    $("#normalLogin").hide();
                    $("#mobileLogin").show();
                }
            });
        });
    </script>
</html>