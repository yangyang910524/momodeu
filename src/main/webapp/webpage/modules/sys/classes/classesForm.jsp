<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>

<html>
<head>
    <title>信息管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">

        $(document).ready(function() {

        });
        function save() {
            var isValidate = jp.validateForm('#inputForm');//校验表单
            if(!isValidate){
                return false;
            }else{
                jp.loading();
                jp.post("${ctx}/sys/classes/classes/save",$('#inputForm').serialize(),function(data){
                    if(data.success){
                        jp.getParent().refresh();
                        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                        parent.layer.close(dialogIndex);
                        jp.success(data.msg)

                    }else{
                        jp.error(data.msg);
                    }
                })
            }

        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="classes" class="form-horizontal">
    <form:hidden path="id"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>校区：</label></td>
            <td class="width-35">
                <form:select path="campus" class="form-control required">
                    <form:options items="${fns:getDictList('bas_campus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
            <td class="width-35">
                <form:input path="name" htmlEscape="false"    class="form-control required" autocomplete="off"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">简述说明：</label></td>
            <td class="width-35">
                <form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control " autocomplete="off"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>