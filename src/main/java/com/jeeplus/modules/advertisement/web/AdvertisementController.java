/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.advertisement.web;

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
import com.jeeplus.modules.advertisement.entity.Advertisement;
import com.jeeplus.modules.advertisement.service.AdvertisementService;

/**
 * 广告信息Controller
 * @author yangyang
 * @version 2019-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/advertisement/advertisement")
public class AdvertisementController extends BaseController {

	@Autowired
	private AdvertisementService advertisementService;
	
	@ModelAttribute
	public Advertisement get(@RequestParam(required=false) String id) {
		Advertisement entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = advertisementService.get(id);
		}
		if (entity == null){
			entity = new Advertisement();
		}
		return entity;
	}
	
	/**
	 * 广告信息列表页面
	 */
	@RequiresPermissions("advertisement:advertisement:list")
	@RequestMapping(value = {"list", ""})
	public String list(Advertisement advertisement, Model model) {
		model.addAttribute("advertisement", advertisement);
		return "modules/advertisement/advertisementList";
	}
	
		/**
	 * 广告信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("advertisement:advertisement:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Advertisement advertisement, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Advertisement> page = advertisementService.findPage(new Page<Advertisement>(request, response), advertisement); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑广告信息表单页面
	 */
	@RequiresPermissions(value={"advertisement:advertisement:view","advertisement:advertisement:add","advertisement:advertisement:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, Advertisement advertisement, Model model) {
		model.addAttribute("advertisement", advertisement);
		model.addAttribute("mode", mode);
		return "modules/advertisement/advertisementForm";
	}

	/**
	 * 保存广告信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"advertisement:advertisement:add","advertisement:advertisement:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Advertisement advertisement, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(advertisement);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		advertisementService.save(advertisement);//保存
		j.setSuccess(true);
		j.setMsg("保存广告信息成功");
		return j;
	}
	
	/**
	 * 删除广告信息
	 */
	@ResponseBody
	@RequiresPermissions("advertisement:advertisement:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Advertisement advertisement) {
		AjaxJson j = new AjaxJson();
		advertisementService.delete(advertisement);
		j.setMsg("删除广告信息成功");
		return j;
	}
	
	/**
	 * 批量删除广告信息
	 */
	@ResponseBody
	@RequiresPermissions("advertisement:advertisement:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			advertisementService.delete(advertisementService.get(id));
		}
		j.setMsg("删除广告信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("advertisement:advertisement:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Advertisement advertisement, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "广告信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Advertisement> page = advertisementService.findPage(new Page<Advertisement>(request, response, -1), advertisement);
    		new ExportExcel("广告信息", Advertisement.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出广告信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("advertisement:advertisement:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Advertisement> list = ei.getDataList(Advertisement.class);
			for (Advertisement advertisement : list){
				try{
					advertisementService.save(advertisement);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条广告信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条广告信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入广告信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入广告信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("advertisement:advertisement:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "广告信息数据导入模板.xlsx";
    		List<Advertisement> list = Lists.newArrayList(); 
    		new ExportExcel("广告信息数据", Advertisement.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}