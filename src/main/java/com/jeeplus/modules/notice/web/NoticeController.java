/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.notice.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.notice.entity.Notice;
import com.jeeplus.modules.notice.service.NoticeService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息公告Controller
 * @author yangyang
 * @version 2019-04-27
 */
@Controller
@RequestMapping(value = "${adminPath}/notice/notice")
public class NoticeController extends BaseController {

	@Autowired
	private NoticeService noticeService;
	
	@ModelAttribute
	public Notice get(@RequestParam(required=false) String id) {
		Notice entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = noticeService.get(id);
		}
		if (entity == null){
			entity = new Notice();
		}
		return entity;
	}
	
	/**
	 * 消息公告列表页面
	 */
	@RequiresPermissions("notice:notice:list")
	@RequestMapping(value = {"list", ""})
	public String list(Notice notice, Model model) {
		model.addAttribute("notice", notice);
		return "modules/notice/noticeList";
	}
	
		/**
	 * 消息公告列表数据
	 */
	@ResponseBody
	@RequiresPermissions("notice:notice:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Notice notice, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Notice> page = noticeService.findPage(new Page<Notice>(request, response), notice); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑消息公告表单页面
	 */
	@RequiresPermissions(value={"notice:notice:view","notice:notice:add","notice:notice:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, Notice notice, Model model) {
		model.addAttribute("notice", notice);
		model.addAttribute("mode", mode);
		return "modules/notice/noticeForm";
	}

	/**
	 * 保存消息公告
	 */
	@ResponseBody
	@RequiresPermissions(value={"notice:notice:add","notice:notice:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Notice notice, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(notice);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		noticeService.save(notice);//保存
		j.setSuccess(true);
		j.setMsg("保存消息公告成功");
		return j;
	}

    /**
     * 修改状态
     */
    @ResponseBody
    @RequestMapping(value = "updateState")
    public AjaxJson updateState(Notice notice, Model model) throws Exception{
        AjaxJson j = new AjaxJson();
        if("0".equals(notice.getState())){
            notice.setIssueTime(new Date());
            notice.setState("1");
        }else if("1".equals(notice.getState())){
            notice.setState("2");
        }else{
            notice.setState("1");
        }
        //新增或编辑表单保存
        noticeService.save(notice);//保存
        j.setSuccess(true);
        j.setMsg("操作成功");
        return j;
    }

	/**
	 * 删除消息公告
	 */
	@ResponseBody
	@RequiresPermissions("notice:notice:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Notice notice) {
		AjaxJson j = new AjaxJson();
		noticeService.delete(notice);
		j.setMsg("删除消息公告成功");
		return j;
	}
	
	/**
	 * 批量删除消息公告
	 */
	@ResponseBody
	@RequiresPermissions("notice:notice:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			noticeService.delete(noticeService.get(id));
		}
		j.setMsg("删除消息公告成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("notice:notice:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Notice notice, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "消息公告"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Notice> page = noticeService.findPage(new Page<Notice>(request, response, -1), notice);
    		new ExportExcel("消息公告", Notice.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出消息公告记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("notice:notice:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Notice> list = ei.getDataList(Notice.class);
			for (Notice notice : list){
				try{
					noticeService.save(notice);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条消息公告记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条消息公告记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入消息公告失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入消息公告数据模板
	 */
	@ResponseBody
	@RequiresPermissions("notice:notice:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "消息公告数据导入模板.xlsx";
    		List<Notice> list = Lists.newArrayList(); 
    		new ExportExcel("消息公告数据", Notice.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}