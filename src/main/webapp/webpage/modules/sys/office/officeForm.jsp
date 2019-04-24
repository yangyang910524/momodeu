<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>机构管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
            }else{
                jp.loading();
                jp.post("${ctx}/sys/office/save",$('#inputForm').serialize(),function(data){
                    if(data.success){
                        jp.getParent().refreshNode(data);
                        jp.success(data.msg);
                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                        parent.layer.close(dialogIndex);
                    }else {
                        jp.error(data.msg);
                    }
                })
            }

        }
	</script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="office"  method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		       <tr>
		         <td class="width-15 active"><label class="pull-right"><font color="red">*</font>班级名称:</label></td>
		         <td class="width-35"><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required" autocomplete="off"/></td>
		      </tr>
               <tr>
                   <td  class="width-15 active" ><label class="pull-right">备注:</label></td>
                   <td class="width-35"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control"  autocomplete="off"/></td>
               </tr>
	      </tbody>
	      </table>
	</form:form>
</body>
</html>