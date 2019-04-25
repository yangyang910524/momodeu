<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@ include file="roleList.js"%>
	<%@ include file="role-userList.js"%>
	
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">角色列表</h3>
	</div>
	<div class="panel-body">
	  <div class="row">
			<div id="left" class="col-sm-12">
                <!-- 表格 -->
                <table id="table"></table>
		    </div>
		   </div>
		   </div>
		   </div>
	</div>
</body>
</html>