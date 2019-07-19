<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#advertisementTable').bootstrapTable({
		 
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
               url: "${ctx}/advertisement/advertisement/data",
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
                        jp.confirm('确认要删除该广告信息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/advertisement/advertisement/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#advertisementTable').bootstrapTable('refresh');
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
		       
		    },{
                   field: 'title',
                   title: '标题',
                   sortable: true,
                   sortName: 'title'
               }

                   ,{
                       field: 'picture',
                       title: '图片',
                       sortable: true,
                       sortName: 'picture',
                       formatter:function(value, row , index){
                           var result="";
                           if(/\.(gif|jpg|jpeg|png|GIF|JPG|JPEG|PNG)$/.test(value)){
                               result = '<img   onclick="jp.showPic(\''+value+'\')"'+' height="50px" src="'+value+'">';
                           }else if(/\.(mp3|mp4|MP3|MP4)$/.test(value)){
                               url="${ctx}/sys/file/playVideo?url=" +value;
                               result = '<a onclick="jp.playVideo(\''+url+'\')">播放视频</a>';
                           }else if(value==null||value==""||value==undefined){
                               result="-"
                           }else{
                               result = "<a href=\""+value+"\" url=\""+value+"\" target=\"_blank\">查看资料</a>";
                           }
                           return result;
                       }

                   },{
                       field: 'content',
                       title: '内容',
                       sortable: true,
                       sortName: 'content',
                       formatter:function(value, row , index){
                           var result="";
                           if(/\.(gif|jpg|jpeg|png|GIF|JPG|JPEG|PNG)$/.test(value)){
                               result = '<img   onclick="jp.showPic(\''+value+'\')"'+' height="50px" src="'+value+'">';
                           }else if(/\.(mp3|mp4|MP3|MP4)$/.test(value)){
                               url="${ctx}/sys/file/playVideo?url=" +value;
                               result = '<a onclick="jp.playVideo(\''+url+'\')">播放视频</a>';
                           }else if(value==null||value==""||value==undefined){
                               result="-"
                           }else{
                               result = "<a href=\""+value+"\" url=\""+value+"\" target=\"_blank\">查看资料</a>";
                           }
                           return result;
                       }

                   },{
                       field: 'video',
                       title: '视频',
                       sortable: true,
                       sortName: 'content',
                       formatter:function(value, row , index){
                           var result="";
                           if(/\.(gif|jpg|jpeg|png|GIF|JPG|JPEG|PNG)$/.test(value)){
                               result = '<img   onclick="jp.showPic(\''+value+'\')"'+' height="50px" src="'+value+'">';
                           }else if(/\.(mp3|mp4|MP3|MP4)$/.test(value)){
                               url="${ctx}/sys/file/playVideo?url=" +value;
                               result = '<a onclick="jp.playVideo(\''+url+'\')">播放视频</a>';
                           }else if(value==null||value==""||value==undefined){
                               result="-"
                           }else{
                               result = "<a href=\""+value+"\" url=\""+value+"\" target=\"_blank\">查看资料</a>";
                           }
                           return result;
                       }
                   }
           ,{
               field: 'type',
               title: '类型',
               sortable: true,
               sortName: 'type',
               formatter:function(value, row , index){
                   return jp.getDictLabel(${fns:toJson(fns:getDictList('bas_advertisement_type'))}, value, "-");
               }
           }
			,{
		        field: 'state',
		        title: '状态',
		        sortable: true,
		        sortName: 'state',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('bas_release_type'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'issueTime',
		        title: '发布时间',
		        sortable: true,
		        sortName: 'issueTime'
		       
		    },{
               field: 'remarks',
               title: '备注信息',
               sortable: true,
               sortName: 'remarks'
            },{
               field: 'id',
               title: '操作',
               sortable: true,
               sortName: 'remarks',
               formatter:function(value, row , index){
                   <shiro:hasPermission name="advertisement:advertisement:edit">
                   if(row.state=='0'){
                       return "<a onclick='updateState(\""+value+"\",\"0\")'>发布</a>";
                   }else if(row.state=='1'){
                       return "<a onclick='updateState(\""+value+"\",\"1\")'>停用</a>";
                   }else if(row.state=='2'){
                       return "<a onclick='updateState(\""+value+"\",\"2\")'>启用</a>";
                   }
                   </shiro:hasPermission>
                   return "";
               }
           }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#advertisementTable').bootstrapTable("toggleView");
		}
	  
	  $('#advertisementTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#advertisementTable').bootstrapTable('getSelections').length);
            $('#view,#edit').prop('disabled', $('#advertisementTable').bootstrapTable('getSelections').length!=1);
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
					 jp.downloadFile('${ctx}/advertisement/advertisement/import/template');
				  },
			    btn2: function(index, layero){
				        var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
						iframeWin.contentWindow.importExcel('${ctx}/advertisement/advertisement/import', function (data) {
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
            var sortName = $('#advertisementTable').bootstrapTable("getOptions", "none").sortName;
            var sortOrder = $('#advertisementTable').bootstrapTable("getOptions", "none").sortOrder;
            var values = "";
            for(var key in searchParam){
                values = values + key + "=" + searchParam[key] + "&";
            }
            if(sortName != undefined && sortOrder != undefined){
                values = values + "orderBy=" + sortName + " "+sortOrder;
            }

			jp.downloadFile('${ctx}/advertisement/advertisement/export?'+values);
	  })

		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#advertisementTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#advertisementTable').bootstrapTable('refresh');
		});
		
		$('#beginIssueTime').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		$('#endIssueTime').datetimepicker({
			 format: "YYYY-MM-DD"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#advertisementTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该广告信息记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/advertisement/advertisement/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#advertisementTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function refresh(){
  	$('#advertisementTable').bootstrapTable('refresh');
  }
  function add(){
		jp.go("${ctx}/advertisement/advertisement/form/add");
	}

  function edit(id){
	  if(id == undefined){
		  id = getIdSelections();
	  }
	  jp.go("${ctx}/advertisement/advertisement/form/edit?id=" + id);
  }

  function view(id) {
      if(id == undefined){
          id = getIdSelections();
      }
      jp.go("${ctx}/advertisement/advertisement/form/view?id=" + id);
  }

    function updateState(id,state) {
        jp.loading();
        jp.get("${ctx}/advertisement/advertisement/updateState?id=" +id+"&state="+state, function(data){
            if(data.success){
                $('#advertisementTable').bootstrapTable('refresh');
                jp.success(data.msg);
            }else{
                jp.error(data.msg);
            }
        })
    }
</script>