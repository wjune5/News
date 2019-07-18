$(function () {
    layui.use(['form', 'layedit', 'upload'], function () {
        var form = layui.form
            , layer = layui.layer
            , layedit = layui.layedit,
            upload = layui.upload;

        var uid = $('#userId').val();
        var newsId = getQueryString("newsId");

        layedit.set({
            uploadImage: {
                url: '/news/news/img/' + uid,
                type: 'post'
            }
        });

        //创建一个编辑器
        var editIndex = layedit.build('content', {
            height: 300 //设置编辑器高度
        }); //建立编辑器


        //自定义验证规则
        form.verify({
            title: function (value) {
                if (value.length < 1) {
                    return '标题至少得1个字符~';
                }
                if (value.length > 60) {
                    return '标题至多60个字符~';
                }
            }
            , content: function (value) {
                if (value.length > 8000) {
                    return '内容不能超过8000字~';
                }
                layedit.sync(editIndex);
            }
        });


        var isUpdate = newsId ? true : false;

        if (isUpdate) {
            // alert(newsId);
            var newsUrl = '/news/details/' + newsId;
            $.getJSON(newsUrl, function (data) {
                // console.log(data);
                if (data.status == 200) {
                    $("#title").val(data.data.news.title);
                    $("#introduction").val(data.data.news.introduction);
                    // $("#content").val(data.data.content);
                    layedit.setContent(editIndex, data.data.news.content, false);
                } else {
                    layer.msg(data.msg, {icon: 0});
                }
            })
        } else {
            insert();
            uploadFile();
        }

        //监听提交
        form.on('submit(btn)', function (data) {

            var news = {};
            news.userId = $('#userId').val();  // session中
            news.title = $('#title').val();
            news.content = layedit.getContent(editIndex);

            news.nickname = $('#nickname').val();
            news.introduction = $('#introduction').val();
            var newsJson = JSON.stringify(news);
            var imgAddress = $("#multiple_show_img").val();
            var fileAddress =  $("#show_file").val();
            // console.log(imgAddress);

            $.ajax({
                url: isUpdate ? ('/news/newsinfo/' + newsId) : '/news/news/',
                type: 'post',
                data: {newsJson: newsJson, imgAddress: imgAddress, fileAddress: fileAddress},
                success: function (data) {
                    if (data.status == 200) {
                        if (isUpdate) {
                            layer.alert("修改成功", {
                                    icon: 6
                                },
                                function () {
                                    //关闭当前frame
                                    xadmin.close();

                                    // 可以对父窗口进行刷新
                                    xadmin.father_reload();
                                }
                            );
                        } else {
                            layer.msg('成功', {icon: 1});
                            window.location.reload();
                        }
                    } else {
                        layer.msg(data.msg, {icon: 2});
                    }

                    // xadmin.reload(true);
                },
                error: function () {
                    layer.msg("出现错误~", {icon: 2});
                }
            });
            // return false;
        });

        function uploadFile() {

            //文件示例
            var fileInfo = $('#fileInfo')
                ,uploadListIns = upload.render({
                elem: '#file'
                , url: '/news/news/file/' + uid
                , accept: 'file'
                , size: 10240
                , auto: false
                , bindAction: '#uploadFile'
                ,choose: function(obj){
                    var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                    //读取本地文件
                    obj.preview(function(index, file, result){
                        var tr = $(['<tr id="upload-'+ index +'">'
                            ,'<td>'+ file.name +'</td>'
                            ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
                            ,'<td>等待上传</td>'
                            ,'<td>'
                            ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                            ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                            ,'</td>'
                            ,'</tr>'].join(''));

                        //单个重传
                        tr.find('.demo-reload').on('click', function(){
                            obj.upload(index, file);
                        });

                        //删除
                        tr.find('.demo-delete').on('click', function(){
                            delete files[index]; //删除对应的文件
                            tr.remove();
                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                        });

                        fileInfo.append(tr);
                    });
                }
                ,done: function(res, index, upload){
                    // console.log(res);
                    if(res.status == 200){ //上传成功
                        var tr = fileInfo.find('tr#upload-'+ index)
                            ,tds = tr.children();
                        tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                        tds.eq(3).html(''); //清空操作
                        $("#show_file").val(res.data);

                        return delete this.files[index]; //删除文件队列已经上传成功的文件
                    }
                    this.error(index, upload);
                }
                ,error: function(index, upload){
                    var tr = fileInfo.find('tr#upload-'+ index)
                        ,tds = tr.children();
                    tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                    tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
                }
            });

        }

        function insert() {
            var files = [];
            //多图片上传
            var uploadList = upload.render({
                elem: '#img'
                , url: '/news/news/img/' + uid
                , multiple: true
                , accept: 'images'
                , size: 10240
                , auto: false
                , bindAction: '#uploadImg'
                , choose: function (obj) {
                    files = obj.pushFile(); //将每次选择的文件追加到文件队列

                    //图像预览，如果是多文件，会逐个添加。(不支持ie8/9)
                    obj.preview(function (index, file, result) {
                        var imgBox = document.createElement("div");//预览图、删除预览图按钮容器
                        var imgDelete = document.createElement("div");//删除预览图按钮
                        var imgobj = new Image(); //创建新img对象

                        imgBox.style.float = 'left';
                        imgBox.style.position = 'relative';

                        imgDelete.setAttribute('class', "feedback-delete-btn layui-icon layui-icon-close-fill");
                        imgDelete.setAttribute('title', '删除');

                        imgobj.src = result; //指定数据源
                        imgobj['layer-src'] = result;
                        imgobj.alt = file.name;
                        imgobj.style = 'width: 92px; height: 92px; margin: 0 10px 10px 0;';
                        imgobj['layer-index'] = index;

                        imgBox.appendChild(imgobj);
                        imgBox.appendChild(imgDelete);

                        imgDelete.onclick = function () { // 为预览图的删除按钮绑定删除事件
                            delete files[index]; //删除对应的文件
                            document.getElementById("previewImg").removeChild(imgBox);//从预览区域移除
                            uploadList.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                        };

                        document.getElementById("previewImg").appendChild(imgBox); //添加到预览区域
                    })
                }
                , done: function (res) {
                    //上传完毕
                    if (res.code == 0) {
                        // console.log($("#multiple_show_img").val());
                        var value = $("#multiple_show_img").val();


                        $("#multiple_show_img").val(value + "," + res.data.src);


                        layer.msg('上传图片成功', {icon: 1});
                    } else {
                        layer.msg(res.msg, {icon: 0});
                    }

                }
            });

        }
    });
});