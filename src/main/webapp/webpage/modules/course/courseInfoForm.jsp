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
		    	var titleType=$("#titleType").val();
		    	if(titleType==1){
		    	    if($("#cover").val()==""){
		    	        layer.msg("请添加课程封面");
                        return false;
					}
				}
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
		<input type="hidden" value="${courseInfo.parent.id}" name="parent.id"/>
		<c:if test="${empty courseInfo.parent.id}">
			<input id="titleType" type="hidden" value="1" name="titleType"/>
		</c:if>
		<c:if test="${not empty courseInfo.parent.id}">
			<input id="titleType" type="hidden" value="2" name="titleType"/>
		</c:if>
		<table class="table table-bordered">
		   <tbody>
				<c:if test="${not empty courseInfo.parent.id}">
					<tr>
						<td class="width-15 active"><label class="pull-right">课程名称：</label></td>
						<td class="width-35" colspan="3">
							 ${courseInfo.parent.name}
						</td>
					</tr>
				</c:if>
                <tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程级别：</label></td>
					<td class="width-35">
						<c:if test="${empty courseInfo.parent.id}">
							<form:select path="level" class="form-control required">
								<form:options items="${fns:getDictList('bae_course_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</c:if>
						<c:if test="${not empty courseInfo.parent.id}">
							${ fns:getDictLabel (courseInfo.parent.level, 'bae_course_level', '')}
						</c:if>
					</td>
					<td class="width-15 active">
						<label class="pull-right">
							<font color="red">*</font>

							<c:if test="${empty courseInfo.parent.id}">
								课程名称：
							</c:if>
							<c:if test="${not empty courseInfo.parent.id}">
								章节名称：
							</c:if>
						</label>
					</td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</td>
                </tr>
				<c:if test="${empty courseInfo.parent.id}">
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>课程封面：</label></td>
						<td class="width-35" colspan="3">
							<sys:fileUpload path="cover"  value="${courseInfo.cover}" type="file" uploadPath="/course/courseInfo"/>
						</td>
					</tr>
				</c:if>
                <tr>
                    <td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
                    <td class="width-35">
						<c:if test="${empty courseInfo.parent.id and empty courseInfo.id}">
							草稿
							<input type="hidden" value="0" name="state"/>
						</c:if>
						<c:if test="${empty courseInfo.parent.id and not empty courseInfo.id and courseInfo.state eq '0'} ">
							<select path="state" class="form-control ">
								<form:option value="" label=""/>
								<form:options items="${fns:getDictList('bas_release_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</select>
						</c:if>
						<c:if test="${empty courseInfo.parent.id and not empty courseInfo.id and courseInfo.state eq '0'} ">
							<form:select path="state" class="form-control ">
								<form:option value="" label=""/>
								<form:options items="${fns:getDictList('bas_release_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</c:if>
						<c:if test="${not empty courseInfo.parent.id}">
							${ fns:getDictLabel (courseInfo.parent.state, 'bas_release_type', '')}
						</c:if>
                    </td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>排序：</label></td>
					<td class="width-35">
						<form:input path="sort" htmlEscape="false"    class="form-control required digits"/>
					</td>
                </tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">简述说明：</label></td>
					<td class="width-35" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
		</form:form>
</body>
</html>