<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
	function save() {
		//判断头像是否上传
		if($("#photo").val()==""){
		    layer.msg("请上传头像！");
		   return false;
		}
        var isValidate = jp.validateForm('#inputForm');//校验表单
		if(!isValidate){
			return false;
		}else{
			jp.loading();
			jp.post("${ctx}/sys/user/save",$('#inputForm').serialize(),function(data){
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
	$(document).ready(function() {
		$("#no").focus();
		$("#inputForm").validate({
			rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
			messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				}
		});
		
	});

	function changeRole(value) {
        if(value=='f89f1a4157b249a2a621c37ab3941cdb'){
            $("#score").removeAttr("disabled");
        }else{
            $("#score").attr("disabled","disabled");
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
		         <td class="width-15 active">	<label class="pull-right"><font color="red">*</font>头像:</label></td>
		         <td class="width-35">
						<sys:fileUpload path="photo" value="${user.photo}" type="image" uploadPath="photo" fileNumLimit="4" readonly="false"/>
                 </td>
                  <td class="active"><label class="pull-right"><font color="red">*</font><c:if test="${not empty user.id}">(不可修改)</c:if>角色:</label></td>
                  <td>
                      <c:if test="${not empty user.id}">
                          <select class="form-control required" name="role.id" onchange="changeRole(this.value)">
                              <c:forEach items="${user.roleList}" var="role" varStatus="status">
                                  <option value="${role.id}">${role.name}</option>
                              </c:forEach>--%>
                          </select>
                      </c:if>
                      <c:if test="${empty user.id}">
                          <select class="form-control required" name="role.id" onchange="changeRole(this.value)">
                              <c:forEach items="${allRoles}" var="role" varStatus="status">
                                  <option value="${role.id}">${role.name}</option>
                              </c:forEach>--%>
                          </select>
                      </c:if>
                  </td>
              </tr>
		      
		      <tr>
		         <td class="active"><label class="pull-right"><font color="red">*</font>姓名:</label></td>
		         <td><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" autocomplete="off"/></td>
                  <td class="active"><label class="pull-right"><font color="red">*</font>英文名:</label></td>
                  <td><form:input path="englishName" htmlEscape="false" maxlength="50" class="form-control required" autocomplete="off"/></td>
		      </tr>

              <tr>
				  <td class="active"><label class="pull-right"><font color="red">*</font>手机号码:</label></td>
				  <td><form:input path="mobile" htmlEscape="false" maxlength="100" class="form-control required" autocomplete="off"/></td>

				  <td class="active"><label class="pull-right"><font color="red">*</font>登录名:</label></td>
                  <td><input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
                      <form:input path="loginName" htmlEscape="false" maxlength="50" class="form-control required userName" autocomplete="off"/></td>
			  <tr>
		         <td class="active"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>密码:</label></td>
		         <td><input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="form-control ${empty user.id?'required':''}" autocomplete="off"/>
					<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if></td>
		         <td class="active"><label class="pull-right"><c:if test="${empty user.id}"><font color="red">*</font></c:if>确认密码:</label></td>
		         <td><input id="confirmNewPassword" name="confirmNewPassword" type="password"  class="form-control ${empty user.id?'required':''}" value="" maxlength="50" minlength="3" equalTo="#newPassword" autocomplete="off"/></td>
		      </tr>
		       <tr>
				    <td class="active"><label class="pull-right">邮箱:</label></td>
		         <td><form:input path="email" htmlEscape="false" maxlength="100" class="form-control email" autocomplete="off"/></td>
				   <td class="active"><label class="pull-right">是否允许登录:</label></td>
				   <td><form:select path="loginFlag"  class="form-control">
					   <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				   </form:select></td>
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