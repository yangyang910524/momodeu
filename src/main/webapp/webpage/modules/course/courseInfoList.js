	<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	    var $courseInfoTreeTable=null;  
		$(document).ready(function() {
			$courseInfoTreeTable=$('#courseInfoTreeTable').treeTable({  
		    	   theme:'vsStyle',	           
					expandLevel : 2,
					column:0,
					checkbox: false,
		            url:'${ctx}/course/courseInfo/getChildren?parentId=',  
		            callback:function(item) { 
		            	 var treeTableTpl= $("#courseInfoTreeTableTpl").html();
		            	 item.dict = {};
						item.dict.titleType = jp.getDictLabel(${fns:toJson(fns:getDictList('bas_course_title_type'))}, item.titleType, "-");
						item.dict.level = jp.getDictLabel(${fns:toJson(fns:getDictList('bae_course_level'))}, item.level, "-");
						item.dict.state = jp.getDictLabel(${fns:toJson(fns:getDictList('bas_release_type'))}, item.state, "-");
	           	  var result = laytpl(treeTableTpl).render({
								  row: item
							});
		                return result;                   
		            },  
		            beforeClick: function($courseInfoTreeTable, id) { 
		                //异步获取数据 这里模拟替换处理  
		                $courseInfoTreeTable.refreshPoint(id);  
		            },  
		            beforeExpand : function($courseInfoTreeTable, id) {   
		            },  
		            afterExpand : function($courseInfoTreeTable, id) {  
		            },  
		            beforeClose : function($courseInfoTreeTable, id) {    
		            	
		            }  
		        });
		        
		        $courseInfoTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点
		       
		});
		
		function del(con,id){  
			jp.confirm('确认要删除课程信息吗？', function(){
				jp.loading();
	       	  	$.get("${ctx}/course/courseInfo/delete?id="+id, function(data){
	       	  		if(data.success){
	       	  			$courseInfoTreeTable.del(id);
	       	  			jp.success(data.msg);
	       	  		}else{
	       	  			jp.error(data.msg);
	       	  		}
	       	  	})
	       	   
       		});
	
		} 
		
		function add(){//新增
			jp.go('${ctx}/course/courseInfo/form/add');
		}
		function edit(id){//编辑
			jp.go('${ctx}/course/courseInfo/form/edit?id='+id);
		}
		function view(id){//查看
			jp.go('${ctx}/course/courseInfo/form/view?id='+id);
		}
		function addChild(id){//添加下级机构
			jp.go('${ctx}/course/courseInfo/form/add?parent.id='+id);
		}
		function refresh(){//刷新
			var index = jp.loading("正在加载，请稍等...");
			$courseInfoTreeTable.refresh();
			jp.close(index);
		}
		function release(id,state) {
			var msg="";
			if(state=="1"){
				msg="确认要发布该课程？";
                state="1";
			}else if(state=="2"){
                msg="确认要停用该课程？";
                state="2";
            }else if(state=="3"){
                msg="确认要启用该课程？";
                state="1";
            }
			jp.confirm(msg, function(){
				jp.loading();
				jp.get("${ctx}/course/courseInfo/release?id="+id+"&state="+state, function(data){
					if(data.success){
                        refresh();
						jp.success(data.msg);
					}else{
						jp.error(data.msg);
					}
				})

			})
        }
</script>
<script type="text/html" id="courseInfoTreeTableTpl">
			<td>
				<c:choose>
					  <c:when test="${fns:hasPermission('course:courseInfo:edit')}">
						<a  href="${ctx}/course/courseInfo/form/edit?id={{d.row.id}}">
							{{d.row.name === undefined ? "": d.row.name}}
						</a>
					  </c:when>
					  <c:when test="${fns:hasPermission('course:courseInfo:view')}">
						<a  href="${ctx}/course/courseInfo/form/view?id={{d.row.id}}">
								{{d.row.name === undefined ? "": d.row.name}}
						</a>
					  </c:when>
					  <c:otherwise>
							{{d.row.name === undefined ? "": d.row.name}}
					  </c:otherwise>
				</c:choose>
			</td>
			<td>
				<img onclick="jp.showPic('{{d.row.cover}}')" height="20px" src="{{d.row.cover}}" style="{{d.row.cover===undefined?'display:none;':''}}"/>
			</td>
			<td>
							{{d.row.dict.level === undefined ? "": d.row.dict.level}}
			</td>
			<td>
							{{d.row.dict.state === undefined ? "": d.row.dict.state}}
			</td>
			<td>
							{{d.row.sort === undefined ? "": d.row.sort}}
			</td>
			<td>
							{{d.row.remarks === undefined ? "": d.row.remarks}}
			</td>
			<td>
				<div class="btn-group">
			 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
						<i class="fa fa-cog"></i>
						<span class="fa fa-chevron-down"></span>
					</button>
				  <ul class="dropdown-menu" role="menu">
					<shiro:hasPermission name="course:courseInfo:view">
						<li><a href="${ctx}/course/courseInfo/form/view?id={{d.row.id}}"><i class="fa fa-search-plus"></i> 查看</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="course:courseInfo:edit">
		   				<li><a href="${ctx}/course/courseInfo/form/edit?id={{d.row.id}}"><i class="fa fa-edit"></i> 修改</a></li>
		   			</shiro:hasPermission>
		   			<shiro:hasPermission name="course:courseInfo:del">
		   				<li><a  onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="course:courseInfo:add">
						{{d.row.titleType==='1'?'<li><a href="${ctx}/course/courseInfo/form/add?parent.id='+d.row.id+'"><i class="fa fa-plus"></i> 添加章节</a></li>':''}}
					</shiro:hasPermission>
					<shiro:hasPermission name="course:courseInfo:release">
						<li style="{{d.row.state==='0'?"":"display:none;"}}"><a onclick="release('{{d.row.id}}','1')"><i class="fa fa-plus"></i> 发布</a></li>
						<li style="{{d.row.state==='1'?"":"display:none;"}}"><a onclick="release('{{d.row.id}}','2')"><i class="fa fa-plus"></i> 停用</a></li>
        				<li style="{{d.row.state==='2'?"":"display:none;"}}"><a onclick="release('{{d.row.id}}','3')"><i class="fa fa-plus"></i> 启用</a></li>
					</shiro:hasPermission>
				  </ul>
				</div>
			</td>
	</script>