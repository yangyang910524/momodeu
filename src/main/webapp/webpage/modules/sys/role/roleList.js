<%@ page contentType="text/html;charset=UTF-8" %>
<script>
		$(document).ready(function() {
			$('#table').bootstrapTable({
					  //请求方法
						method: 'post',
						//类型json
						dataType: "json",
						contentType: "application/x-www-form-urlencoded",
		                 //是否显示行间隔色
		                striped: true,
		                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
		                cache: false,
				        showSearch: false,
						//显示刷新按钮
						showRefresh: false,
						//显示切换手机试图按钮
						showToggle: false,
						//显示 内容列下拉框
						showColumns: false,
						//显示切换分页按钮
		                //是否显示分页（*）  
		                pagination: false,
		                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
		                url: "${ctx}/sys/role/data",
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
						onShowSearch: function () {
							$("#search-collapse").slideToggle();
						},
		                //分页方式：client客户端分页，server服务端分页（*）
		                sidePagination: "server",
		                contextMenuTrigger:"right",//pc端 按右键弹出菜单
		                contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
		                contextMenu: '#context-menu',
		                onContextMenuItem: function(row, $el){
		                    if($el.data("item") == "edit"){
		                    	edit(row.id);
		                    } else if($el.data("item") == "delete"){
		                        del(row.id);
		                       
		                    } 
		                },
		                columns: [ {
                            field: '',
                            title: '序号',
                            formatter:function(value, row, index){
                                return index+1;
                            }

                        },{
                            field: 'name',
                            title: '角色名称',
                            formatter:function(value, row, index){
                                return '<a  href="#" onclick="jp.openViewDialog(\'查看角色\', \'${ctx}/sys/role/form?id='+row.id+'\',\'800px\', \'500px\')">'+value+'</a>'
                            }

                        }, {
	                        field: 'operate',
	                        title: '操作',
	                        align: 'center',
	                        events: {
	            		        'click .auth': function (e, value, row, index) {
	            		        	jp.openSaveDialog('权限设置', '${ctx}/sys/role/auth?id=' + row.id,'350px', '700px');
	            		        }
	            		    },
	                        formatter:  function operateFormatter(value, row, index) {
	            		        return [
	        						<shiro:hasPermission name="sys:role:assign">
	        							'<a href="#" class="auth"  title="权限设置">权限设置</i> </a>',
	        						</shiro:hasPermission>
	            		        ].join('');
	            		    }
	                    }]
					
					});
				
				  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端，默认关闭tab
					  $('#table').bootstrapTable("toggleView");
					}
				  

		
				  $("#search").click("click", function() {// 绑定查询按扭
					  $('#table').bootstrapTable('refresh');
					});
				  $("#reset").click("click", function() {// 绑定查询按扭
					  $("#searchForm  input").val("");
					  $("#searchForm  select").val("");
					  $('#table').bootstrapTable('refresh');
					});
				  
			});
		
		  function getIdSelections() {
		        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
		            return row.id
		        });
		    }
		  
		  function add(){
			  jp.openSaveDialog('新建角色', '${ctx}/sys/role/form','800px', '500px')
		  }
		  
		  function edit(id){
			  if(!id){
					id = getIdSelections();
				}
			  jp.openSaveDialog('编辑角色', "${ctx}/sys/role/form?id=" + id,'800px', '500px')
			  
		  }
		  function del(ids){
				if(!ids){
					ids = getIdSelections();
				}
				jp.confirm('确认要删除选中角色吗？',  function(){
					jp.loading();
             	  	jp.get("${ctx}/sys/role/delete?ids=" + ids, function(data){
             	  		if(data.success){
             	  			$('#table').bootstrapTable('refresh');
            	  			jp.success(data.msg);
            	  		}else{
            	  			jp.error(data.msg);
            	  		}
             	  	})
				})
		  }

		  function refresh() {
              $('#table').bootstrapTable('refresh');
          }

          function auth(id) {
              if(!id){
                  id = getIdSelections();
              }
              jp.openSaveDialog('权限设置', '${ctx}/sys/role/auth?id=' + id,'350px', '700px');
          }
	</script>