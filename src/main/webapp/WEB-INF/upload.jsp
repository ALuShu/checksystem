<%--
  User: lushu
  Date: 2019/11/26
  Time: 10:22
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>提交作业</title>
    <link rel="stylesheet" href="../layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">

    <div class="layui-header layui-bg-gray">

        <div class="layui-logo" style="font-size: 20px; color: #393D49">作业查重系统</div>

        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item">
                <a href="javascript:;" style="color: #393D49">
                    <img src="http://t.cn/RCzsdCq" class="layui-nav-img">
                    贤心
                </a>
            </li>
            <li class="layui-nav-item"><a href="" style="color: #393D49">退了</a></li>
        </ul>
    </div>

    <div class="layui-bg-green">
        <!-- 内容主体区域 -->
        <div style="padding: 15px; width: 80%; margin: auto">
            <div class="layui-row layui-col-space5 layui-bg-gray">
                <div class="layui-col-md12">
                    <div class="layui-row grid-demo">

                        <%-- 面包屑导航 --%>
                        <div class="layui-col-md12 layui-form-item" style="padding-left: 20px">
                            <span id="view" class="layui-breadcrumb" lay-filter="breaddemo">
                                <script id="bread" type="text/html">
                                    {{# layui.each(d.bread, function (i, e) { }}
                                    <a href="javascript:;" onclick="breadOn('{{e.name}}');" style="font-size: 20px">{{ e.name }}</a>
                                    <span lay-separator>/</span>
                                    {{# }); }}
                                </script>
                            </span>
                        </div>

                        <%-- 列表 --%>
                        <div class="layui-col-md9" style="background: #1E9FFF; padding-left: 20px">
                            <%-- 数据表格 --%>
                            <table id="test" class="layui-table" lay-filter="test"></table>
                        </div>

                        <%-- 上传区域 --%>
                        <div class="layui-col-md3 layui-row grid-demo">
                            <div class="layui-upload-drag" id="test10">
                                <i class="layui-icon"></i>
                                <p>点击上传，或将文件拖拽到此处</p>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<script src="../layui/layui.js"></script>
<script>
    var url = '${pageContext.request.contextPath}/list';
    var path = [];
    var breadArray = [];
    var j = {"name": "root"};
    breadArray.push(j);
    var breadData = {
        "bread": breadArray
    };
    var getTpl = bread.innerHTML
        , view = document.getElementById('view');


    //layui官方方法
    layui.use(['table', 'tree', 'util', 'element', 'laytpl'], function () {
        var table = layui.table
            , layer = layui.layer
            , element = layui.element
            , laytpl = layui.laytpl;


        //layui数据表格
        table.render({
            elem: '#test'
            , url: url
            , height: 400
            , width: 760
            , cols: [[
                {type: 'checkbox', fixed: 'left'}
                , {field: 'name', title: '文件名', width: 300, sort: true, style: 'cursor: pointer;', event: 'setSign'}
                , {field: 'date', title: '修改日期', width: 150, sort: true}
                , {field: 'size', title: '文件大小', width: 120, sort: true}
                , {field: 'type', title: '文件类型', width: 120}
            ]]
            , limit: 15
        });


        //layui表格点击文件事件
        table.on('tool(test)', function (obj) {
            var data = obj.data;
            if (obj.event === 'setSign') {
                if (data.type === 'file') {
                    layer.alert("暂不支持打开文件");
                } else {
                    path.push(data.name);
                    var j = {};
                    j.name = path[path.length - 1];
                    breadArray.push(j);
                    breadData = {
                        "bread": breadArray
                    };
                    laytpl(getTpl).render(breadData, function (html) {
                        view.innerHTML = html;
                    });

                    table.reload('test', {
                        where: { //设定异步数据接口的额外参数，任意设
                            path: ('/' + path.join('/') + '/' + this.name).replace(/\/\//, '/')
                        }
                    });
                }
            }
        });

        //监听面包屑导航点击
        window.breadOn = function (breadName) {
            breadArray = [];
            var j = {"name": "root"};
            breadArray.push(j);
            if (breadName === 'root') {
                path = [];
            } else {
                var i = path.indexOf(breadName);
                if (i === 0) {
                    path = [breadName];
                    j = {};
                    j.name = path[0];
                    breadArray.push(j);
                } else {
                    path = path.slice(0, i + 1);
                    for (var x = 0; x < i + 1; x++) {
                        j = {};
                        j.name = path[x];
                        breadArray.push(j);
                    }
                }
            }
            breadData = {
                "bread": breadArray
            };
            laytpl(getTpl).render(breadData, function (html) {
                view.innerHTML = html;
            });

            table.reload('test', {
                where: { //设定异步数据接口的额外参数，任意设
                    path: ('/' + path.join('/') + '/' + this.name).replace(/\/\//, '/')
                }
            });
        };

        //拖拽上传
        upload.render({
            elem: '#test10'
            ,url: 'javascript:;'
            ,done: function(res){
                console.log(res)
            }
        });

        //初始化面包屑导航
        laytpl(getTpl).render(breadData, function (html) {
            view.innerHTML = html;
        });


    });


</script>
</body>
</html>
