/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.information.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 图文信息Entity
 * @author yangyang
 * @version 2019-04-27
 */
public class Information extends DataEntity<Information> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 标题
	private String content;		// 内容
	
	public Information() {
		super();
	}

	public Information(String id){
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
	
}