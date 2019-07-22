<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>老师打分</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">

		$(document).ready(function() {
			jp.ajaxForm("#inputForm",function(data){
				if(data.success){
				    jp.success(data.msg);
					jp.go("${ctx}/userhomework/userHomework/homeworkGradingList");
				}else{
				    jp.error(data.msg);
				    $("#inputForm").find("button:submit").button("reset");
				}
			});
            selectStar('${userHomework.score}');
		});

		function selectStar(score) {
		    if(score==null||score==""||score==undefined||score=="0"||score==0){
                score=1;
            }else{
                score=Number(score);
            }
            for(var i=1;i<6;i++){
                if(i<score+1){
                    $("#star"+i).attr("class","glyphicon glyphicon-star")
                }else{
                    $("#star"+i).attr("class","glyphicon glyphicon-star-empty")
                }
            }
            $("#score").val(score);
        }
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="${ctx}/userhomework/userHomework/homeworkGradingList"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="userHomework" action="${ctx}/userhomework/userHomework/gradingSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label">班级名称：</label>
                    <div class="col-sm-10">
                        <input htmlEscape="false"  class="form-control" readonly="readonly" value="${userHomework.office.name}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">学生姓名：</label>
                    <div class="col-sm-10">
                        <input htmlEscape="false"  class="form-control" readonly="readonly" value="${userHomework.student.name}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">作业名称：</label>
                    <div class="col-sm-10">
                        <input htmlEscape="false"  class="form-control" readonly="readonly" value="${userHomework.homework.name}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">作业类型：</label>
                    <div class="col-sm-10">
                        <input htmlEscape="false"  class="form-control" readonly="readonly" value="${fns:getDictLabel(userHomework.homework.type, 'bas_material_type', '')}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">原音视频：</label>
                    <div class="col-sm-10">
                        <a onclick="jp.playVideo('${userHomework.homework.data1}')" >播放视频</a>
                    </div>
                </div>
                <c:if test="${userHomework.homework.type eq '1'}">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">配音视频：</label>
                        <div class="col-sm-10">
                            <a onclick="jp.playVideo('${userHomework.homework.data2}')">播放视频</a>
                        </div>
                    </div>
                </c:if>
            <c:if test="${userHomework.state ne '0'or mode == 'edit'}">
                <div class="form-group">
                    <label class="col-sm-2 control-label">学生音频：</label>
                    <div class="col-sm-10">
                        <c:if test="${not empty userHomework.file}">
                            <a href="${ctx}/userhomework/userHomework/playStudentVideo?id=${userHomework.id}" target="_blank">播放音频</a>
                        </c:if>
                    </div>
                </div>
				<div class="form-group">
					<label class="col-sm-2 control-label">老师打分：</label>
					<div class="col-sm-10">
                        <input type="hidden" id="score" name="score"/>
                        <a onclick="selectStar(1)"><i id="star1" class=""></i></a>
                        <a onclick="selectStar(2)"><i id="star2" class=""></i></a>
                        <a onclick="selectStar(3)"><i id="star3" class=""></i></a>
                        <a onclick="selectStar(4)"><i id="star4" class=""></i></a>
                        <a onclick="selectStar(5)"><i id="star5" class=""></i></a>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">老师评语：</label>
					<div class="col-sm-10">
						<form:textarea path="comment" htmlEscape="false"    class="form-control"/>
					</div>
				</div>
                </c:if>
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