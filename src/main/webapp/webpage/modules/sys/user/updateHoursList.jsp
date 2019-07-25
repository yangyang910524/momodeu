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
            $('#hourTable').bootstrapTable({

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
                url: "${ctx}/sys/user/updateHoursListData",
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
                    field: 'photo',
                    title: '头像',
                    formatter:function(value, row , index){
                        if(value ==''){
                            return '<img height="40px" src="${ctxStatic}/common/images/flat-avatar.png">';
                        }else{
                            return '<img   onclick="jp.showPic(\''+value+'\')"'+' height="40px" src="'+value+'">';
                        }

                    }

                }, {
                    field: 'userType',
                    title: '用户类型',
                    sortable: true,
                    formatter:function(value, row , index){
                        if(value=='1'){
                            return "管理员";
                        }else if(value=='2'){
                            return "老师";
                        }else if(value=='3'){
                            return "学生";
                        }else{
                            return "";
                        }
                    }
                }, {
                    field: 'loginName',
                    title: '登录名',
                    sortable: true

                }, {
                    field: 'name',
                    title: '姓名',
                    sortable: true
                }, {
                    field: 'englishName',
                    title: '英文名',
                    sortable: true
                }, {
                    field: 'mobile',
                    title: '手机',
                    sortable: true
                }, {
                    field: 'hours',
                    title: '当前课时',
                    sortable: true
                },{
                    field: 'id',
                    title: '操作',
                    sortable: false,
					formatter:function(value, row , index){
                        return "<a onclick='reduceHours(\"" + value + "\")'>课时扣除</a>";
					}
                }



                ]

            });


            if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端


                $('#hourTable').bootstrapTable("toggleView");
            }

            $('#hourTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
                $('#remove').prop('disabled', ! $('#hourTable').bootstrapTable('getSelections').length);
                $('#view,#edit').prop('disabled', $('#hourTable').bootstrapTable('getSelections').length!=1);
            });

            $("#search").click("click", function() {// 绑定查询按扭
                $('#hourTable').bootstrapTable('refresh');
            });

            $("#reset").click("click", function() {// 绑定查询按扭
                $("#searchForm  input").val("");
                $("#searchForm  select").val("");
                $("#searchForm  .select-item").html("");
                $('#hourTable').bootstrapTable('refresh');
            });


        });

        function getIdSelections() {
            return $.map($("#hourTable").bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        function refresh(){
            $('#hourTable').bootstrapTable('refresh');
        }

        function edit(id){
            if(id == undefined){
                id = getIdSelections();
            }
            jp.go("${ctx}/userhour/userhour/gradingForm?id=" + id);
        }

        function updateHours(id) {
            jp.openSaveDialog('课时调整', "${ctx}/sys/user/updateHoursForm?id=" + id,'800px', '680px');
        }

        function reduceHours(id) {
            jp.confirm('确认要扣除课程吗？', function(){
                jp.loading();
                jp.get("${ctx}/sys/user/reduceHours?id=" + id, function(data){
                    if(data.success){
                        $('#hourTable').bootstrapTable('refresh');
                        jp.success(data.msg);
                    }else{
                        jp.error(data.msg);
                    }
                })

            })
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
			<form:form id="searchForm" modelAttribute="user" class="form form-horizontal well clearfix">

                <div class="col-xs-12 col-sm-6 col-md-4">
                    <label class="label-item single-overflow pull-left" title="名称：">学生名称：</label>
                    <form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control"/>
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
	<table id="hourTable"   data-toolbar="#toolbar"></table>

	</div>
	</div>
	</div>
</body>
</html>