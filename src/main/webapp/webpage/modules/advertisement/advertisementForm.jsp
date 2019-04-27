<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>广告信息管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			jp.ajaxForm("#inputForm",function(data){
				if(data.success){
				    jp.success(data.msg);
					jp.go("${ctx}/advertisement/advertisement");
				}else{
				    jp.error(data.msg);
				    $("#inputForm").find("button:submit").button("reset");
				}
			});

	        $('#issueTime').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
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
				<a class="panelButton" href="${ctx}/advertisement/advertisement"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="advertisement" action="${ctx}/advertisement/advertisement/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">图片：</label>
					<div class="col-sm-10">
						<sys:fileUpload path="picture"  value="${advertisement.picture}" type="file" uploadPath="/advertisement/advertisement"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">状态：</label>
					<div class="col-sm-10">
						<form:select path="state" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('bas_release_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">发布时间：</label>
					<div class="col-sm-10">
						<div class='input-group form_datetime' id='issueTime'>
							<input type='text'  name="issueTime" class="form-control "  value="<fmt:formatDate value="${advertisement.issueTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							<span class="input-group-addon">
								<span class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
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