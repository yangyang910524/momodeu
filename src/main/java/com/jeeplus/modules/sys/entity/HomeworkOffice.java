/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 作业-班级关联Entity
 * @author yangyang
 * @version 2019-04-26
 */
public class HomeworkOffice extends DataEntity<HomeworkOffice> {
	
	private static final long serialVersionUID = 1L;
	private String officeid;		// 班级id
	private String homeworkid;		// 作业id
	private String officeName;  //班级名称
	private String homeworkName;  //作业名称
    private String statue;		// 状态
    private String issueDate;		// 发布时间
    private String beginIssueDate;		// 开始 发布时间
    private String endIssueDate;		// 结束 发布时间
	
	public HomeworkOffice() {
		super();
	}

	public HomeworkOffice(String id){
		super(id);
	}

	@ExcelField(title="班级id", align=2, sort=1)
	public String getOfficeid() {
		return officeid;
	}

	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}
	
	@ExcelField(title="作业id", align=2, sort=2)
	public String getHomeworkid() {
		return homeworkid;
	}

	public void setHomeworkid(String homeworkid) {
		this.homeworkid = homeworkid;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getHomeworkName() {
		return homeworkName;
	}

	public void setHomeworkName(String homeworkName) {
		this.homeworkName = homeworkName;
	}

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getBeginIssueDate() {
        return beginIssueDate;
    }

    public void setBeginIssueDate(String beginIssueDate) {
        this.beginIssueDate = beginIssueDate;
    }

    public String getEndIssueDate() {
        return endIssueDate;
    }

    public void setEndIssueDate(String endIssueDate) {
        this.endIssueDate = endIssueDate;
    }
}