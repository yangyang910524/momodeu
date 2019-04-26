/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.TreeEntity;

/**
 * 课程管理Entity
 * @author yangyang
 * @version 2019-04-26
 */
public class CourseInfo extends TreeEntity<CourseInfo> {
	
	private static final long serialVersionUID = 1L;
	
	private List<CourseData> courseDataList = Lists.newArrayList();		// 子表列表
	
	public CourseInfo() {
		super();
	}

	public CourseInfo(String id){
		super(id);
	}

	public  CourseInfo getParent() {
			return parent;
	}
	
	@Override
	public void setParent(CourseInfo parent) {
		this.parent = parent;
		
	}
	
	public List<CourseData> getCourseDataList() {
		return courseDataList;
	}

	public void setCourseDataList(List<CourseData> courseDataList) {
		this.courseDataList = courseDataList;
	}
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}