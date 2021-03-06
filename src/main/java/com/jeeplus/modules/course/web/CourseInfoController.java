/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.course.entity.CourseInfo;
import com.jeeplus.modules.course.service.CourseInfoService;
import com.jeeplus.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 课程管理Controller
 * @author yangyang
 * @version 2019-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/course/courseInfo")
public class CourseInfoController extends BaseController {

	@Autowired
	private CourseInfoService courseInfoService;
	
	@ModelAttribute
	public CourseInfo get(@RequestParam(required=false) String id) {
		CourseInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = courseInfoService.get(id);
		}
		if (entity == null){
			entity = new CourseInfo();
		}
		return entity;
	}
	
	/**
	 * 课程信息列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CourseInfo courseInfo, @ModelAttribute("parentIds") String parentIds,  HttpServletRequest request, HttpServletResponse response, Model model) {

		if(StringUtils.isNotBlank(parentIds)){
			model.addAttribute("parentIds", parentIds);
		}
		model.addAttribute("courseInfo", courseInfo);
		return "modules/course/courseInfoList";
	}

	/**
	 * 查看，增加，编辑课程信息表单页面
	 */
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, CourseInfo courseInfo, Model model) {
		if (courseInfo.getParent()!=null && StringUtils.isNotBlank(courseInfo.getParent().getId())){
			courseInfo.setParent(courseInfoService.get(courseInfo.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(courseInfo.getId())){
				CourseInfo courseInfoChild = new CourseInfo();
				courseInfoChild.setParent(new CourseInfo(courseInfo.getParent().getId()));
				List<CourseInfo> list = courseInfoService.findList(courseInfo); 
				if (list.size() > 0){
					courseInfo.setSort(list.get(list.size()-1).getSort());
					if (courseInfo.getSort() != null){
						courseInfo.setSort(courseInfo.getSort() + 30);
					}
				}
			}
		}
		if (courseInfo.getSort() == null){
			courseInfo.setSort(1);
		}
		model.addAttribute("mode", mode);
		model.addAttribute("courseInfo", courseInfo);
		return "modules/course/courseInfoForm";
	}

	/**
	 * 保存课程信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(CourseInfo courseInfo, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(courseInfo);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}

		//新增或编辑表单保存
		courseInfoService.save(courseInfo);//保存
		j.setSuccess(true);
		j.put("courseInfo", courseInfo);
		j.setMsg("保存课程信息成功");
		return j;
	}
	
	@ResponseBody
	@RequestMapping(value = "getChildren")
	public List<CourseInfo> getChildren(String parentId){
		if("-1".equals(parentId)){//如果是-1，没指定任何父节点，就从根节点开始查找
			parentId = "0";
		}
        List<CourseInfo> list = courseInfoService.getChildren(parentId);
		return list;
	}
	
	/**
	 * 删除课程信息
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(CourseInfo courseInfo) {
		AjaxJson j = new AjaxJson();
		courseInfoService.delete(courseInfo);
		j.setSuccess(true);
		j.setMsg("删除课程信息成功");
		return j;
	}

	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<CourseInfo> list = courseInfoService.findList(new CourseInfo());
		for (int i=0; i<list.size(); i++){
			CourseInfo e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				if("1".equals(e.getLevel())){
					map.put("text", "("+ DictUtils.getDictLabel(e.getState(), "bas_release_type", "未知")  +")"+e.getName());
				}else{
					map.put("text", e.getName());
				}
				if(StringUtils.isBlank(e.getParentId()) || "0".equals(e.getParentId())){
					map.put("parent", "#");
					Map<String, Object> state = Maps.newHashMap();
					state.put("opened", true);
					map.put("state", state);
				}else{
					map.put("parent", e.getParentId());
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 课程章节选择页面
	 */
	@RequestMapping(value = "openChapterSelectDialog")
	public String openChapterSelectDialog(boolean isMultiSelect, Model model,String officeid) {
		model.addAttribute("isMultiSelect", isMultiSelect);
		model.addAttribute("officeid", officeid);
		return "modules/common/chapterSelect";
	}

	@ResponseBody
	@RequestMapping(value = "chapterList")
	public Map<String, Object> chapterList(CourseInfo courseInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CourseInfo> page = courseInfoService.findChapterList(new Page<CourseInfo>(request, response), courseInfo);
		return getBootstrapData(page);
	}

	/**
	 * 发布、停用、启用课程
	 */
	@ResponseBody
	@RequestMapping(value = "release")
	public AjaxJson release(CourseInfo courseInfo) {
		AjaxJson j = new AjaxJson();
		courseInfoService.release(courseInfo);
		j.setSuccess(true);
		j.setMsg("操作成功");
		return j;
	}
}