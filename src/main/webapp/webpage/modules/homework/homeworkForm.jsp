<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>作业信息管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			jp.ajaxForm("#inputForm",function(data){
				if(data.success){
				    jp.success(data.msg);
					jp.go("${ctx}/homework/homework");
				}else{
				    jp.error(data.msg);
				    $("#inputForm").find("button:submit").button("reset");
				}
			});

		});
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/homework/homework"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="homework" action="${ctx}/homework/homework/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">名称：</label>
					<div class="col-sm-10">
						<form:input path="name" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">课程级别：</label>
                    <div class="col-sm-10">
                        <form:select path="courseLevel" class="form-control ">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('bae_course_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">作业类型：</label>
                    <div class="col-sm-10">
                        <form:select path="type" class="form-control ">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('bas_material_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label">材料1：</label>
					<div class="col-sm-10">
						<sys:fileUpload path="data1"  value="${homework.data1}" type="file" uploadPath="/homework/homework"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">材料2：</label>
					<div class="col-sm-10">
						<sys:fileUpload path="data2"  value="${homework.data2}" type="file" uploadPath="/homework/homework"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">封面：</label>
					<div class="col-sm-10">
						<sys:fileUpload path="cover"  value="${homework.cover}" type="file" uploadPath="/homework/homework"/>
					</div>
				</div>
		<c:if test="${mode == 'add' || mode=='edit'}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 </div>
		             </div>
		        </div>
		</c:if>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>