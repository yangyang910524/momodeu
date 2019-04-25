/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.sys.entity.UserOffice;
import com.jeeplus.modules.sys.service.UserOfficeService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 用户-班级关联Controller
 * @author yangyang
 * @version 2019-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/useroffice/userOffice")
public class UserOfficeController extends BaseController {

	@Autowired
	private UserOfficeService userOfficeService;
	
	@ModelAttribute
	public UserOffice get(@RequestParam(required=false) String id) {
		UserOffice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userOfficeService.get(id);
		}
		if (entity == null){
			entity = new UserOffice();
		}
		return entity;
	}
	
	/**
	 * 信息列表页面
	 */
	@RequiresPermissions("useroffice:userOffice:list")
	@RequestMapping(value = {"list", ""})
	public String list(UserOffice userOffice, Model model) {
		model.addAttribute("userOffice", userOffice);
		return "modules/useroffice/userOfficeList";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("useroffice:userOffice:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(UserOffice userOffice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserOffice> page = userOfficeService.findPage(new Page<UserOffice>(request, response), userOffice); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequiresPermissions(value={"useroffice:userOffice:view","useroffice:userOffice:add","useroffice:userOffice:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(UserOffice userOffice, Model model) {
		model.addAttribute("userOffice", userOffice);
		return "modules/useroffice/userOfficeForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"useroffice:userOffice:add","useroffice:userOffice:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(UserOffice userOffice, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(userOffice);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		userOfficeService.save(userOffice);//保存
		j.setSuccess(true);
		j.setMsg("保存信息成功");
		return j;
	}
	
	/**
	 * 删除信息
	 */
	@ResponseBody
	@RequiresPermissions("useroffice:userOffice:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(UserOffice userOffice) {
		AjaxJson j = new AjaxJson();
		userOfficeService.delete(userOffice);
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 批量删除信息
	 */
	@ResponseBody
	@RequiresPermissions("useroffice:userOffice:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			userOfficeService.delete(userOfficeService.get(id));
		}
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("useroffice:userOffice:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(UserOffice userOffice, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserOffice> page = userOfficeService.findPage(new Page<UserOffice>(request, response, -1), userOffice);
    		new ExportExcel("信息", UserOffice.class).setDataList(page.getList()).write(response, fileName).dispose();
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
	@RequiresPermissions("useroffice:userOffice:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<UserOffice> list = ei.getDataList(UserOffice.class);
			for (UserOffice userOffice : list){
				try{
					userOfficeService.save(userOffice);
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
	@RequiresPermissions("useroffice:userOffice:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息数据导入模板.xlsx";
    		List<UserOffice> list = Lists.newArrayList(); 
    		new ExportExcel("信息数据", UserOffice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}