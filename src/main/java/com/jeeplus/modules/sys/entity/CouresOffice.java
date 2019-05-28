/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.course.entity.CourseData;
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
    private CourseData courseData;//课程-内容
    private CourseInfo courseInfo;//课程-章节
    private CourseInfo father;//课程
    private String playCount;//学生播放课程次数（学生不存在-1，未播放过0，其他显示播放次数）
    private String userid;//学生id

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

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

    public CourseData getCourseData() {
        return courseData;
    }

    public void setCourseData(CourseData courseData) {
        this.courseData = courseData;
    }

    public CourseInfo getFather() {
        return father;
    }

    public void setFather(CourseInfo father) {
        this.father = father;
    }
}