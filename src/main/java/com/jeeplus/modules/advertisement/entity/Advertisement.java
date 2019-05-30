/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.advertisement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;

/**
 * 广告信息Entity
 * @author yangyang
 * @version 2019-04-27
 */
public class Advertisement extends DataEntity<Advertisement> {
	
	private static final long serialVersionUID = 1L;
	private String picture;		// 图片
	private String state;		// 状态
	private Date issueTime;		// 发布时间
	private Date beginIssueTime;		// 开始 发布时间
	private Date endIssueTime;		// 结束 发布时间
    private String type;//广告类型
    private String content;//内容图片

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Advertisement() {
		super();
	}

	public Advertisement(String id){
		super(id);
	}

	@ExcelField(title="图片", align=2, sort=7)
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	@ExcelField(title="状态", dictType="bas_release_type", align=2, sort=8)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="发布时间", align=2, sort=9)
	public Date getIssueTime() {
		return issueTime;
	}

	public void setIssueTime(Date issueTime) {
		this.issueTime = issueTime;
	}
	
	public Date getBeginIssueTime() {
		return beginIssueTime;
	}

	public void setBeginIssueTime(Date beginIssueTime) {
		this.beginIssueTime = beginIssueTime;
	}
	
	public Date getEndIssueTime() {
		return endIssueTime;
	}

	public void setEndIssueTime(Date endIssueTime) {
		this.endIssueTime = endIssueTime;
	}
		
}