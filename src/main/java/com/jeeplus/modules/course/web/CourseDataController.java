/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.course.entity.CourseData;
import com.jeeplus.modules.course.service.CourseDataService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 课程内容Controller
 * @author yangyang
 * @version 2019-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/course/courseData")
public class CourseDataController extends BaseController {

	@Autowired
	private CourseDataService courseDataService;
	
	@ModelAttribute
	public CourseData get(@RequestParam(required=false) String id) {
		CourseData entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = courseDataService.get(id);
		}
		if (entity == null){
			entity = new CourseData();
		}
		return entity;
	}
	
	/**
	 * 课程内容列表页面
	 */
	@RequiresPermissions("course:courseData:list")
	@RequestMapping(value = {"list", ""})
	public String list(CourseData courseData, Model model) {
		model.addAttribute("courseData", courseData);
		return "modules/course/courseDataList";
	}
	
		/**
	 * 课程内容列表数据
	 */
	@ResponseBody
	@RequiresPermissions("course:courseData:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(CourseData courseData, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CourseData> page = courseDataService.findPage(new Page<CourseData>(request, response), courseData); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑课程内容表单页面
	 */
	@RequiresPermissions(value={"course:courseData:view","course:courseData:add","course:courseData:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, CourseData courseData, Model model) {
		model.addAttribute("courseData", courseData);
		model.addAttribute("mode", mode);
		return "modules/course/courseDataForm";
	}

	/**
	 * 保存课程内容
	 */
	@ResponseBody
	@RequiresPermissions(value={"course:courseData:add","course:courseData:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(CourseData courseData, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(courseData);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		courseDataService.save(courseData);//保存
		j.setSuccess(true);
		j.setMsg("保存课程内容成功");
		return j;
	}
	
	/**
	 * 删除课程内容
	 */
	@ResponseBody
	@RequiresPermissions("course:courseData:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(CourseData courseData) {
		AjaxJson j = new AjaxJson();
		if("0".equals(courseData.getFather().getState())){
            courseDataService.delete(courseData);
            j.setSuccess(true);
            j.setMsg("删除课程内容成功");
        }else{
		    j.setSuccess(false);
            j.setMsg("已发布课程不容许删除课程内容");
        }

		return j;
	}
	
	/**
	 * 批量删除课程内容
	 */
	@ResponseBody
	@RequiresPermissions("course:courseData:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
        CourseData temp=null;
		for(String id : idArray){
            temp = courseDataService.get(id);
            if("0".equals(temp.getFather().getState())){
                courseDataService.delete(temp);
            }
		}
		j.setMsg("删除课程内容成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("course:courseData:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(CourseData courseData, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "课程内容"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CourseData> page = courseDataService.findPage(new Page<CourseData>(request, response, -1), courseData);
    		new ExportExcel("课程内容", CourseData.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出课程内容记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("course:courseData:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CourseData> list = ei.getDataList(CourseData.class);
			for (CourseData courseData : list){
				try{
					courseDataService.save(courseData);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条课程内容记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条课程内容记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入课程内容失败！失败信息："+e.getMessage());
		}
		return j;
    }

	/**
	 * 下载导入课程内容数据模板
	 */
	@ResponseBody
	@RequiresPermissions("course:courseData:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "课程内容数据导入模板.xlsx";
    		List<CourseData> list = Lists.newArrayList();
    		new ExportExcel("课程内容数据", CourseData.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

    /**
     * 课程内容列表页面
     */
    @RequestMapping(value = "openCourseSelectDialog")
    public String openCourseSelectDialog(boolean isMultiSelect, Model model,String officeid) {
        model.addAttribute("isMultiSelect", isMultiSelect);
        model.addAttribute("officeid", officeid);
        return "modules/common/courseSelect";
    }

    @ResponseBody
    @RequestMapping(value = "courseList")
    public Map<String, Object> courseList(CourseData courseData, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<CourseData> page = courseDataService.findCourseList(new Page<CourseData>(request, response), courseData);
        return getBootstrapData(page);
    }
}