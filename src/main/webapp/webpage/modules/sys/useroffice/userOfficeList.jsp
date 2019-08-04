<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>信息管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="userOfficeList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">信息列表</h3>
		</div>
		<div class="panel-body">

			<!-- 搜索 -->
			<div id="search-collapse" class="collapse">
				<div class="accordion-inner">
					<form:form id="searchForm" modelAttribute="userOffice" class="form form-horizontal well clearfix">
						<div class="col-xs-12 col-sm-6 col-md-4">
							<label class="label-item single-overflow pull-left" title="用户类型">用户类型：</label>
							<form:select path="userType"  class="form-control m-b">
								<form:option value="" label=""/>
								<form:options items="${fns:getDictList('bas_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
						<div class="col-sm-4">
							<label class="label-item single-overflow pull-left" title="登录名：">登录名：</label>
							<input type="text" name="loginName" maxlength="100"  class=" form-control"/>
						</div>
						<div class="col-sm-4">
							<label class="label-item single-overflow pull-left" title="姓名：">姓名：</label>
							<input type="text" name="userName" maxlength="100"  class=" form-control"/>
						</div>
						<div class="col-sm-4">
							<label class="label-item single-overflow pull-left" title="姓名：">英文名：</label>
							<input type="text" name="englishName" maxlength="100"  class=" form-control"/>
						</div>
						<div class="col-sm-4">
							<label class="label-item single-overflow pull-left" title="姓名：">手机号码：</label>
							<input type="text" name="mobile" maxlength="100"  class=" form-control"/>
						</div>

						<div class="col-xs-12 col-sm-6 col-md-4">
							<div style="margin-top:26px">
								<a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
								<a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
							</div>
						</div>
					</form:form>
				</div>
			</div>

			<!-- 工具栏 -->
			<div id="toolbar">
                <a href="${ctx}/sys/classes/classes" class="btn btn-primary">
                    返回
                </a>
					<c:if test="${fns:getUser().userType ne '2'}">
						<button class="btn btn-primary" onclick="add('1')">
							<i class="glyphicon glyphicon-plus"></i>添加/变更班主任
						</button>
						<button class="btn btn-primary" onclick="add('2')">
							<i class="glyphicon glyphicon-plus"></i>添加老师
						</button>
					</c:if>
					<button class="btn btn-primary" onclick="add('3')">
						<i class="glyphicon glyphicon-plus"></i>添加学生
					</button>
					<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
						<i class="glyphicon glyphicon-remove"></i> 删除
					</button>
					<button id="view" class="btn btn-default" disabled onclick="view()">
						<i class="fa fa-search-plus"></i> 查看
					</button>
			</div>

			<!-- 表格 -->
			<table id="userOfficeTable"   data-toolbar="#toolbar"></table>

			<!-- context menu -->
			<ul id="context-menu" class="dropdown-menu">
				<li data-item="delete"><a>删除</a></li>
				<li data-item="action1"><a>取消</a></li>
			</ul>
		</div>
	</div>
</div>
</body>
</html>