$(function () {
    layui.use(['layer'],
        function () {
            $ = layui.jquery;
            var layer = layui.layer;
            var userId = $("#userId").val();
            var newsUrl = "/news/news/" + userId;

            var flag = true;
            initList(1);

            function initList(page) {
                $.getJSON(newsUrl, {page: page}, function (data) {
                    if (data.status == 200) {
                        var lhtml = "";
                        var newsList = data.data.rows;
                        var totalRecords = data.data.records;

                        for (var i = 0; i < newsList.length; i++) {

                            lhtml += "<div class=\"layui-col-md12\"><div class=\"layui-card\"><div class=\"layui-card-header\">" +
                                "<div class=\"layui-row\"><div class=\"layui-col-md7\">" + newsList[i].title + "</div><div class=\"layui-col-md3\">" +
                                new Date(newsList[i].lastEditTime).Format("yyyy-MM-dd hh:mm") + "</div><div class=\"layui-col-md2\"><div class=\"layui-btn-group\">" +
                                "<button type=\"button\" class=\"layui-btn layui-btn-sm\" onclick=\"updateNews('" + newsList[i].newsId + "')\"><i class=\"layui-icon\">&#xe642;</i></button>" +
                                "<button type=\"button\" class=\"layui-btn layui-btn-sm\"  onclick=\"deleteNews('" + newsList[i].newsId + "')\"><i class=\"layui-icon\">&#xe640;</i></button>" +
                                "</div></div></div></div>" +
                                "<div class=\"layui-card-body\">" + newsList[i].content + "</div></div></div></div>"
                        }

                        $("#news-wrap").html(lhtml);
                        wordlimit("layui-card-body", 100);
                    } else {
                        layer.alert("删除成功", {
                            icon: 1
                        })
                    }
                    if (flag) {
                        jumpPage(totalRecords);
                    }
                    flag = false;
                })
            }


            deleteNews = function (newsId) {
                $.getJSON('/news/delete/' + newsId, function (res) {
                    if (res.status == 200) {
                        layer.alert("删除成功", {
                                icon: 6
                            },
                            function () {
                                window.location.reload();

                            }
                        );
                    } else {
                        layer.alert(res.msg, { icon: 6 });
                    }
                })
            };


            updateNews = function (newsId) {

                layer.open({
                    type: 2  // iframe
                    ,
                    title: "修改新闻" //不显示标题栏
                    ,
                    closeBtn: false
                    ,
                    area: ['800px', '700px']
                    ,
                    shade: 0.5
                    ,
                    id: 'LAY_layuipro' //设定一个id，防止重复弹出
                    ,
                    btn: ['退出']
                    ,
                    content: "/news/inews?newsId=" + newsId,
                    success: function (layero, index) {
                        // viewDetails(taskId);
                    }
                });
            };

            function jumpPage(totalRecords) {

                layui.use(['laypage'], function () {
                    var laypage = layui.laypage;
                    //完整功能
                    laypage.render({
                        elem: 'demo7'
                        , count: totalRecords
                        , layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip']
                        , jump: function (obj) {
                            initList(obj.curr);
                        }
                    });
                })
            }

        });
});