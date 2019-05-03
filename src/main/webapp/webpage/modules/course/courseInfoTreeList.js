<%@ page contentType="text/html;charset=UTF-8" %>
	<script>
		$(document).ready(function() {
			var to = false;
			$('#search_q').keyup(function () {
				if(to) { clearTimeout(to); }
				to = setTimeout(function () {
					var v = $('#search_q').val();
					$('#courseInfojsTree').jstree(true).search(v);
				}, 250);
			});
			$('#courseInfojsTree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/course/courseInfo/treeData",
						"dataType" : "json" 
					}
				},
				"conditionalselect" : function (node, event) {
					return false;
				},
				'plugins': ["contextmenu", 'types', 'wholerow', "search"],
				"contextmenu": {
					"items": function (node) {
						var tmp = $.jstree.defaults.contextmenu.items();
						delete tmp.create.action;
						delete tmp.rename.action;
						tmp.rename = null;
						tmp.create = {
							"label": "添加章节",
							"action": function (data) {
                                var inst = jQuery.jstree.reference(data.reference),
                                    obj = inst.get_node(data.reference);
								if(obj.parent=="#"){
                                    jp.openSaveDialog('添加章节信息', '${ctx}/course/courseInfo/form?parent.id=' + obj.id + "&parent.name=" + obj.text, '800px', '500px');
								}else{
                                    layer.msg("请在课程标题下添加章节");
								}
							}
						};
						tmp.remove = {
							"label": "删除",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								jp.confirm('确认要删除吗？', function(){
									jp.loading();
									$.get("${ctx}/course/courseInfo/delete?id="+obj.id, function(data){
										if(data.success){
											$('#courseInfojsTree').jstree("refresh");
											jp.success(data.msg);
										}else{
											jp.error(data.msg);
										}
									})

								});
							}
						}
						tmp.ccp = {
							"label": "编辑",
							"action": function (data) {
								var inst = jQuery.jstree.reference(data.reference),
									obj = inst.get_node(data.reference);
								var parentId = inst.get_parent(data.reference);
								var parent = inst.get_node(parentId);
								jp.openSaveDialog('编辑信息', '${ctx}/course/courseInfo/form?id=' + obj.id, '800px', '500px');
							}
						}
						return tmp;
					}

				},
				"types":{
					'default' : { 'icon' : 'fa fa-folder' },
					'1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
					'3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				}

			}).bind("activate_node.jstree", function (obj, e) {
				var node = $('#courseInfojsTree').jstree(true).get_selected(true)[0];
				var opt = {
					silent: true,
					query:{
						'courseInfo.id':node.id
					}
				};
				$("#courseInfoId").val(node.id);
				$("#courseInfoName").val(node.text);
				$('#courseDataTable').bootstrapTable('refresh',opt);
			}).on('loaded.jstree', function() {
				$("#courseInfojsTree").jstree('open_all');
			});
		});

		function refreshTree() {
	            $('#courseInfojsTree').jstree("refresh");
	        }
	</script>