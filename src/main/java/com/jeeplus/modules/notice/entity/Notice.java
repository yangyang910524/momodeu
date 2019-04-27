/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.notice.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 消息公告Entity
 * @author yangyang
 * @version 2019-04-27
 */
public class Notice extends DataEntity<Notice> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String content;		// 内容
	private String state;		// 状态
	
	public Notice() {
		super();
	}

	public Notice(String id){
		super(id);
	}

	@ExcelField(title="标题", align=2, sort=7)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@ExcelField(title="内容", align=2, sort=8)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@ExcelField(title="状态", dictType="bas_release_type", align=2, sort=9)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}