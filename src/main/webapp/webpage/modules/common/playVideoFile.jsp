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
<script type="text/javascript" src="ckplayer/ckplayer.js" charset="UTF-8"></script>
<script type="text/javascript">
    var videoObject = {
        container: '#video', //容器的ID或className
        variable: 'player', //播放函数名称
        //loop: true, //播放结束是否循环播放
        autoplay: true,//是否自动播放
        poster: 'material/poster.jpg', //封面图片
        preview: { //预览图片
            file: ['material/mydream_en1800_1010_01.png', 'material/mydream_en1800_1010_02.png'],
            scale: 2
        },
        //flashplayer:true,
        //live:true,
        //debug:true,
        video:[
            ['http://img.ksbbs.com/asset/Mon_1703/05cacb4e02f9d9e.mp4', 'video/mp4', '中文标清', 0],
            ['http://img.ksbbs.com/asset/Mon_1703/d0897b4e9ddd9a5.mp4', 'video/mp4', '中文高清', 0],
            ['http://img.ksbbs.com/asset/Mon_1703/eb048d7839442d0.mp4', 'video/mp4', '英文高清', 0],
            ['http://img.ksbbs.com/asset/Mon_1703/d30e02a5626c066.mp4', 'video/mp4', '英文超清', 0]
        ]
    };
    var player = new ckplayer(videoObject);
</script>
</body>
</html>