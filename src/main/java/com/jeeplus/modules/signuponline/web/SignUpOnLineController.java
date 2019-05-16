/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.signuponline.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.signuponline.entity.SignUpOnLine;
import com.jeeplus.modules.signuponline.service.SignUpOnLineService;
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
 * 在线报名Controller
 * @author yangyang
 * @version 2019-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/signuponline/signUpOnLine")
public class SignUpOnLineController extends BaseController {

	@Autowired
	private SignUpOnLineService signUpOnLineService;
	
	@ModelAttribute
	public SignUpOnLine get(@RequestParam(required=false) String id) {
		SignUpOnLine entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = signUpOnLineService.get(id);
		}
		if (entity == null){
			entity = new SignUpOnLine();
		}
		return entity;
	}
	
	/**
	 * 操作信息列表页面
	 */
	@RequiresPermissions("signuponline:signUpOnLine:list")
	@RequestMapping(value = {"list", ""})
	public String list(SignUpOnLine signUpOnLine, Model model) {
		model.addAttribute("signUpOnLine", signUpOnLine);
		return "modules/signuponline/signUpOnLineList";
	}
	
		/**
	 * 操作信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("signuponline:signUpOnLine:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SignUpOnLine signUpOnLine, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SignUpOnLine> page = signUpOnLineService.findPage(new Page<SignUpOnLine>(request, response), signUpOnLine); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑操作信息表单页面
	 */
	@RequiresPermissions(value={"signuponline:signUpOnLine:view","signuponline:signUpOnLine:add","signuponline:signUpOnLine:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, SignUpOnLine signUpOnLine, Model model) {
		model.addAttribute("signUpOnLine", signUpOnLine);
		model.addAttribute("mode", mode);
		return "modules/signuponline/signUpOnLineForm";
	}

	/**
	 * 保存操作信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"signuponline:signUpOnLine:add","signuponline:signUpOnLine:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SignUpOnLine signUpOnLine, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(signUpOnLine);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		signUpOnLineService.save(signUpOnLine);//保存
		j.setSuccess(true);
		j.setMsg("保存操作信息成功");
		return j;
	}
	
	/**
	 * 删除操作信息
	 */
	@ResponseBody
	@RequiresPermissions("signuponline:signUpOnLine:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SignUpOnLine signUpOnLine) {
		AjaxJson j = new AjaxJson();
		signUpOnLineService.delete(signUpOnLine);
		j.setMsg("删除操作信息成功");
		return j;
	}
	
	/**
	 * 批量删除操作信息
	 */
	@ResponseBody
	@RequiresPermissions("signuponline:signUpOnLine:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			signUpOnLineService.delete(signUpOnLineService.get(id));
		}
		j.setMsg("删除操作信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("signuponline:signUpOnLine:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(SignUpOnLine signUpOnLine, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "操作信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<SignUpOnLine> page = signUpOnLineService.findPage(new Page<SignUpOnLine>(request, response, -1), signUpOnLine);
    		new ExportExcel("操作信息", SignUpOnLine.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出操作信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("signuponline:signUpOnLine:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SignUpOnLine> list = ei.getDataList(SignUpOnLine.class);
			for (SignUpOnLine signUpOnLine : list){
				try{
					signUpOnLineService.save(signUpOnLine);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条操作信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条操作信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入操作信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入操作信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("signuponline:signUpOnLine:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "操作信息数据导入模板.xlsx";
    		List<SignUpOnLine> list = Lists.newArrayList(); 
    		new ExportExcel("操作信息数据", SignUpOnLine.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}