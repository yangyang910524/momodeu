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
            $("#type").change(function() {// 绑定查询按扭
				var type=$("#type").val();
               if(type=='1'){
                   $("#data2").removeAttr("disabled");
                   $("#data2Div").show();
			   }else{
                   $("#data2").attr("disabled","disabled")
                   $("#data2Div").hide();
			   }
            });

            var type=$("#type").val();
            if(type=='1'){
                $("#data2").removeAttr("disabled");
                $("#data2Div").show();
            }else{
                $("#data2").attr("disabled","disabled")
                $("#data2Div").hide();
            }
		});

        function openFileDialog(type)
        {
            if(type=='1'){
                $("#file1").click();
			}else if(type=='2'){
                $("#file2").click();
            }else{
            $("#file3").click();
        }

        }

        function fileSelected1(){
            var index =jp.loading("文件上传中……");
            var filename = $("#file1").val();
            var suffix=(filename.substr(filename.lastIndexOf("."))).toLowerCase();
            if(suffix!=".mp3"&&suffix!=".mp4") {
                jp.info("您上传视频、音频的类型不符合(.mp3|.mp4)！");
                return false;
            }
            var formData = new FormData($("#uploadForm1")[0]);
            formData.append("filePath","homework_original_video")
            $.ajax({
                url:"${ctx}/sys/file/fileUpload",
                type: 'POST',
                data:formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success:function(data) {
                    $("#data1").val(data.body.url);
                    $("#data1	").blur();
                    jp.close(index);
                },
                error:function(data) {
                    jp.info("上传失败");
                }
            });
        }

        function fileSelected2(){
            var index =jp.loading("文件上传中……")
            var filename = $("#file2").val();
            var suffix=(filename.substr(filename.lastIndexOf("."))).toLowerCase();
            if(suffix!=".mp3"&&suffix!=".mp4") {
                jp.info("您上传视频、音频的类型不符合(.mp3|.mp4)！");
                return false;
            }
            var formData = new FormData($("#uploadForm2")[0]);
            formData.append("filePath","homework_dubbing_video")
            $.ajax({
                url:"${ctx}/sys/file/fileUpload",
                type: 'POST',
                data:formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success:function(data) {
                    $("#data2").val(data.body.url);
                    $("#data2").blur();
                    jp.close(index);
                },
                error:function(data) {
                    jp.info("上传失败");
                }
            });
        }

        function fileSelected3(){
            var index =jp.loading();
            var filename = $("#file3").val();
            var suffix=(filename.substr(filename.lastIndexOf("."))).toLowerCase();
            if(suffix!=".jpg"&&suffix!=".gif"&&suffix!=".jpeg"&& suffix!=".png") {
                jp.info("您上传图片的类型不符合(.jpg|.jpeg|.gif|.png)！");
                return false;
            }
            var formData = new FormData($("#uploadForm3")[0]);
            formData.append("filePath","homework_cover")
            $.ajax({
                url:"${ctx}/sys/file/fileUpload",
                type: 'POST',
                data:formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success:function(data) {
                    $("#cover").val(data.body.url);
                    $("#cover").blur();
                    jp.close(index);
                },
                error:function(data) {
                    jp.info("上传失败");
                }
            });
        }

	</script>
</head>
<body>
<!-- 文件上传form beigin-->
<form id= "uploadForm1" action= "" method= "post" enctype ="multipart/form-data">
	<input type="file" id="file1" name="file" style="display: none;"  onchange="fileSelected1()">
</form>
<form id= "uploadForm2" action= "" method= "post" enctype ="multipart/form-data">
	<input type="file" id="file2" name="file" style="display: none;"  onchange="fileSelected2()">
</form>
<form id= "uploadForm3" action= "" method= "post" enctype ="multipart/form-data">
	<input type="file" id="file3" name="file" style="display: none;"  onchange="fileSelected3()">
</form>
<!-- 文件上传form end-->
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
					<label class="col-sm-2 control-label"><font color="red">*</font>课程级别：</label>
					<div class="col-sm-10">
						<form:select path="courseLevel" class="form-control required">
							<form:options items="${fns:getDictList('bae_course_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>作业名称：</label>
					<div class="col-sm-10">
						<form:input path="name" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>作业类型：</label>
                    <div class="col-sm-10">
                        <form:select path="type" class="form-control required">
                            <form:options items="${fns:getDictList('bas_material_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>原音视频：</label>
					<div class="col-sm-10">
						<div class="input-group input-append" style="width:100%">
							<input type="text" id="data1" name="data1"  class="form-control required" readonly="readonly" aria-invalid="false" value="${homework.data1}">
							<span class="input-group-btn">
									<button type="button" onclick="openFileDialog('1')" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
								</span>
						</div>
					</div>
				</div>
				<div class="form-group" id="data2Div">
					<label class="col-sm-2 control-label"><font color="red">*</font>配音视频：</label>
					<div class="col-sm-10">
						<div class="input-group input-append" style="width:100%">
							<input type="text" id="data2" name="data2"  readonly="readonly" class="form-control required" aria-invalid="false" value="${homework.data2}">
							<span class="input-group-btn">
									<button type="button" onclick="openFileDialog('2')" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
								</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>作业封面：</label>
					<div class="col-sm-10">
						<div class="input-group input-append" style="width:100%">
							<input type="text" id="cover" name="cover"  class="form-control required" readonly="readonly" aria-invalid="false" value="${homework.cover}">
							<span class="input-group-btn">
									<button type="button" onclick="openFileDialog('3')" class="btn btn-primary "><i class="fa fa-cloud-upload"></i></button>
								</span>
						</div>
					</div>
				</div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>课程状态：</label>
                    <div class="col-sm-10">
                        <c:if test="${empty id}">
                            草稿
                            <input type="hidden" value="0" name="state"/>
                        </c:if>
                        <c:if test="${not empty id}">
                            ${ fns:getDictLabel (state, 'bas_release_type', '')}
                            <input type="hidden" value="${state}" name="state"/>
                        </c:if>
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
</div>
</body>
</html>