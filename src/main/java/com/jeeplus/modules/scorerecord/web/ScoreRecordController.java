/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scorerecord.web;

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
import com.jeeplus.modules.scorerecord.entity.ScoreRecord;
import com.jeeplus.modules.scorerecord.service.ScoreRecordService;

/**
 * 积分调整记录Controller
 * @author yangyang
 * @version 2019-05-01
 */
@Controller
@RequestMapping(value = "${adminPath}/scorerecord/scoreRecord")
public class ScoreRecordController extends BaseController {

	@Autowired
	private ScoreRecordService scoreRecordService;
	
	@ModelAttribute
	public ScoreRecord get(@RequestParam(required=false) String id) {
		ScoreRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = scoreRecordService.get(id);
		}
		if (entity == null){
			entity = new ScoreRecord();
		}
		return entity;
	}
	
	/**
	 * 积分调整记录列表页面
	 */
	@RequiresPermissions("scorerecord:scoreRecord:list")
	@RequestMapping(value = {"list", ""})
	public String list(ScoreRecord scoreRecord, Model model) {
		model.addAttribute("scoreRecord", scoreRecord);
		return "modules/scorerecord/scoreRecordList";
	}
	
		/**
	 * 积分调整记录列表数据
	 */
	@ResponseBody
	@RequiresPermissions("scorerecord:scoreRecord:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ScoreRecord scoreRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ScoreRecord> page = scoreRecordService.findPage(new Page<ScoreRecord>(request, response), scoreRecord); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑积分调整记录表单页面
	 */
	@RequiresPermissions(value={"scorerecord:scoreRecord:view","scorerecord:scoreRecord:add","scorerecord:scoreRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, ScoreRecord scoreRecord, Model model) {
		model.addAttribute("scoreRecord", scoreRecord);
		model.addAttribute("mode", mode);
		return "modules/scorerecord/scoreRecordForm";
	}

	/**
	 * 保存积分调整记录
	 */
	@ResponseBody
	@RequiresPermissions(value={"scorerecord:scoreRecord:add","scorerecord:scoreRecord:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ScoreRecord scoreRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(scoreRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		scoreRecordService.save(scoreRecord);//保存
		j.setSuccess(true);
		j.setMsg("保存积分调整记录成功");
		return j;
	}
	
	/**
	 * 删除积分调整记录
	 */
	@ResponseBody
	@RequiresPermissions("scorerecord:scoreRecord:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ScoreRecord scoreRecord) {
		AjaxJson j = new AjaxJson();
		scoreRecordService.delete(scoreRecord);
		j.setMsg("删除积分调整记录成功");
		return j;
	}
	
	/**
	 * 批量删除积分调整记录
	 */
	@ResponseBody
	@RequiresPermissions("scorerecord:scoreRecord:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			scoreRecordService.delete(scoreRecordService.get(id));
		}
		j.setMsg("删除积分调整记录成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("scorerecord:scoreRecord:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ScoreRecord scoreRecord, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "积分调整记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ScoreRecord> page = scoreRecordService.findPage(new Page<ScoreRecord>(request, response, -1), scoreRecord);
    		new ExportExcel("积分调整记录", ScoreRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出积分调整记录记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("scorerecord:scoreRecord:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ScoreRecord> list = ei.getDataList(ScoreRecord.class);
			for (ScoreRecord scoreRecord : list){
				try{
					scoreRecordService.save(scoreRecord);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条积分调整记录记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条积分调整记录记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入积分调整记录失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入积分调整记录数据模板
	 */
	@ResponseBody
	@RequiresPermissions("scorerecord:scoreRecord:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "积分调整记录数据导入模板.xlsx";
    		List<ScoreRecord> list = Lists.newArrayList(); 
    		new ExportExcel("积分调整记录数据", ScoreRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}