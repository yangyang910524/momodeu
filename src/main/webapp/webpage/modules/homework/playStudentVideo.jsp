<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学生配音</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${ctxStatic}/plugin/playStudentVideo/css/ckin.css">
    <link rel="stylesheet" href="${ctxStatic}/plugin/playStudentVideo/css/buttons.css">
</head>
<body>
<div class="htmleaf-container">
    <section class="demo-section demo-section--light" id="demo">
        <div class="ckin__player default ckin__overlay is-playing">
            <video id="video1">
                <source src="${userHomework.homework.data1}" type="video/mp4">
            </video>
        </div>


        <div class="container">
            <audio id="video2">
                <source src="${userHomework.file}" type="audio/mp3">
            </audio>
        </div>

        <div class="container" style="margin:0 auto;width:300px;">
            <button onclick="playVid()" type="button" class="button button-3d button-caution">播放</button>
            <button onclick="pauseVid()" type="button" class="button button-3d button-royal">暂停</button>
        </div>

    </section>
</div>


</div>

<script>
    var myVideo=document.getElementById("video1");
    var myVideo2=document.getElementById("video2");

    function playVid()
    {
        myVideo.play();
        myVideo2.play();
    }

    function pauseVid()
    {
        myVideo.pause();
        myVideo2.pause();
    }
</script>
</body>
</html>