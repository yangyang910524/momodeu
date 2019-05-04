/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 课程内容Entity
 * @author yangyang
 * @version 2019-04-26
 */
public class CourseData extends DataEntity<CourseData> {
	
	private static final long serialVersionUID = 1L;
	private String data;		// 资料
	private CourseInfo courseInfo;		// 章节
	private CourseInfo father;			//课程

	public CourseData() {
		super();
	}

	public CourseData(String id){
		super(id);
	}

	public CourseData(CourseInfo courseInfo){
		this.courseInfo = courseInfo;
	}

	@ExcelField(title="资料", align=2, sort=7)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public CourseInfo getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(CourseInfo courseInfo) {
		this.courseInfo = courseInfo;
	}

	public CourseInfo getFather() {
		return father;
	}

	public void setFather(CourseInfo father) {
		this.father = father;
	}
}