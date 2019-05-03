<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程信息管理</title>
	<meta name="decorator" content="ani"/>
	<%@include file="courseInfoList.js" %>
</head>
<body>

	<div class="wrapper wrapper-content">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">课程信息列表  </h3>
			</div>
			
			<div class="panel-body">
	
			<!-- 工具栏 -->
			<div class="row">
			<div class="col-sm-12">
				<div class="pull-left treetable-bar">
					<shiro:hasPermission name="course:courseInfo:add">
						<a id="add" href="javascript: return false" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 添加课程</a><!-- 增加按钮 -->
					</shiro:hasPermission>
			       <button class="btn btn-default" data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
				
				</div>
			</div>
			</div>
			<table id="courseInfoTreeTable" class="table table-hover">
				<thead>
					<tr>

						<th>名称</th>
						<th>封面</th>
						<th>课程级别</th>
						<th>状态</th>
						<th>排序</th>
						<th>简述说明</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="courseInfoTreeTableList"></tbody>
			</table>
			<br/>
			</div>
			</div>
	</div>
</body>
</html>