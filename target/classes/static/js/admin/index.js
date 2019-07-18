$(function () {
    var loginUrl = "/news/user";

    $("#btnLogin").click(function () {
        var username = $("#userName").val();
        var pwd = $("#pwd").val();
        console.log(username);
        console.log(pwd);
        if (username=="") {
            $("#userNameErr").html("用户名不能为空~");
            return;
        }
        if (pwd=="") {
            $("#passwordErr").html("密码不能为空~");
            return;
        }

        var user = {};
        user.userName = username;
        user.password = pwd;
        var userJson = JSON.stringify(user);

        $.ajax({
            url: loginUrl,
            data: {
                userJson: userJson
            },
            dataType: "json",
            success: function (data) {
                // console.log(data);
                if (data.status == 200) {
                    // console.log("登录");
                    window.location.href = "/news/menu";
                } else {
                    alert(data.msg);
                    $("#errMsg").html(data.msg);
                }
            }
        })
    });

});