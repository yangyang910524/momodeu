/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.Classes;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.service.ClassesService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 班级管理Controller
 * @author yangyang
 * @version 2019-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/classes/classes")
public class ClassesController extends BaseController {

	@Autowired
	private ClassesService classesService;
	
	@ModelAttribute
	public Classes get(@RequestParam(required=false) String id) {
		Classes entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = classesService.get(id);
		}
		if (entity == null){
			entity = new Classes();
		}
		return entity;
	}
	
	/**
	 * 信息列表页面
	 */
	@RequiresPermissions("sys:classes:classes:list")
	@RequestMapping(value = {"list", ""})
	public String list(Classes classes, Model model) {
		model.addAttribute("classes", classes);
		return "modules/sys/classes/classesList";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("sys:classes:classes:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Classes classes, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user=UserUtils.getUser();
		if("2".equals(user.getUserType())){
			classes.setTeacherid(user.getId());
		}
		Page<Classes> page = classesService.findPage(new Page<Classes>(request, response), classes); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequiresPermissions(value={"sys:classes:classes:view","sys:classes:classes:add","sys:classes:classes:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Classes classes, Model model) {
		model.addAttribute("classes", classes);
		return "modules/sys/classes/classesForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"sys:classes:classes:add","sys:classes:classes:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Classes classes, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
        if("0749d5b41e5847959cdc944da591efea".equals(classes.getId())){
            j.setSuccess(false);
            j.setMsg("管理处不能修改！");
            return j;
        }
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(classes);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		classesService.save(classes);//保存
		j.setSuccess(true);
		j.setMsg("保存信息成功");
		return j;
	}
	
	/**
	 * 删除信息
	 */
	@ResponseBody
	@RequiresPermissions("sys:classes:classes:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Classes classes) {
		AjaxJson j = new AjaxJson();
        if("0749d5b41e5847959cdc944da591efea".equals(classes.getId())){
            j.setSuccess(false);
            j.setMsg("管理处不能删除！");
            return j;
        }
		classesService.delete(classes);
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 批量删除信息
	 */
	@ResponseBody
	@RequiresPermissions("sys:classes:classes:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
            if("0749d5b41e5847959cdc944da591efea".equals(id)){
                continue;
            }
			classesService.delete(classesService.get(id));
		}
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("sys:classes:classes:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Classes classes, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Classes> page = classesService.findPage(new Page<Classes>(request, response, -1), classes);
    		new ExportExcel("信息", Classes.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("sys:classes:classes:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Classes> list = ei.getDataList(Classes.class);
			for (Classes classes : list){
				try{
					classesService.save(classes);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("sys:classes:classes:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息数据导入模板.xlsx";
    		List<Classes> list = Lists.newArrayList(); 
    		new ExportExcel("信息数据", Classes.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}