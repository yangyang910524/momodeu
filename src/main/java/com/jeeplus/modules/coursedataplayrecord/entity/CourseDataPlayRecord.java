/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coursedataplayrecord.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.course.entity.CourseData;

/**
 * 课程内容播放记录Entity
 * @author yangyang
 * @version 2019-05-16
 */
public class CourseDataPlayRecord extends DataEntity<CourseDataPlayRecord> {
	
	private static final long serialVersionUID = 1L;
	private CourseData courseData;		// 课程内容

    public CourseData getCourseData() {
        return courseData;
    }

    public void setCourseData(CourseData courseData) {
        this.courseData = courseData;
    }
}