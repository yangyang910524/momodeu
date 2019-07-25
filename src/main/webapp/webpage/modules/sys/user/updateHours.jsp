<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户积分调整</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	function save() {
        var isValidate = jp.validateForm('#inputForm');//校验表单
		if(!isValidate){
			return false;
		}else{
			jp.loading();
			jp.post("${ctx}/sys/user/updateHours",$('#inputForm').serialize(),function(data){
				if(data.success){
					jp.getParent().refresh();
					var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
					parent.layer.close(dialogIndex);
					jp.success(data.msg)

				}else{
					jp.error(data.msg);
				}
			})
		}
	}
	</script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="user"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
		      <tr>
		         <td class="active"><label class="pull-right">姓名:</label></td>
		         <td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control" autocomplete="off" readonly="true"/></td>
                  <td class="active"><label class="pull-right">英文名:</label></td>
                  <td><form:input path="englishName" htmlEscape="false" maxlength="50" class="form-control" autocomplete="off" readonly="true"/></td>
		      </tr>
			  <tr>
				  <td class="active"><label class="pull-right">手机号码:</label></td>
				  <td><form:input path="mobile" htmlEscape="false" maxlength="100" class="form-control" autocomplete="off" readonly="true"/></td>
				  <td class="active"><label class="pull-right">登录名:</label></td>
				  <td><input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
					  <form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control userName" autocomplete="off" readonly="true"/></td>
			  <tr>
			  <tr>
				  <td class="active"><label class="pull-right">调整前课时:</label></td>
				  <td><input class="form-control" readonly="readonly" value="${user.hours}"/></td>
				  <td class="active"><label class="pull-right"><font color="red">*</font>调整后课时:</label></td>
				  <td><form:input path="hours" htmlEscape="false" maxlength="50" class="form-control required digits" autocomplete="off"/></td>
			  </tr>
			  <tr>
		         <td class="active"><label class="pull-right">备注:</label></td>
		         <td colspan="3"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control" autocomplete="off"/></td>
		      </tr>
		      </tbody>
		      </table>
	</form:form>
</body>
</html>