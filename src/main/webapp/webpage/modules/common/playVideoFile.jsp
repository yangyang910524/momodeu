<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title></title>
	<meta name="decorator" content="ani"/>
    <style type="text/css">
        body {
            margin: 0;
            padding: 0px;
            font-family: "Microsoft YaHei", YaHei, "微软雅黑", SimHei, "黑体";
            font-size: 18px;
        }
        p{
            padding-left: 2em;
        }
    </style>
</head>
<body>
<div id="video" style="width: 100%; height: 400px;max-width: 600px;">
</div>
<script type="text/javascript" src="${ctxStatic}/plugin/ckplayer/ckplayer.js" charset="UTF-8"></script>
<script type="text/javascript">
    var videoObject = {
        container: '#video', //容器的ID或className
        variable: 'player', //播放函数名称
        //loop: true, //播放结束是否循环播放
        autoplay: true,//是否自动播放
        poster: '', //封面图片
        preview: { //预览图片
            file: [''],
            scale: 2
        },
        //flashplayer:true,
        //live:true,
        //debug:true,
        video:[
        ['${url}', 'video/mp4', '中文标清', 0]
        ]
    };
    var player = new ckplayer(videoObject);
</script>
</body>
</html>