<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>作业信息管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<script>
        $(document).ready(function() {
            $('#homeworkTable').bootstrapTable({

                //请求方法
                method: 'post',
                //类型json
                dataType: "json",
                contentType: "application/x-www-form-urlencoded",
                //显示检索按钮
                showSearch: true,
                //显示刷新按钮
                showRefresh: true,
                //显示切换手机试图按钮
                showToggle: true,
                //显示 内容列下拉框
                showColumns: true,
                //显示到处按钮
                showExport: true,
                //显示切换分页按钮
                showPaginationSwitch: true,
                //最低显示2行
                minimumCountColumns: 2,
                //是否显示行间隔色
                striped: true,
                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                cache: false,
                //是否显示分页（*）
                pagination: true,
                //排序方式
                sortOrder: "asc",
                //初始化加载第一页，默认第一页
                pageNumber:1,
                //每页的记录行数（*）
                pageSize: 10,
                //可供选择的每页的行数（*）
                pageList: [10, 25, 50, 100],
                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
                url: "${ctx}/userhomework/userHomework/homeworkGradingListData",
                //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
                //queryParamsType:'',
                ////查询参数,每次调用是会带上这个参数，可自定义
                queryParams : function(params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
                    searchParam.pageSize = params.limit === undefined? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                    return searchParam;
                },
                //分页方式：client客户端分页，server服务端分页（*）
                sidePagination: "server",
                contextMenuTrigger:"right",//pc端 按右键弹出菜单
                contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
                contextMenu: '#context-menu',
                onContextMenuItem: function(row, $el){
                    if($el.data("item") == "edit"){
                        edit(row.id);
                    }
                },

                onClickRow: function(row, $el){
                },
                onShowSearch: function () {
                    $("#search-collapse").slideToggle();
                },
                columns: [{
                    checkbox: true

        },
                    {
                    field: 'office.name',
                    title: '班级名称',
                    sortable: false
                },{
                    field: 'student.name',
                    title: '学生姓名',
                    sortable: false
                },{
                    field: 'homework.name',
                    title: '作业名称',
                    sortable: false
                },{
                    field: 'homework.type',
                    title: '作业类型',
                    sortable: false,
                    formatter:function(value, row , index){
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('bas_material_type'))}, value, "-");
                    }
                },{
                    field: 'state',
                    title: '作业状态',
                    sortable: false,
					formatter:function(value, row , index){
						return jp.getDictLabel(${fns:toJson(fns:getDictList('bas_finish_state'))}, value, "-");
					}
                },{
                    field: 'score',
                    title: '打分情况',
                    sortable: false,
					formatter:function(value, row , index){
                        if(row.state=='0'||row.state=='1'){
                            return '-';
						}else{
                            return value;
						}
					}
                },{
                    field: 'comment',
                    title: '老师评语',
                    sortable: false,
                    formatter:function(value, row , index){
                        if(row.state=='0'||row.state=='1'){
                            return '-';
                        }else{
                            return value;
                        }
                    }
                },{
                    field: 'id',
                    title: '操作',
                    sortable: false,
					formatter:function(value, row , index){
                        if(row.state=='1'){
                            return "<a href='${ctx}/userhomework/userHomework/gradingForm/edit?id="+value+"'>打分</a>";
                        }else{
                            return "<a href='${ctx}/userhomework/userHomework/gradingForm/view?id="+value+"'>查看</a>";
                        }

					}
                }



                ]

            });


            if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


                $('#homeworkTable').bootstrapTable("toggleView");
            }

            $('#homeworkTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
                $('#remove').prop('disabled', ! $('#homeworkTable').bootstrapTable('getSelections').length);
                $('#view,#edit').prop('disabled', $('#homeworkTable').bootstrapTable('getSelections').length!=1);
            });

            $("#search").click("click", function() {// 绑定查询按扭
                $('#homeworkTable').bootstrapTable('refresh');
            });

            $("#reset").click("click", function() {// 绑定查询按扭
                $("#searchForm  input").val("");
                $("#searchForm  select").val("");
                $("#searchForm  .select-item").html("");
                $('#homeworkTable').bootstrapTable('refresh');
            });


        });

        function getIdSelections() {
            return $.map($("#homeworkTable").bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        function refresh(){
            $('#homeworkTable').bootstrapTable('refresh');
        }

        function edit(id){
            if(id == undefined){
                id = getIdSelections();
            }
            jp.go("${ctx}/userhomework/userHomework/gradingForm?id=" + id);
        }


	</script>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">作业信息列表</h3>
	</div>
	<div class="panel-body">
	
	<!-- 搜索 -->
	<div id="search-collapse" class="collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="userHomework" class="form form-horizontal well clearfix">
                <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="名称：">班级名称：</label>
                    <form:input path="office.name" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="名称：">学生名称：</label>
                    <form:input path="student.name" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="名称：">作业名称：</label>
                    <form:input path="homework.name" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="名称：">作业类型：</label>
                    <form:select path="homework.type"  class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('bas_material_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="名称：">作业状态：</label>
                    <form:select path="state"  class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('bas_finish_state')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="名称：">打分情况：</label>
                    <form:input path="score" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>
                <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="名称：">老师评语：</label>
                    <form:input path="comment" htmlEscape="false" maxlength="64"  class=" form-control"/>
                </div>
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>	
	</form:form>
	</div>
	</div>

	<!-- 表格 -->
	<table id="homeworkTable"   data-toolbar="#toolbar"></table>

	</div>
	</div>
	</div>
</body>
</html>