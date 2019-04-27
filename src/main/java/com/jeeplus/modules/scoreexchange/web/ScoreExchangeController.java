/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scoreexchange.web;

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
import com.jeeplus.modules.scoreexchange.entity.ScoreExchange;
import com.jeeplus.modules.scoreexchange.service.ScoreExchangeService;

/**
 * 积分兑换信息Controller
 * @author yangyang
 * @version 2019-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/scoreexchange/scoreExchange")
public class ScoreExchangeController extends BaseController {

	@Autowired
	private ScoreExchangeService scoreExchangeService;
	
	@ModelAttribute
	public ScoreExchange get(@RequestParam(required=false) String id) {
		ScoreExchange entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = scoreExchangeService.get(id);
		}
		if (entity == null){
			entity = new ScoreExchange();
		}
		return entity;
	}
	
	/**
	 * 积分兑换信息列表页面
	 */
	@RequiresPermissions("scoreexchange:scoreExchange:list")
	@RequestMapping(value = {"list", ""})
	public String list(ScoreExchange scoreExchange, Model model) {
		model.addAttribute("scoreExchange", scoreExchange);
		return "modules/scoreexchange/scoreExchangeList";
	}
	
		/**
	 * 积分兑换信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("scoreexchange:scoreExchange:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ScoreExchange scoreExchange, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ScoreExchange> page = scoreExchangeService.findPage(new Page<ScoreExchange>(request, response), scoreExchange); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑积分兑换信息表单页面
	 */
	@RequiresPermissions(value={"scoreexchange:scoreExchange:view","scoreexchange:scoreExchange:add","scoreexchange:scoreExchange:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, ScoreExchange scoreExchange, Model model) {
		model.addAttribute("scoreExchange", scoreExchange);
		model.addAttribute("mode", mode);
		return "modules/scoreexchange/scoreExchangeForm";
	}

	/**
	 * 保存积分兑换信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"scoreexchange:scoreExchange:add","scoreexchange:scoreExchange:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ScoreExchange scoreExchange, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(scoreExchange);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		scoreExchangeService.save(scoreExchange);//保存
		j.setSuccess(true);
		j.setMsg("保存积分兑换信息成功");
		return j;
	}
	
	/**
	 * 删除积分兑换信息
	 */
	@ResponseBody
	@RequiresPermissions("scoreexchange:scoreExchange:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ScoreExchange scoreExchange) {
		AjaxJson j = new AjaxJson();
		scoreExchangeService.delete(scoreExchange);
		j.setMsg("删除积分兑换信息成功");
		return j;
	}
	
	/**
	 * 批量删除积分兑换信息
	 */
	@ResponseBody
	@RequiresPermissions("scoreexchange:scoreExchange:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			scoreExchangeService.delete(scoreExchangeService.get(id));
		}
		j.setMsg("删除积分兑换信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("scoreexchange:scoreExchange:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ScoreExchange scoreExchange, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "积分兑换信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ScoreExchange> page = scoreExchangeService.findPage(new Page<ScoreExchange>(request, response, -1), scoreExchange);
    		new ExportExcel("积分兑换信息", ScoreExchange.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出积分兑换信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("scoreexchange:scoreExchange:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ScoreExchange> list = ei.getDataList(ScoreExchange.class);
			for (ScoreExchange scoreExchange : list){
				try{
					scoreExchangeService.save(scoreExchange);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条积分兑换信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条积分兑换信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入积分兑换信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入积分兑换信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("scoreexchange:scoreExchange:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "积分兑换信息数据导入模板.xlsx";
    		List<ScoreExchange> list = Lists.newArrayList(); 
    		new ExportExcel("积分兑换信息数据", ScoreExchange.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}