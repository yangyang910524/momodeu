/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.homework.entity.Homework;

import java.util.Date;

/**
 * 用户-作业关联Entity
 * @author yangyang
 * @version 2019-04-28
 */
public class UserHomework extends DataEntity<UserHomework> {
	
	private static final long serialVersionUID = 1L;
	private String state;		// 状态
	private Date finishDate;		// 完成时间
	private String file;		// 文件
	private String score;		// 老师打分
	private String comment;		// 老师评语
	private String beginFinishDate;		// 开始 完成时间
	private String endFinishDate;		// 结束 完成时间
    private Office office;//班级信息
    private User student;//学生信息
    private User teacher;//老师信息
	private Homework homework;//作业信息
    private String startRecordingTime;//开始录音时间

    public String getStartRecordingTime() {
        return startRecordingTime;
    }

    public void setStartRecordingTime(String startRecordingTime) {
        this.startRecordingTime = startRecordingTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBeginFinishDate() {
        return beginFinishDate;
    }

    public void setBeginFinishDate(String beginFinishDate) {
        this.beginFinishDate = beginFinishDate;
    }

    public String getEndFinishDate() {
        return endFinishDate;
    }

    public void setEndFinishDate(String endFinishDate) {
        this.endFinishDate = endFinishDate;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }
}