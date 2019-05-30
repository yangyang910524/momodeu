<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>消息公告管理</title>
	<meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/fileUpload.jsp"%>
	<script type="text/javascript">

		$(document).ready(function() {
			jp.ajaxForm("#inputForm",function(data){
				if(data.success){
				    jp.success(data.msg);
					jp.go("${ctx}/notice/notice");
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
            var path = "notice_picture/${fns:getUser()}/"+timestamp()+suffix;
            fileUpload(file,path,function (data) {
                $("#picture").val(data);
                $("#picture").blur();
                jp.close(index);
            },function () {
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
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/notice/notice"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="notice" action="${ctx}/notice/notice/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>标题：</label>
					<div class="col-sm-10">
						<form:input path="title" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>内容：</label>
					<div class="col-sm-10">
						<form:textarea path="content" htmlEscape="false" rows="4"    class="form-control required"/>
					</div>
				</div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>图片：</label>
                    <div class="col-sm-10">
                        <div class="input-group input-append" style="width:100%">
                            <input type="text" id="picture" name="picture"  class="form-control required" readonly="readonly" aria-invalid="false" value="${notice.picture}">
                            <span class="input-group-btn">
                                        <button type="button"  onclick="openFileDialog()" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
                                    </span>
                        </div>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>状态：</label>
                    <div class="col-sm-10">
                        <c:if test="${mode eq 'add'}">
                            <input type="hidden" name="state" value="0"/>
                            <input type="text" class="form-control "  value="${ fns:getDictLabel ('0', 'bas_release_type', '')}" readonly="readonly"/>
                        </c:if>
                        <c:if test="${mode ne 'add'}">
                            <input type="hidden" name="state" value="${notice.state}"/>
                            <input type="text" class="form-control "  value="${ fns:getDictLabel (notice.state, 'bas_release_type', '')}"  readonly="readonly"/>
                        </c:if>
                    </div>
				</div>
			<div class="form-group">
				<label class="col-sm-2 control-label">备注信息：</label>
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
</div>
</body>
</html>