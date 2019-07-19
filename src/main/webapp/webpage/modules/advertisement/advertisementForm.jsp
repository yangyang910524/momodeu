<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>广告信息管理</title>
	<meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/fileUpload.jsp"%>
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

        function openFileDialog()
        {
            $("#file").click();
        }

        function openFileDialog2()
        {
            $("#file2").click();
        }

        function openFileDialog3()
        {
            $("#file3").click();
        }
        function openFileDialog4()
        {
            $("#file4").click();
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
            var path = "advertisement_picture/${fns:getUser()}/"+timestamp()+suffix;
            fileUpload(file,path,function (data) {
                $("#picture").val(data);
                $("#picture").blur();
                jp.close(index);
            },function () {
                jp.info("上传失败");
            });
        }

        function fileSelected2(obj){
            var index =jp.loading("文件上传中……")
            var file=obj.files[0];//获取文件流
            var filename= obj.value;
            var suffix=(filename.substr(filename.lastIndexOf("."))).toLowerCase();
            if(suffix!=".jpg"&&suffix!=".gif"&&suffix!=".jpeg"&& suffix!=".png") {
                jp.info("您上传图片的类型不符合(.jpg|.jpeg|.gif|.png)！");
                return false;
            }
            var path = "advertisement_content/${fns:getUser()}/"+timestamp()+suffix;
            fileUpload(file,path,function (data) {
                $("#content").val(data);
                $("#content").blur();
                jp.close(index);
            },function () {
                jp.info("上传失败");
            });
        }

        function fileSelected3(obj){
            var index =jp.loading("文件上传中……")
            var file=obj.files[0];//获取文件流
            var filename= obj.value;
            var suffix=(filename.substr(filename.lastIndexOf("."))).toLowerCase();
            if(suffix!=".mp3"&&suffix!=".mp4") {
                jp.info("您上传视频的类型不符合(.ma3|.mp4)！");
                return false;
            }
            var path = "advertisement_video/${fns:getUser()}/"+timestamp()+suffix;
            fileUpload(file,path,function (data) {
                $("#video").val(data);
                $("#video").blur();
                jp.close(index);
            },function () {
                jp.info("上传失败");
            });
        }

        function fileSelected4(obj){
            var index =jp.loading("文件上传中……")
            var file=obj.files[0];//获取文件流
            var filename= obj.value;
            var suffix=(filename.substr(filename.lastIndexOf("."))).toLowerCase();
            if(suffix!=".jpg"&&suffix!=".gif"&&suffix!=".jpeg"&& suffix!=".png") {
                jp.info("您上传图片的类型不符合(.jpg|.jpeg|.gif|.png)！");
                return false;
            }
            var path = "advertisement_content2/${fns:getUser()}/"+timestamp()+suffix;
            fileUpload(file,path,function (data) {
                $("#content2").val(data);
                $("#content2").blur();
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
    <input type="file" id="file2" name="file2" style="display: none;"  onchange='fileSelected2(this)'>
    <input type="file" id="file3" name="file3" style="display: none;"  onchange='fileSelected3(this)'>
    <input type="file" id="file4" name="file4" style="display: none;"  onchange='fileSelected4(this)'>
</form>
<!-- 文件上传form end-->
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
                    <label class="col-sm-2 control-label"><font color="red">*</font>标题：</label>
                    <div class="col-sm-10">
                        <form:input path="title" htmlEscape="false"    class="form-control required"/>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>封面：</label>
					<div class="col-sm-10">
                        <div class="input-group input-append" style="width:100%">
                            <input type="text" id="picture" name="picture"  class="form-control required" readonly="readonly" aria-invalid="false" value="${advertisement.picture}">
                            <span class="input-group-btn">
									<button type="button"  onclick="openFileDialog()" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
								</span>
                        </div>
                    </div>
				</div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>内容一：</label>
                    <div class="col-sm-10">
                        <div class="input-group input-append" style="width:100%">
                            <input type="text" id="content" name="content"  class="form-control required" readonly="readonly" aria-invalid="false" value="${advertisement.content}">
                            <span class="input-group-btn">
                                <button type="button"  onclick="openFileDialog2()" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>内容二：</label>
                    <div class="col-sm-10">
                        <div class="input-group input-append" style="width:100%">
                            <input type="text" id="content2" name="content2"  class="form-control required" readonly="readonly" aria-invalid="false" value="${advertisement.content2}">
                            <span class="input-group-btn">
                                            <button type="button"  onclick="openFileDialog4()" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
                                        </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>视频：</label>
                    <div class="col-sm-10">
                        <div class="input-group input-append" style="width:100%">
                            <input type="text" id="video" name="video"  class="form-control required" readonly="readonly" aria-invalid="false" value="${advertisement.video}">
                            <span class="input-group-btn">
                                    <button type="button"  onclick="openFileDialog3()" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
                                </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>链接一：</label>
                    <div class="col-sm-10">
                        <form:input path="link" htmlEscape="false"    class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>链接二：</label>
                    <div class="col-sm-10">
                        <form:input path="link2" htmlEscape="false"    class="form-control required"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>类型：</label>
                    <div class="col-sm-10">
                        <form:select path="type" class="form-control required">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('bas_advertisement_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label">状态：</label>
					<div class="col-sm-10">
                        <c:if test="${mode eq 'add'}">
                            <input type="hidden" name="state" value="0"/>
                            <input type="text" class="form-control "  value="${ fns:getDictLabel ('0', 'bas_release_type', '')}" readonly="readonly"/>
                        </c:if>
                        <c:if test="${mode ne 'add'}">
                            <input type="hidden" name="state" value="${advertisement.state}"/>
                            <input type="text" class="form-control "  value="${ fns:getDictLabel (advertisement.state, 'bas_release_type', '')}"  readonly="readonly"/>
                        </c:if>
					</div>
				</div>
                <c:if test="${mode ne 'add'}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">发布时间：</label>
                        <div class="col-sm-10">
                            <input type='text' class="form-control "  value="<fmt:formatDate value="${advertisement.issueTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly"/>
                        </div>
                    </div>
                </c:if>
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