<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>信息管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="couresOfficeList.js" %>
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
			<form:form id="searchForm" modelAttribute="couresOffice" class="form form-horizontal well clearfix">
            <div class="col-sm-2">
                <label class="label-item single-overflow pull-left" title="课程名称：">课程名称：</label>
                <input type="text" name="father.name" maxlength="100"  class=" form-control" value="${father.name}"/>
            </div>
            <div class="col-sm-2">
                <label class="label-item single-overflow pull-left" title="章节名称：">章节名称：</label>
                <input type="text" name="courseInfo.name" maxlength="100"  class=" form-control" value="${courseInfo.name}"/>
            </div>
            <div class="col-sm-2">
                <label class="label-item single-overflow pull-left" title="课程级别：">课程级别：</label>
                <select  class="form-control m-b" name="father.level">
                    <option value="" ></option>
                    <c:forEach items="${fns:getDictList('bae_course_level')}" var="l">
                        <option value="${l.value}" <c:if test="${l.value eq father.level}">selected</c:if>>${l.label}</option>
                    </c:forEach>
                </select>
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
            <button id="add" class="btn btn-primary" onclick="add()">
                <i class="glyphicon glyphicon-plus"></i> 添加课程
            </button>
            <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
                <i class="glyphicon glyphicon-remove"></i> 删除
            </button>
    </div>
		
	<!-- 表格 -->
	<table id="couresOfficeTable"   data-toolbar="#toolbar"></table>

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