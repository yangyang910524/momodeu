/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
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
	private Integer score;		// 老师打分
	private String comment;		// 老师评语
	private String beginFinishDate;		// 开始 完成时间
	private String endFinishDate;		// 结束 完成时间
	private String userid;
	private String homeworkid;
	private String officeid;
	private Homework homework;//作业信息

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public UserHomework() {
		super();
	}

	public UserHomework(String id){
		super(id);
	}

	@ExcelField(title="状态", dictType="", align=2, sort=7)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    @ExcelField(title="文件", align=2, sort=9)
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	@ExcelField(title="老师打分", align=2, sort=10)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@ExcelField(title="老师评语", align=2, sort=11)
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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getHomeworkid() {
		return homeworkid;
	}

	public void setHomeworkid(String homeworkid) {
		this.homeworkid = homeworkid;
	}

	public String getOfficeid() {
		return officeid;
	}

	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}
}