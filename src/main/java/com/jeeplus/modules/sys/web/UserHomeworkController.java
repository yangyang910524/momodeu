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
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.UserHomework;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.service.UserHomeworkService;
import com.jeeplus.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 用户-作业关联Controller
 * @author yangyang
 * @version 2019-04-28
 */
@Controller
@RequestMapping(value = "${adminPath}/userhomework/userHomework")
public class UserHomeworkController extends BaseController {

	@Autowired
	private UserHomeworkService userHomeworkService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private SystemService systemService;
	
	@ModelAttribute
	public UserHomework get(@RequestParam(required=false) String id) {
		UserHomework entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = userHomeworkService.get(id);
		}
		if (entity == null){
			entity = new UserHomework();
		}
		return entity;
	}
	
	/**
	 * 信息列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(UserHomework userHomework, Model model) {
		model.addAttribute("userHomework", userHomework);
		return "modules/userhomework/userHomeworkList";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(UserHomework userHomework, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserHomework> page = userHomeworkService.findPage(new Page<UserHomework>(request, response), userHomework); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, UserHomework userHomework, Model model) {
		model.addAttribute("userHomework", userHomework);
		model.addAttribute("mode", mode);
		return "modules/userhomework/userHomeworkForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(UserHomework userHomework, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(userHomework);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		userHomeworkService.save(userHomework);//保存
		j.setSuccess(true);
		j.setMsg("保存信息成功");
		return j;
	}

	/**
	 * 查看，增加，编辑信息表单页面
	 */
	@RequestMapping(value = "gradingForm/{mode}")
	public String gradingForm(@PathVariable String mode, UserHomework userHomework, Model model) {
		model.addAttribute("userHomework", userHomework);
        model.addAttribute("mode", mode);
		return "modules/homework/userHomeworkForm";
	}

	/**
	 * 保存信息
	 */
	@ResponseBody
	@RequestMapping(value = "gradingSave")
	public AjaxJson gradingSave(UserHomework userHomework, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(userHomework);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
        userHomework.setState("2");
        userHomework.setTeacher(UserUtils.getUser());
		userHomeworkService.save(userHomework);//保存
        //把分数给学生加上
        User student=userMapper.get(userHomework.getStudent().getId());
        student.setScore(Integer.valueOf(student.getScore())+Integer.valueOf(userHomework.getScore()));
        systemService.saveUser(student);
		j.setSuccess(true);
		j.setMsg("打分成功");
		return j;
	}
	
	/**
	 * 删除信息
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(UserHomework userHomework) {
		AjaxJson j = new AjaxJson();
		userHomeworkService.delete(userHomework);
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
			userHomeworkService.delete(userHomeworkService.get(id));
		}
		j.setMsg("删除信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export")
    public AjaxJson exportFile(UserHomework userHomework, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<UserHomework> page = userHomeworkService.findPage(new Page<UserHomework>(request, response, -1), userHomework);
    		new ExportExcel("信息", UserHomework.class).setDataList(page.getList()).write(response, fileName).dispose();
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
			List<UserHomework> list = ei.getDataList(UserHomework.class);
			for (UserHomework userHomework : list){
				try{
					userHomeworkService.save(userHomework);
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
    		List<UserHomework> list = Lists.newArrayList(); 
    		new ExportExcel("信息数据", UserHomework.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

    /**
     * 作业打分列表
     */
    @RequestMapping(value ="homeworkGradingList")
    public String homeworkGradingList(UserHomework userHomework, Model model) {
        model.addAttribute("userHomework", userHomework);
        return "modules/homework/homeworkGradingList";
    }

    /**
     * 作业打分列表数据
     */
    @ResponseBody
    @RequestMapping(value = "homeworkGradingListData")
    public Map<String, Object> homeworkGradingListData(UserHomework userHomework, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user=UserUtils.getUser();
    	if("2".equals(user.getUserType())){
            userHomework.setTeacher(user);
		}
    	Page<UserHomework> page = userHomeworkService.findPage(new Page<UserHomework>(request, response), userHomework);
        return getBootstrapData(page);
    }


    /**
     * 查看，增加，编辑信息表单页面
     */
    @RequestMapping(value = "playStudentVideo")
    public String playStudentVideo(UserHomework userHomework, Model model) {
        model.addAttribute("userHomework", userHomework);
        return "modules/homework/playStudentVideo";
    }
}