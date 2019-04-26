/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.material.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.material.entity.Material;
import com.jeeplus.modules.material.service.MaterialService;
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
 * 材料管理Controller
 * @author yangyang
 * @version 2019-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/material/material")
public class MaterialController extends BaseController {

	@Autowired
	private MaterialService materialService;
	
	@ModelAttribute
	public Material get(@RequestParam(required=false) String id) {
		Material entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = materialService.get(id);
		}
		if (entity == null){
			entity = new Material();
		}
		return entity;
	}
	
	/**
	 * 材料信息列表页面
	 */
	@RequiresPermissions("material:material:list")
	@RequestMapping(value = {"list", ""})
	public String list(Material material, Model model) {
		model.addAttribute("material", material);
		return "modules/material/materialList";
	}
	
		/**
	 * 材料信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("material:material:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Material material, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Material> page = materialService.findPage(new Page<Material>(request, response), material); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑材料信息表单页面
	 */
	@RequiresPermissions(value={"material:material:view","material:material:add","material:material:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, Material material, Model model) {
		model.addAttribute("material", material);
		model.addAttribute("mode", mode);
		return "modules/material/materialForm";
	}

	/**
	 * 保存材料信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"material:material:add","material:material:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Material material, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(material);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		materialService.save(material);//保存
		j.setSuccess(true);
		j.setMsg("保存材料信息成功");
		return j;
	}
	
	/**
	 * 删除材料信息
	 */
	@ResponseBody
	@RequiresPermissions("material:material:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Material material) {
		AjaxJson j = new AjaxJson();
		materialService.delete(material);
		j.setMsg("删除材料信息成功");
		return j;
	}
	
	/**
	 * 批量删除材料信息
	 */
	@ResponseBody
	@RequiresPermissions("material:material:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			materialService.delete(materialService.get(id));
		}
		j.setMsg("删除材料信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("material:material:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Material material, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "材料信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Material> page = materialService.findPage(new Page<Material>(request, response, -1), material);
    		new ExportExcel("材料信息", Material.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出材料信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("material:material:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Material> list = ei.getDataList(Material.class);
			for (Material material : list){
				try{
					materialService.save(material);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条材料信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条材料信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入材料信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入材料信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("material:material:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "材料信息数据导入模板.xlsx";
    		List<Material> list = Lists.newArrayList(); 
    		new ExportExcel("材料信息数据", Material.class, 1).setDataList(list).write(response, fileName).dispose();
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
    @RequestMapping(value = "openMaterialSelectDialog")
    public String openCourseSelectDialog(boolean isMultiSelect, Model model) {
        model.addAttribute("isMultiSelect", isMultiSelect);
        return "modules/common/materialSelect";
    }
}