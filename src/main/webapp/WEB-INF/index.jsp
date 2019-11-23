<%--
  User: lushu
  Date: 2019/11/23
  Time: 14:24
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页</title>
    <link rel="stylesheet" href="../layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">

    <div class="layui-header layui-bg-gray">

        <div class="layui-logo" style="font-size: 20px; color: #393D49">作业查重系统</div>

        <ul class="layui-nav layui-layout-right" >
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
                        <div class="layui-col-md12" style="background: #00FF00; margin-bottom: 15px">
                            搜索框
                        </div>
                        <div class="layui-col-md9" style="background: #1E9FFF">
                            作业提交记录列表
                        </div>
                        <div class="layui-col-md3" style="background: #8D8D8D">
                            最近选择教师
                        </div>
                        <div class="layui-col-md3 layui-col-md-offset9" style="background: #00FF00">
                            最近通知列表
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


</div>

<script src="../src/layui.js"></script>
<script>
    //JavaScript代码区域
    layui.use('element', function(){
        var element = layui.element;

    });
</script>
</body>
</html>
