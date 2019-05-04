/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;

import com.jeeplus.core.persistence.DataEntity;

/**
 * 班级管理Entity
 * @author yangyang
 * @version 2019-04-25
 */
public class Classes extends DataEntity<Classes> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String sort;		// 排序
    private User classroomTeacher;//班主任
    private String teacherTotal;//老师总数
    private String studentTotal;//学生总数
    private String courseTotal;//课程总数
    private String homeworkTotal;//作业总数
    private String campus;//校区


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public User getClassroomTeacher() {
        return classroomTeacher;
    }

    public void setClassroomTeacher(User classroomTeacher) {
        this.classroomTeacher = classroomTeacher;
    }

    public String getTeacherTotal() {
        return teacherTotal;
    }

    public void setTeacherTotal(String teacherTotal) {
        this.teacherTotal = teacherTotal;
    }

    public String getStudentTotal() {
        return studentTotal;
    }

    public void setStudentTotal(String studentTotal) {
        this.studentTotal = studentTotal;
    }

    public String getCourseTotal() {
        return courseTotal;
    }

    public void setCourseTotal(String courseTotal) {
        this.courseTotal = courseTotal;
    }

    public String getHomeworkTotal() {
        return homeworkTotal;
    }

    public void setHomeworkTotal(String homeworkTotal) {
        this.homeworkTotal = homeworkTotal;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }
}