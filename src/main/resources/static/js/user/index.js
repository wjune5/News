$(function () {
    var newsUrl = '/news/newsinfo';
    var serverUrl = 'http://localhost:8080';
    var flag = true;
    initNewsImg();
    initNews(1);

    function initNewsImg() {
        $.getJSON(newsUrl, function (data) {
            // console.log(data);
            if (data.status == 200) {
                var newsList = data.data.rows;

                // console.log(newsList);
                var lhtml = "";
                var bhtml = "";

                for (var i = 0; i < 4; i++) {
                    if (newsList[i].newsImgList.length > 0) {
                        lhtml += "<div class=\"swiper-slide\"><a href=\"/news/details?newsId=" + newsList[i].newsId + "\">" +
                            "<img src='" + serverUrl + newsList[i].newsImgList[0].imgAddress + "'></a><div class=\"layer-mask\"></div></div>";
                    }
                    bhtml += "<div class=\"marquee1\"><a class=\"breaking\" href=\"/news/details?newsId=" + newsList[i].newsId +"\">" + newsList[i].title + "</a></div>";
                }
                $("#swiper-wrapper").html(lhtml);
                $("#breaking-news-wrap").html(bhtml);

                swiperFunc();

            } else {
                alert("加载失败~");
            }
        })
    }

    function initNews(page) {
        $.getJSON(newsUrl, {page: page}, function (data) {
            // console.log(data);
            if (data.status == 200) {
                var newsList = data.data.rows;
                var totalRecords = data.data.records;
                // console.log(totalRecords);data.data.records

                var lhtml = "";

                for (var i = 0; i < newsList.length; i++) {
                    if (newsList[i].newsImgList.length > 0) {
                        lhtml += "<li><div class=\"news-grid-left1\"><img src='" + serverUrl + newsList[i].newsImgList[0].imgAddress + "' alt=\" \" class=\"img-responsive\" /></div>" +
                            "<div class=\"news-grid-right1\"><h4><a href=\"/news/details?newsId=" + newsList[i].newsId + "\">" + newsList[i].title + "</a></h4>" +
                            "<h5>By <a href=\"#\">" + newsList[i].nickname + "</a> <label>|</label> <i>" + new Date(newsList[i].lastEditTime).Format("yyyy-MM-dd hh:mm") +
                            "</i></h5><div class='news-content'>" + newsList[i].content + "</div></div><div class=\"clearfix\"></div></li>";
                    }
                }
                $("#latest-news").html(lhtml);
                wordlimit("news-content", 100);


            } else {
                alert("加载失败~");
            }
            if (flag) {
                jumpPage(totalRecords);
            }
            flag = false;

        })
    }

    function swiperFunc() {

        var swiper = new Swiper('.swiper-container', {
            autoplay: false,
            speed: 2000,
            autoplayDisableOnInteraction: false,
            loop: true,
            centeredSlides: true,
            slidesPerView: 2,
            pagination: '.swiper-pagination',
            paginationClickable: true,
            prevButton: '.swiper-button-prev',
            nextButton: '.swiper-button-next',
            onInit: function (swiper) {
                swiper.slides[2].className = "swiper-slide swiper-slide-active";
            },
            breakpoints: {
                668: {
                    slidesPerView: 1
                }
            }
        });
        $('.marquee').marquee({ pauseOnHover: true });
    }

    function jumpPage(totalRecords) {

        layui.use(['laypage'], function() {
            var laypage = layui.laypage;
            //完整功能
            laypage.render({
                elem: 'demo7'
                , count: totalRecords
                , layout: ['prev', 'page', 'next', 'refresh', 'skip']
                , jump: function (obj) {
                    initNews(obj.curr);
                }
            });
        })
    }
});