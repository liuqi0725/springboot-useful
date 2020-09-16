(function(window,$){

    let admin = {};

    /**
     * 第一个菜单为默认菜单选中
     * @type {*|null|undefined|jQuery}
     */
    let DEFAULT_MENU_ID = $($("#ADMIN_SIDE_MENU a")[0]).attr("id");

    admin.menuInit = function(){
        // cookie 中获取菜单
        let menuId = cookie.get("USER_CHOOSE_MENU")

        if(menuId == null || menuId === "undefined" || menuId === ""){
            // 如果 cookie 中没有，则获取菜单列表第一个作为默认选中 menu
            console.log(menuId)
            menuId = DEFAULT_MENU_ID;
        }
        // 跳转 url
        admin.chooseMenu(menuId)
    }

    admin.chooseMenu = function(menuId){
        let menuObj = $("#"+menuId);
        let url = $(menuObj).attr("r");
        // 设置到 cookie
        cookie.set("USER_CHOOSE_MENU" , menuId);
        setMenuChooseClass(menuObj);
        admin.pjax_get(url);
    }

    function setMenuChooseClass(menuObj){
        const choose = $(menuObj).hasClass("active");
        if(!choose){
            // 清空其他 css
            const currentChooseMenu = $("#ADMIN_SIDE_MENU").find(".active");
            $(currentChooseMenu).removeClass("active");
            // 添加 css
            $(menuObj).addClass("active");
        }
    }

    admin.pjax_get = function(url){
        $.pjax({url: url, container: "#content_container"  ,type:"GET" , timeout: 650});
    }


    let cookie = {

        /**
         * 设置 cookie 值
         * @param key key name
         * @param val 值
         * @param expireDays 过期时间【单位：天】，默认当前 session ，关闭浏览器或窗口，失效.
         */
        set : function(key , val , expireDays){
            if(expireDays != null && expireDays != "undefined" && expireDays != ""){
                const exp = this.__getExpires(expireDays);
                console.log("set cookie key = ", key, " val=",val , " time=",exp.toUTCString());
                document.cookie = key + "=" + escape(val) + ";expires="+exp.toUTCString() + ";path=/";
            }else{
                document.cookie = key + "=" + escape(val) + ";path=/";
            }

        },

        /**
         * 获取
         * @param key
         * @param defaultVal
         * @returns {string|*}
         */
        get : function(key , defaultVal){

            let name = key + "=";
            let decodedCookie = decodeURIComponent(document.cookie);
            let ca = decodedCookie.split(';');
            for(var i = 0; i <ca.length; i++) {
                let c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return defaultVal;
        },

        __getExpires : function(expireDays){
            const exp = new Date();
            exp.setTime(exp.getTime() + expireDays * 24 * 60 * 60 * 1000);
            return exp;
        }

    };

    window.admin = admin;

})(window,jQuery);