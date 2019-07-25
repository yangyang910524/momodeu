/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.hourrecord.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.hourrecord.entity.HourRecord;
import com.jeeplus.modules.hourrecord.service.HourRecordService;
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
 * 课程调整记录Controller
 * @author yangyang
 * @version 2019-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/hourrecord/hourRecord")
public class HourRecordController extends BaseController {

	@Autowired
	private HourRecordService hourRecordService;
	
	@ModelAttribute
	public HourRecord get(@RequestParam(required=false) String id) {
		HourRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = hourRecordService.get(id);
		}
		if (entity == null){
			entity = new HourRecord();
		}
		return entity;
	}
	
	/**
	 * 信息列表页面
	 */
	@RequiresPermissions("hourrecord:hourRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(HourRecord hourRecord, Model model) {
		model.addAttribute("hourRecord", hourRecord);
		return "modules/hourrecord/hourRecordList";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("hourrecord:hourRecord:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(HourRecord hourRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<HourRecord> page = hourRecordService.findPage(new Page<HourRecord>(request, response), hourRecord); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequiresPermissions(value={"hourrecord:hourRecord:view","hourrecord:hourRecord:add","hourrecord:hourRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, HourRecord hourRecord, Model model) {
		model.addAttribute("hourRecord", hourRecord);
		model.addAttribute("mode", mode);
		return "modules/hourrecord/hourRecordForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"hourrecord:hourRecord:add","hourrecord:hourRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(HourRecord hourRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(hourRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		hourRecordService.save(hourRecord);//保存
		j.setSuccess(true);
		j.setMsg("保存信息成功");
		return j;
	}
	
	/**
	 * 删除信息
	 */
	@ResponseBody
	@RequiresPermissions("hourrecord:hourRecord:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(HourRecord hourRecord) {
		AjaxJson j = new AjaxJson();
		hourRecordService.delete(hourRecord);
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 批量删除信息
	 */
	@ResponseBody
	@RequiresPermissions("hourrecord:hourRecord:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			hourRecordService.delete(hourRecordService.get(id));
		}
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("hourrecord:hourRecord:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(HourRecord hourRecord, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<HourRecord> page = hourRecordService.findPage(new Page<HourRecord>(request, response, -1), hourRecord);
    		new ExportExcel("信息", HourRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("hourrecord:hourRecord:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<HourRecord> list = ei.getDataList(HourRecord.class);
			for (HourRecord hourRecord : list){
				try{
					hourRecordService.save(hourRecord);
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
	@RequiresPermissions("hourrecord:hourRecord:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息数据导入模板.xlsx";
    		List<HourRecord> list = Lists.newArrayList(); 
    		new ExportExcel("信息数据", HourRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}