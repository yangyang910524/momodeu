<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#classesTable').bootstrapTable({
		 
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
               url: "${ctx}/sys/classes/classes/data",
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
                   }else if($el.data("item") == "view"){
                       view(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该信息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/sys/classes/classes/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#classesTable').bootstrapTable('refresh');
                   	  			jp.success(data.msg);
                   	  		}else{
                   	  			jp.error(data.msg);
                   	  		}
                   	  	})
                   	   
                   	});
                      
                   } 
               },
              
               onClickRow: function(row, $el){
               },
               	onShowSearch: function () {
			$("#search-collapse").slideToggle();
		},
               columns: [{
		        checkbox: true
		       
		    }
                   ,{
                       field: 'name',
                       title: '名称',
                       sortable: true,
                       sortName: 'name'
                       ,formatter:function(value, row , index){
                           value = jp.unescapeHTML(value);
                           <c:choose>
                           <c:when test="${fns:hasPermission('sys:classes:classes:edit')}">
                           return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
                           </c:when>
                           <c:when test="${fns:hasPermission('sys:classes:classes:view')}">
                           return "<a href='javascript:view(\""+row.id+"\")'>"+value+"</a>";
                           </c:when>
                           <c:otherwise>
                           return value;
                           </c:otherwise>
                           </c:choose>
                       }

                   }, {
                       field: 'campus',
                       title: '校区',
                       sortable: false,
                       formatter:function(value, row , index){
                           return jp.getDictLabel(${fns:toJson(fns:getDictList('bas_campus'))}, value, "-");
                       }
                   },{
                       field: 'level',
                       title: '课程级别',
                       sortable: false,
                       formatter:function(value, row , index){
                           return jp.getDictLabel(${fns:toJson(fns:getDictList('bae_course_level'))}, value, "-");
                       }

                   }, {
                       field: 'classroomTeacher',
                       title: '班主任',
                       sortable: false,
                       formatter:function (value, row, index) {
                          if(value){
                                 return value.name;
                          }else{
                                var result="";
                                return result
                          }
                       }
                   }, {
                       field: 'teacherTotal',
                       title: '老师人数',
                       sortable: false
                   }, {
                       field: 'studentTotal',
                       title: '学生人数',
                       sortable: false
                   }, {
                       field: 'courseTotal',
                       title: '课程总数',
                       sortable: false
                   }, {
                       field: 'homeworkTotal',
                       title: '作业总数',
                       sortable: false
                   }, {
                       field: 'id',
                       title: '操作',
                       sortable: false,
                       formatter:function (value, row, index) {
                           var result="";
                           <shiro:hasPermission name="sys:classes:classes:user">
                           result+= "&nbsp;&nbsp;<a href='${ctx}/useroffice/userOffice?officeid="+value+"'>人员管理</a>&nbsp;&nbsp;";
                           </shiro:hasPermission>

                           <shiro:hasPermission name="sys:classes:classes:course">
                           result+= "&nbsp;&nbsp;<a href='${ctx}/sys/couresoffice/couresOffice?office.id="+value+"'>课程管理</a>&nbsp;&nbsp;";
                           </shiro:hasPermission>

                           <shiro:hasPermission name="sys:classes:classes:homework">
                           result+= "&nbsp;&nbsp;<a href='${ctx}/homeworkoffice/homeworkOffice?officeid="+value+"'>作业管理</a>&nbsp;&nbsp;";
                           </shiro:hasPermission>

                           return result;
                       }
                   }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#classesTable').bootstrapTable("toggleView");
		}
	  
	  $('#classesTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#classesTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#classesTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 2,
                area: [500, 200],
                auto: true,
			    title:"导入数据",
			    content: "${ctx}/tag/importExcel" ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  jp.downloadFile('${ctx}/sys/classes/classes/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/sys/classes/classes/import', function (data) {
							if(data.success){
								jp.success(data.msg);
								refresh();
							}else{
								jp.error(data.msg);
							}
					   		jp.close(index);
						});//调用保存事件
						return false;
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		
		
	 $("#export").click(function(){//导出Excel文件
	        var searchParam = $("#searchForm").serializeJSON();
	        searchParam.pageNo = 1;
	        searchParam.pageSize = -1;
            var sortName = $('#classesTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#classesTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/sys/classes/classes/export?'+values);
	  })

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#classesTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#classesTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#classesTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该信息记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/sys/classes/classes/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#classesTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }

    //刷新列表
  function refresh(){
  	$('#classesTable').bootstrapTable('refresh');
  }
  
   function add(){
	  jp.openSaveDialog('新增信息', "${ctx}/sys/classes/classes/form",'800px', '500px');
  }


  
   function edit(id){//没有权限时，不显示确定按钮
       if(id == undefined){
	      id = getIdSelections();
	}
	jp.openSaveDialog('编辑信息', "${ctx}/sys/classes/classes/form?id=" + id, '800px', '500px');
  }
  
 function view(id){//没有权限时，不显示确定按钮
      if(id == undefined){
             id = getIdSelections();
      }
        jp.openViewDialog('查看信息', "${ctx}/sys/classes/classes/form?id=" + id, '800px', '500px');
 }



</script>