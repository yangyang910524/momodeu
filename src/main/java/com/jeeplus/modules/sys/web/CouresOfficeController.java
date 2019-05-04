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
import com.jeeplus.modules.course.entity.CourseInfo;
import com.jeeplus.modules.sys.entity.CouresOffice;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.service.CouresOfficeService;
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
 * 课程-班级关联Controller
 * @author yangyang
 * @version 2019-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/couresoffice/couresOffice")
public class CouresOfficeController extends BaseController {

	@Autowired
	private CouresOfficeService couresOfficeService;
	
	@ModelAttribute
	public CouresOffice get(@RequestParam(required=false) String id) {
		CouresOffice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = couresOfficeService.get(id);
		}
		if (entity == null){
			entity = new CouresOffice();
		}
		return entity;
	}
	
	/**
	 * 信息列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CouresOffice couresOffice, Model model) {
		model.addAttribute("couresOffice", couresOffice);
		return "modules/sys/couresoffice/couresOfficeList";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(CouresOffice couresOffice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CouresOffice> page = couresOfficeService.findPage(new Page<CouresOffice>(request, response), couresOffice); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequestMapping(value = "form")
	public String form(CouresOffice couresOffice, Model model) {
		model.addAttribute("couresOffice", couresOffice);
		return "modules/sys/couresoffice/couresOfficeForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(CouresOffice couresOffice, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(couresOffice);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		couresOfficeService.save(couresOffice);//保存
		j.setSuccess(true);
		j.setMsg("保存信息成功");
		return j;
	}
	
	/**
	 * 删除信息
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(CouresOffice couresOffice) {
		AjaxJson j = new AjaxJson();
		couresOfficeService.delete(couresOffice);
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
			couresOfficeService.delete(couresOfficeService.get(id));
		}
		j.setMsg("删除信息成功");
		return j;
	}

    /**
     * 添加课程信息
     */
    @ResponseBody
    @RequestMapping(value = "addCoures")
    public AjaxJson addCoures(String ids,String officeid) {
        AjaxJson j = new AjaxJson();
        CouresOffice couresOffice=new CouresOffice();
        Office o=new Office();
        o.setId(officeid);
        couresOffice.setOffice(o);
        CourseInfo c=new CourseInfo();
        String idArray[] =ids.split(",");
        List<CouresOffice> list=null;
        for(String id : idArray){
            couresOffice.setId(null);
            c.setId(id);
            couresOffice.setCourseInfo(c);
            list=couresOfficeService.findList(couresOffice);
            if(list.size()>0){
                continue;
            }
            couresOfficeService.save(couresOffice);
        }
        j.setMsg("添加成功");
        return j;
    }

	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export")
    public AjaxJson exportFile(CouresOffice couresOffice, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<CouresOffice> page = couresOfficeService.findPage(new Page<CouresOffice>(request, response, -1), couresOffice);
    		new ExportExcel("信息", CouresOffice.class).setDataList(page.getList()).write(response, fileName).dispose();
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
			List<CouresOffice> list = ei.getDataList(CouresOffice.class);
			for (CouresOffice couresOffice : list){
				try{
					couresOfficeService.save(couresOffice);
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
    		List<CouresOffice> list = Lists.newArrayList(); 
    		new ExportExcel("信息数据", CouresOffice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}