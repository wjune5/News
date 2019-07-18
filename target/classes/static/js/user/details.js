$(function () {
    var newsId = getQueryString("newsId");
    var url = '/news/details/' + newsId;

    $.getJSON(url, function (data) {
        // console.log(data.data);
        if (data.status == 200) {
            $("#title").text(data.data.news.title);
            $("#author").html(data.data.news.nickname);
            $("#content").html(data.data.news.content);
            $("#fileName").val(data.data.fileAddress);
        } else {
            alert(data.msg);
        }
    });

    downloadFile = function () {
        var fileName = $("#fileName").val();
        console.log(fileName);
        window.location.href = "/news/" + fileName;
        return false;
    }
});