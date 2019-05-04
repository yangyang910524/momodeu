/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.sys.entity.CouresOffice;
import com.jeeplus.modules.sys.entity.HomeworkOffice;
import com.jeeplus.modules.sys.service.HomeworkOfficeService;
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

/**
 * 作业-班级关联Controller
 * @author yangyang
 * @version 2019-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/homeworkoffice/homeworkOffice")
public class HomeworkOfficeController extends BaseController {

	@Autowired
	private HomeworkOfficeService homeworkOfficeService;
	
	@ModelAttribute
	public HomeworkOffice get(@RequestParam(required=false) String id) {
		HomeworkOffice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = homeworkOfficeService.get(id);
		}
		if (entity == null){
			entity = new HomeworkOffice();
		}
		return entity;
	}
	
	/**
	 * 信息列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(HomeworkOffice homeworkOffice, Model model) {
		model.addAttribute("homeworkOffice", homeworkOffice);
		return "modules/sys/homeworkoffice/homeworkOfficeList";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(HomeworkOffice homeworkOffice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<HomeworkOffice> page = homeworkOfficeService.findPage(new Page<HomeworkOffice>(request, response), homeworkOffice); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, HomeworkOffice homeworkOffice, Model model) {
		model.addAttribute("homeworkOffice", homeworkOffice);
		model.addAttribute("mode", mode);
		return "modules/sys/homeworkoffice/homeworkOfficeForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(HomeworkOffice homeworkOffice, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(homeworkOffice);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		homeworkOfficeService.save(homeworkOffice);//保存
		j.setSuccess(true);
		j.setMsg("保存信息成功");
		return j;
	}
	
	/**
	 * 删除信息
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(HomeworkOffice homeworkOffice) {
		AjaxJson j = new AjaxJson();
		homeworkOfficeService.delete(homeworkOffice);
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
			homeworkOfficeService.delete(homeworkOfficeService.get(id));
		}
		j.setMsg("删除信息成功");
		return j;
	}

	/**
	 * 绑定作业
	 */
	@ResponseBody
	@RequestMapping(value = "addHomework")
	public AjaxJson addHomework(String ids,String officeid) {
		AjaxJson j = new AjaxJson();
		HomeworkOffice homeworkOffice=new HomeworkOffice();
		homeworkOffice.setOfficeid(officeid);
		String idArray[] =ids.split(",");
		List<HomeworkOffice> list=null;
		for(String id : idArray){
			homeworkOffice.setId(null);
			homeworkOffice.setHomeworkid(id);
			list=homeworkOfficeService.findList(homeworkOffice);
			if(list.size()>0){
				continue;
			}
			homeworkOfficeService.save(homeworkOffice);
		}
		j.setMsg("添加成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export")
    public AjaxJson exportFile(HomeworkOffice homeworkOffice, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<HomeworkOffice> page = homeworkOfficeService.findPage(new Page<HomeworkOffice>(request, response, -1), homeworkOffice);
    		new ExportExcel("信息", HomeworkOffice.class).setDataList(page.getList()).write(response, fileName).dispose();
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
			List<HomeworkOffice> list = ei.getDataList(HomeworkOffice.class);
			for (HomeworkOffice homeworkOffice : list){
				try{
					homeworkOfficeService.save(homeworkOffice);
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
    		List<HomeworkOffice> list = Lists.newArrayList(); 
    		new ExportExcel("信息数据", HomeworkOffice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}