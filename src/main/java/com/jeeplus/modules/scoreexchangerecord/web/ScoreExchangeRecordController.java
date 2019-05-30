/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scoreexchangerecord.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.scoreexchangerecord.entity.ScoreExchangeRecord;
import com.jeeplus.modules.scoreexchangerecord.service.ScoreExchangeRecordService;
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
 * 积分兑换记录Controller
 * @author yangyang
 * @version 2019-05-30
 */
@Controller
@RequestMapping(value = "${adminPath}/scoreexchangerecord/scoreExchangeRecord")
public class ScoreExchangeRecordController extends BaseController {

	@Autowired
	private ScoreExchangeRecordService scoreExchangeRecordService;
	
	@ModelAttribute
	public ScoreExchangeRecord get(@RequestParam(required=false) String id) {
		ScoreExchangeRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = scoreExchangeRecordService.get(id);
		}
		if (entity == null){
			entity = new ScoreExchangeRecord();
		}
		return entity;
	}
	
	/**
	 * 信息列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(ScoreExchangeRecord scoreExchangeRecord, Model model) {
		model.addAttribute("scoreExchangeRecord", scoreExchangeRecord);
		return "modules/scoreexchangerecord/scoreExchangeRecordList";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(ScoreExchangeRecord scoreExchangeRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ScoreExchangeRecord> page = scoreExchangeRecordService.findPage(new Page<ScoreExchangeRecord>(request, response), scoreExchangeRecord); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, ScoreExchangeRecord scoreExchangeRecord, Model model) {
		model.addAttribute("scoreExchangeRecord", scoreExchangeRecord);
		model.addAttribute("mode", mode);
		return "modules/scoreexchangerecord/scoreExchangeRecordForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(ScoreExchangeRecord scoreExchangeRecord, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(scoreExchangeRecord);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		scoreExchangeRecordService.save(scoreExchangeRecord);//保存
		j.setSuccess(true);
		j.setMsg("保存信息成功");
		return j;
	}
	
	/**
	 * 删除信息
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(ScoreExchangeRecord scoreExchangeRecord) {
		AjaxJson j = new AjaxJson();
		scoreExchangeRecordService.delete(scoreExchangeRecord);
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
			scoreExchangeRecordService.delete(scoreExchangeRecordService.get(id));
		}
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ScoreExchangeRecord scoreExchangeRecord, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ScoreExchangeRecord> page = scoreExchangeRecordService.findPage(new Page<ScoreExchangeRecord>(request, response, -1), scoreExchangeRecord);
    		new ExportExcel("信息", ScoreExchangeRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
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
			List<ScoreExchangeRecord> list = ei.getDataList(ScoreExchangeRecord.class);
			for (ScoreExchangeRecord scoreExchangeRecord : list){
				try{
					scoreExchangeRecordService.save(scoreExchangeRecord);
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
    		List<ScoreExchangeRecord> list = Lists.newArrayList(); 
    		new ExportExcel("信息数据", ScoreExchangeRecord.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}