/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coursedataplayrecord.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.coursedataplayrecord.entity.CourseDataPlayRecord;
import com.jeeplus.modules.coursedataplayrecord.service.CourseDataPlayRecordService;
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
 * 课程内容播放记录Controller
 * @author yangyang
 * @version 2019-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/coursedataplayrecord/courseDataPlayRecord")
public class CourseDataPlayRecordController extends BaseController {

	@Autowired
	private CourseDataPlayRecordService courseDataPlayRecordService;
	
	@ModelAttribute
	public CourseDataPlayRecord get(@RequestParam(required=false) String id) {
		CourseDataPlayRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = courseDataPlayRecordService.get(id);
		}
		if (entity == null){
			entity = new CourseDataPlayRecord();
		}
		return entity;
	}
	
	/**
	 * 信息列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CourseDataPlayRecord courseDataPlayRecord, Model model) {
		model.addAttribute("courseDataPlayRecord", courseDataPlayRecord);
		return "modules/coursedataplayrecord/courseDataPlayRecordList";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(CourseDataPlayRecord courseDataPlayRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CourseDataPlayRecord> page = courseDataPlayRecordService.findPage(new Page<CourseDataPlayRecord>(request, response), courseDataPlayRecord); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, CourseDataPlayRecord courseDataPlayRecord, Model model) {
		model.addAttribute("courseDataPlayRecord", courseDataPlayRecord);
		model.addAttribute("mode", mode);
		return "modules/coursedataplayrecord/courseDataPlayRecordForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(CourseDataPlayRecord courseDataPlayRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(courseDataPlayRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		courseDataPlayRecordService.save(courseDataPlayRecord);//保存
		j.setSuccess(true);
		j.setMsg("保存信息成功");
		return j;
	}
	
	/**
	 * 删除信息
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(CourseDataPlayRecord courseDataPlayRecord) {
		AjaxJson j = new AjaxJson();
		courseDataPlayRecordService.delete(courseDataPlayRecord);
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 批量删除信息
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			courseDataPlayRecordService.delete(courseDataPlayRecordService.get(id));
		}
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export")
    public AjaxJson exportFile(CourseDataPlayRecord courseDataPlayRecord, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CourseDataPlayRecord> page = courseDataPlayRecordService.findPage(new Page<CourseDataPlayRecord>(request, response, -1), courseDataPlayRecord);
    		new ExportExcel("信息", CourseDataPlayRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
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
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<CourseDataPlayRecord> list = ei.getDataList(CourseDataPlayRecord.class);
			for (CourseDataPlayRecord courseDataPlayRecord : list){
				try{
					courseDataPlayRecordService.save(courseDataPlayRecord);
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
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息数据导入模板.xlsx";
    		List<CourseDataPlayRecord> list = Lists.newArrayList(); 
    		new ExportExcel("信息数据", CourseDataPlayRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}