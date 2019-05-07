<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>积分排名</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
    <script>
        function testMp3() {
            layer.open({
                type: 2,
                title: false,
                area: ['630px', '360px'],
                shade: 0.8,
                closeBtn: 0,
                shadeClose: true,
                content: 'https://webmomofile.oss-cn-beijing.aliyuncs.com/user_homework/8eaef66b4155420c99096a2d3466aa09/20190506172421100.mp3'
            });
        }
    </script>
</head>
<body>
    <button type="button" onclick="testMp3()">测试在线播放</button>
</body>
</html>