/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.hourrecord.entity;

import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 课时调整记录Entity
 * @author yangyang
 * @version 2019-07-24
 */
public class HourRecord extends DataEntity<HourRecord> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户
	private String oldHours;		// 原始课时
	private String newHours;		// 调整后课时
	
	public HourRecord() {
		super();
	}

	public HourRecord(String id){
		super(id);
	}

	@ExcelField(title="用户", align=2, sort=7)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="原始课时", align=2, sort=8)
	public String getOldHours() {
		return oldHours;
	}

	public void setOldHours(String oldHours) {
		this.oldHours = oldHours;
	}
	
	@ExcelField(title="调整后课时", align=2, sort=9)
	public String getNewHours() {
		return newHours;
	}

	public void setNewHours(String newHours) {
		this.newHours = newHours;
	}
	
}