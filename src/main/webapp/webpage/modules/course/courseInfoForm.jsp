<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程信息管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
		});

		function save() {
	            var isValidate = jp.validateForm('#inputForm');//校验表单
	            if(!isValidate){
	                return false;
		    }else{
	                jp.loading();
	                jp.post("${ctx}/course/courseInfo/save",$('#inputForm').serialize(),function(data){
	                    if(data.success){
	                        jp.getParent().refreshTree();
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
		<form:form id="inputForm" modelAttribute="courseInfo" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">父级编号：</label></td>
					<td class="width-35">
						<sys:treeselect id="parent" name="parent.id" value="${courseInfo.parent.id}" labelName="parent.name" labelValue="${courseInfo.parent.name}"
						title="父级编号" url="/course/courseInfo/treeData" extId="${courseInfo.id}" cssClass="form-control " allowClear="true"/>
					</td>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">封面：</label></td>
                    <td class="width-35">
                        <sys:fileUpload path="cover"  value="${courseInfo.cover}" type="file" uploadPath="/course/courseInfo"/>
                    </td>
                    <td class="width-15 active"><label class="pull-right">标题类型：</label></td>
                    <td class="width-35">
                        <form:select path="titleType" class="form-control ">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('bas_course_title_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td class="width-15 active"><label class="pull-right">课程级别：</label></td>
                    <td class="width-35">
                        <form:select path="level" class="form-control ">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('bae_course_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </td>
                    <td class="width-15 active"><label class="pull-right">状态：</label></td>
                    <td class="width-35">
                        <form:select path="state" class="form-control ">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('bas_release_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </td>
                </tr>
		 	</tbody>
		</table>
		</form:form>
</body>
</html>