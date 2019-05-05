/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.publiccours.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.publiccours.entity.PublicCourse;
import com.jeeplus.modules.publiccours.service.PublicCourseService;

/**
 * 公共课程管理Controller
 * @author yangyang
 * @version 2019-05-05
 */
@Controller
@RequestMapping(value = "${adminPath}/publiccours/publicCourse")
public class PublicCourseController extends BaseController {

	@Autowired
	private PublicCourseService publicCourseService;
	
	@ModelAttribute
	public PublicCourse get(@RequestParam(required=false) String id) {
		PublicCourse entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = publicCourseService.get(id);
		}
		if (entity == null){
			entity = new PublicCourse();
		}
		return entity;
	}
	
	/**
	 * 课程信息列表页面
	 */
	@RequiresPermissions("publiccours:publicCourse:list")
	@RequestMapping(value = {"list", ""})
	public String list(PublicCourse publicCourse, Model model) {
		model.addAttribute("publicCourse", publicCourse);
		return "modules/publiccours/publicCourseList";
	}
	
		/**
	 * 课程信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("publiccours:publicCourse:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PublicCourse publicCourse, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PublicCourse> page = publicCourseService.findPage(new Page<PublicCourse>(request, response), publicCourse); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑课程信息表单页面
	 */
	@RequiresPermissions(value={"publiccours:publicCourse:view","publiccours:publicCourse:add","publiccours:publicCourse:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, PublicCourse publicCourse, Model model) {
		model.addAttribute("publicCourse", publicCourse);
		model.addAttribute("mode", mode);
		return "modules/publiccours/publicCourseForm";
	}

	/**
	 * 保存课程信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"publiccours:publicCourse:add","publiccours:publicCourse:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(PublicCourse publicCourse, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(publicCourse);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		publicCourseService.save(publicCourse);//保存
		j.setSuccess(true);
		j.setMsg("保存课程信息成功");
		return j;
	}
	
	/**
	 * 删除课程信息
	 */
	@ResponseBody
	@RequiresPermissions("publiccours:publicCourse:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PublicCourse publicCourse) {
		AjaxJson j = new AjaxJson();
		publicCourseService.delete(publicCourse);
		j.setMsg("删除课程信息成功");
		return j;
	}
	
	/**
	 * 批量删除课程信息
	 */
	@ResponseBody
	@RequiresPermissions("publiccours:publicCourse:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			publicCourseService.delete(publicCourseService.get(id));
		}
		j.setMsg("删除课程信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("publiccours:publicCourse:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(PublicCourse publicCourse, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "课程信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PublicCourse> page = publicCourseService.findPage(new Page<PublicCourse>(request, response, -1), publicCourse);
    		new ExportExcel("课程信息", PublicCourse.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出课程信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("publiccours:publicCourse:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PublicCourse> list = ei.getDataList(PublicCourse.class);
			for (PublicCourse publicCourse : list){
				try{
					publicCourseService.save(publicCourse);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条课程信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条课程信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入课程信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入课程信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("publiccours:publicCourse:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "课程信息数据导入模板.xlsx";
    		List<PublicCourse> list = Lists.newArrayList(); 
    		new ExportExcel("课程信息数据", PublicCourse.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}