/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.information.web;

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
import com.jeeplus.modules.information.entity.Information;
import com.jeeplus.modules.information.service.InformationService;

/**
 * 图文信息Controller
 * @author yangyang
 * @version 2019-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/information/information")
public class InformationController extends BaseController {

	@Autowired
	private InformationService informationService;
	
	@ModelAttribute
	public Information get(@RequestParam(required=false) String id) {
		Information entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = informationService.get(id);
		}
		if (entity == null){
			entity = new Information();
		}
		return entity;
	}
	
	/**
	 * 图文信息列表页面
	 */
	@RequiresPermissions("information:information:list")
	@RequestMapping(value = {"list", ""})
	public String list(Information information, Model model) {
		model.addAttribute("information", information);
		return "modules/information/informationList";
	}
	
		/**
	 * 图文信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("information:information:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Information information, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Information> page = informationService.findPage(new Page<Information>(request, response), information); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑图文信息表单页面
	 */
	@RequiresPermissions(value={"information:information:view","information:information:add","information:information:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, Information information, Model model) {
		model.addAttribute("information", information);
		model.addAttribute("mode", mode);
		return "modules/information/informationForm";
	}

	/**
	 * 保存图文信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"information:information:add","information:information:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Information information, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(information);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		informationService.save(information);//保存
		j.setSuccess(true);
		j.setMsg("保存图文信息成功");
		return j;
	}
	
	/**
	 * 删除图文信息
	 */
	@ResponseBody
	@RequiresPermissions("information:information:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Information information) {
		AjaxJson j = new AjaxJson();
		informationService.delete(information);
		j.setMsg("删除图文信息成功");
		return j;
	}
	
	/**
	 * 批量删除图文信息
	 */
	@ResponseBody
	@RequiresPermissions("information:information:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			informationService.delete(informationService.get(id));
		}
		j.setMsg("删除图文信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("information:information:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Information information, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "图文信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Information> page = informationService.findPage(new Page<Information>(request, response, -1), information);
    		new ExportExcel("图文信息", Information.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出图文信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("information:information:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Information> list = ei.getDataList(Information.class);
			for (Information information : list){
				try{
					informationService.save(information);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条图文信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条图文信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入图文信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入图文信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("information:information:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "图文信息数据导入模板.xlsx";
    		List<Information> list = Lists.newArrayList(); 
    		new ExportExcel("图文信息数据", Information.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}