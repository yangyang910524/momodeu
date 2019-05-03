<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<link href="${ctxStatic}/plugin/bootstrapTree/bootstrap-treeview.css" rel="stylesheet" type="text/css"/>
	<script src="${ctxStatic}/plugin/bootstrapTree/bootstrap-treeview.js" type="text/javascript"></script>
	<%@ include file="userIndex.js"%>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">用户列表</h3>
	</div>
	<div class="panel-body">
	<div class="row">
		<div  class="">
			<!-- 搜索框-->
			<div id="search-collapse" class="collapse">
				<div class="accordion-inner">
					<form id="searchForm" class="form form-horizontal well clearfix" >
						<div class="col-sm-4">
							<label class="label-item single-overflow pull-left" title="登录名：">登录名：</label>
							<input type="text" name="loginName" maxlength="100"  class=" form-control"/>
						</div>
						<div class="col-sm-4">
							<label class="label-item single-overflow pull-left" title="姓名：">姓名：</label>
							<input type="text" name="name" maxlength="100"  class=" form-control"/>
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
							<label class="label-item single-overflow pull-left" title="用户类型：">用户类型：</label>
							<select name="userType" value="userType"  class="form-control m-b">
								<option value=""></option>
								<option value="1" <c:if test="${userType eq '1'}">selected</c:if>  >管理员</option>
								<option value="2" <c:if test="${userType eq '2'}">selected</c:if>  >老师</option>
								<option value="3" <c:if test="${userType eq '3'}">selected</c:if>  >学生</option>
							</select>
						</div>
					    <div class="col-sm-4">
							 <div style="margin-top:26px">
							  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
							  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
							</div>
					    </div>
					</form>
				</div>
			</div><!-- 搜索框结束 -->

		<!-- 工具栏 -->
	    <div id="toolbar">

	    	<shiro:hasPermission name="sys:user:add">
	    		<a id="add" class="btn btn-primary"  onclick="jp.openSaveDialog('新建用户', '${ctx}/sys/user/form','800px', '680px')"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:user:edit">
				<button id="edit" class="btn btn-success" disabled onclick="edit()">
		            <i class="glyphicon glyphicon-edit"></i> 修改
		        </button>
			</shiro:hasPermission>
			<shiro:hasPermission name="sys:user:del">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
		            <i class="glyphicon glyphicon-remove"></i> 删除
		        </button>
			</shiro:hasPermission>
	    </div><!-- 工具栏结束 -->


	    <!-- 表格 -->
	    <table id="table"
	           data-toolbar="#toolbar">
	    </table>

	    <!-- context menu -->
	    <ul id="context-menu" class="dropdown-menu">
	        <li data-item="edit"><a>编辑</a></li>
	        <li data-item="delete"><a>删除</a></li>
	        <li data-item="cancel"><a>取消</a></li>
	    </ul>
	</div>
	</div>
	</div>
	</div>
	</div>
</body>
</html>