<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>课程信息管理</title>
	<meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/fileUpload.jsp"%>
	<script type="text/javascript">

        $(document).ready(function() {
            jp.ajaxForm("#inputForm",function(data){
                if(data.success){
                    jp.success(data.msg);
                    jp.go("${ctx}/course/courseInfo?parentIds="+data.body.parentIds);
                }else{
                    jp.error(data.msg);
                    $("#inputForm").find("button:submit").button("reset");
                }
            });
        });

        function openFileDialog()
        {
            $("#file").click();
        }

        function fileSelected(obj){
            var index =jp.loading("文件上传中……")
            var file=obj.files[0];//获取文件流
            var filename= obj.value;
            var suffix=(filename.substr(filename.lastIndexOf("."))).toLowerCase();
            if(suffix!=".jpg"&&suffix!=".gif"&&suffix!=".jpeg"&& suffix!=".png") {
                jp.info("您上传图片的类型不符合(.jpg|.jpeg|.gif|.png)！");
                return false;
            }
            var path = "coruse_cover/${fns:getUser()}/"+timestamp()+suffix;
            fileUpload(file,path,function (data) {
                $("#cover").val(data);
                $("#cover").blur();
                $("#coverShow").attr("src",data);
                $("#coverShow").attr("onclick","jp.showPic('"+data+"')");
                jp.close(index);
            },function (err) {
                console.log(err);
                jp.info("上传失败");
            });
        }

	</script>
</head>
<body>
<!-- 文件上传form beigin-->
<form id= "uploadForm" action= "" method= "post" enctype ="multipart/form-data">
	<input type="file" id="file" name="file" style="display: none;"  onchange='fileSelected(this)'>
</form>
<!-- 文件上传form end-->
<div class="wrapper wrapper-content">
	<div class="row">
		<div class="col-md-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<a class="panelButton" href="${ctx}/course/courseInfo"><i class="ti-angle-left"></i> 返回</a>
					</h3>
				</div>
				<form:form id="inputForm" modelAttribute="courseInfo" action="${ctx}/course/courseInfo/save" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<input type="hidden" value="${courseInfo.parent.id}" name="parent.id"/>
				<c:if test="${empty courseInfo.parent.id}">
					<input id="titleType" type="hidden" value="1" name="titleType"/>
				</c:if>
				<c:if test="${not empty courseInfo.parent.id}">
					<input id="titleType" type="hidden" value="2" name="titleType"/>
				</c:if>

				<c:if test="${not empty courseInfo.parent.id}">
					<div class="form-group">
						<label class="col-sm-2 control-label">课程名称：</label>
						<div class="col-sm-10">
								${courseInfo.parent.name}
						</div>
					</div>
				</c:if>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>课程级别：</label>
					<div class="col-sm-10">
						<c:if test="${empty courseInfo.parent.id}">
							<form:select path="level" class="form-control required">
								<form:options items="${fns:getDictList('bae_course_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</c:if>
						<c:if test="${not empty courseInfo.parent.id}">
							${ fns:getDictLabel (courseInfo.parent.level, 'bae_course_level', '')}
						</c:if>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>
						<c:if test="${empty courseInfo.parent.id}">
							课程名称：
						</c:if>
						<c:if test="${not empty courseInfo.parent.id}">
							章节名称：
						</c:if>
					</label>
					<div class="col-sm-10">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>

				<c:if test="${empty courseInfo.parent.id}">
					<div class="form-group" >
						<label class="col-sm-2 control-label"><font color="red">*</font>课程封面：</label>
						<div class="col-sm-10">
							<div class="input-group input-append" style="width:100%">
								<input type="text" id="cover" name="cover"  class="form-control required" readonly="readonly" aria-invalid="false" value="${courseInfo.cover}">
								<span class="input-group-btn">
									<button type="button" id="photoButton" onclick="openFileDialog()" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
								</span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">封面预览：</label>
						<div class="col-sm-10">
							<div class="input-group" style="width:100%">
								<img id="coverShow"  onclick="jp.showPic('${courseInfo.cover}')" height="34px" src="${courseInfo.cover}">
							</div>
						</div>
					</div>
				</c:if>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>课程状态：</label>
					<div class="col-sm-10">
						<c:if test="${empty courseInfo.parent.id and empty courseInfo.id}">
							草稿
							<input type="hidden" value="0" name="state"/>
						</c:if>
						<c:if test="${empty courseInfo.parent.id and not empty courseInfo.id}">
							${ fns:getDictLabel (courseInfo.state, 'bas_release_type', '')}
							<input type="hidden" value="${courseInfo.state}" name="state"/>
						</c:if>
						<c:if test="${not empty courseInfo.parent.id}">
							${ fns:getDictLabel (courseInfo.parent.state, 'bas_release_type', '')}
						</c:if>
					</div>
				</div>

				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>
						<c:if test="${empty courseInfo.parent.id}">
							课程排序：
						</c:if>
						<c:if test="${not empty courseInfo.parent.id}">
							章节排序：
						</c:if>
					</label>
					<div class="col-sm-10">
						<form:input path="sort" htmlEscape="false"    class="form-control required digits"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">简述说明：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>

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
</body>
</html>