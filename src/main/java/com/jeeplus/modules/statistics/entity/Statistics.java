/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.statistics.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.homework.entity.Homework;
import com.jeeplus.modules.sys.entity.Office;
import com.jeeplus.modules.sys.entity.User;

/**
 * 数据统计Entity
 * @author yangyang
 * @version 2019-04-29
 */
public class Statistics extends DataEntity<Statistics> {
	
	private static final long serialVersionUID = 1L;
	private Office office;//班级信息
	private User user;	// 学生信息
	private Homework homework;// 分数
	private User teacher;//老师
    private String beginTime;
    private String endTime;
    private String homeworkFinishToal;
    private String homeworkNotFinishToal;
    private String courseDataPlayRecordToal;
    private String finishToal;
    private String homeworkType;//作业类型

    public String getCourseDataPlayRecordToal() {
        return courseDataPlayRecordToal;
    }

    public void setCourseDataPlayRecordToal(String courseDataPlayRecordToal) {
        this.courseDataPlayRecordToal = courseDataPlayRecordToal;
    }

    public String getFinishToal() {
        return finishToal;
    }

    public void setFinishToal(String finishToal) {
        this.finishToal = finishToal;
    }

    public String getHomeworkType() {
        return homeworkType;
    }

    public void setHomeworkType(String homeworkType) {
        this.homeworkType = homeworkType;
    }

    public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Homework getHomework() {
		return homework;
	}

	public void setHomework(Homework homework) {
		this.homework = homework;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getHomeworkFinishToal() {
        return homeworkFinishToal;
    }

    public void setHomeworkFinishToal(String homeworkFinishToal) {
        this.homeworkFinishToal = homeworkFinishToal;
    }

    public String getHomeworkNotFinishToal() {
        return homeworkNotFinishToal;
    }

    public void setHomeworkNotFinishToal(String homeworkNotFinishToal) {
        this.homeworkNotFinishToal = homeworkNotFinishToal;
    }
}