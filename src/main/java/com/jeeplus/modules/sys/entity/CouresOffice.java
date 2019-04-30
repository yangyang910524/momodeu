/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.course.entity.CourseInfo;

/**
 * 课程-班级关联Entity
 * @author yangyang
 * @version 2019-04-26
 */
public class CouresOffice extends DataEntity<CouresOffice> {
	
	private static final long serialVersionUID = 1L;
    private String state;		// 学习状态
    private Office office;//班级信息
    private CourseInfo courseInfo;//课程信息

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }
}