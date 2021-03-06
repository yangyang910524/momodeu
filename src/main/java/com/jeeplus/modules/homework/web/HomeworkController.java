/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.homework.entity.Homework;
import com.jeeplus.modules.homework.service.HomeworkService;
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
 * 作业管理Controller
 * @author yangyang
 * @version 2019-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/homework/homework")
public class HomeworkController extends BaseController {

	@Autowired
	private HomeworkService homeworkService;
	
	@ModelAttribute
	public Homework get(@RequestParam(required=false) String id) {
		Homework entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = homeworkService.get(id);
		}
		if (entity == null){
			entity = new Homework();
		}
		return entity;
	}
	
	/**
	 * 作业信息列表页面
	 */
	@RequiresPermissions("homework:homework:list")
	@RequestMapping(value = {"list", ""})
	public String list(Homework homework, Model model) {
		model.addAttribute("homework", homework);
		return "modules/homework/homeworkList";
	}
	
		/**
	 * 作业信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("homework:homework:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Homework homework, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Homework> page = homeworkService.findPage(new Page<Homework>(request, response), homework); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑作业信息表单页面
	 */
	@RequiresPermissions(value={"homework:homework:view","homework:homework:add","homework:homework:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, Homework homework, Model model) {
		model.addAttribute("homework", homework);
		model.addAttribute("mode", mode);
		return "modules/homework/homeworkForm";
	}

	/**
	 * 保存作业信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"homework:homework:add","homework:homework:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Homework homework, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(homework);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		if(!"1".equals(homework.getType())){
			homework.setData2("");
		}
		//新增或编辑表单保存
		homeworkService.save(homework);//保存
		j.setSuccess(true);
		j.setMsg("保存作业信息成功");
		return j;
	}
	
	/**
	 * 删除作业信息
	 */
	@ResponseBody
	@RequiresPermissions("homework:homework:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Homework homework) {
		AjaxJson j = new AjaxJson();
        if("0".equals(homework.getState())){
            homeworkService.delete(homework);
            j.setSuccess(true);
            j.setMsg("删除课程内容成功");
        }else{
            j.setSuccess(false);
            j.setMsg("已发布作业不容许删除");
        }
		j.setMsg("删除作业信息成功");
		return j;
	}
	
	/**
	 * 批量删除作业信息
	 */
	@ResponseBody
	@RequiresPermissions("homework:homework:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		Homework temp=null;
		for(String id : idArray){
            temp=homeworkService.get(id);
            if("0".equals(temp.getState())){
                homeworkService.delete(homeworkService.get(id));
            }
		}
        j.setSuccess(true);
		j.setMsg("删除作业信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("homework:homework:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Homework homework, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "作业信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Homework> page = homeworkService.findPage(new Page<Homework>(request, response, -1), homework);
    		new ExportExcel("作业信息", Homework.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出作业信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("homework:homework:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Homework> list = ei.getDataList(Homework.class);
			for (Homework homework : list){
				try{
					homeworkService.save(homework);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条作业信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条作业信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入作业信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入作业信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("homework:homework:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "作业信息数据导入模板.xlsx";
    		List<Homework> list = Lists.newArrayList(); 
    		new ExportExcel("作业信息数据", Homework.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

	/**
	 * 作业信息列表页面
	 */
	@RequestMapping(value = "openHomeworkSelectDialog")
	public String homeworkList(boolean isMultiSelect, Model model,String officeid) {
		model.addAttribute("isMultiSelect", isMultiSelect);
		model.addAttribute("officeid", officeid);
		return "modules/common/homeworkSelect";
	}

	/**
	 * 作业信息列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "homeworkList")
	public Map<String, Object> homeworkList(Homework homework, HttpServletRequest request, HttpServletResponse response, Model model) {
        homework.setState("1");
		Page<Homework> page = homeworkService.findHomework(new Page<Homework>(request, response), homework);
		return getBootstrapData(page);
	}

    /**
     * 修改状态
     */
    @ResponseBody
    @RequestMapping(value = "updateState")
    public AjaxJson updateState(Homework homework, Model model) throws Exception{
        AjaxJson j = new AjaxJson();
        if("0".equals(homework.getState())){
            homework.setState("1");
        }else if("1".equals(homework.getState())){
            homework.setState("2");
        }else{
            homework.setState("1");
        }
        //新增或编辑表单保存
        homeworkService.save(homework);//保存
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }
}