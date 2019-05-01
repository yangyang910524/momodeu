/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scorerecord.entity;

import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 积分调整记录Entity
 * @author yangyang
 * @version 2019-05-01
 */
public class ScoreRecord extends DataEntity<ScoreRecord> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 学生
	private String oldScore;		// 原始积分
	private String newScore;		// 调整后积分
	
	public ScoreRecord() {
		super();
	}

	public ScoreRecord(String id){
		super(id);
	}

	@ExcelField(title="学生", align=2, sort=7)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="原始积分", align=2, sort=8)
	public String getOldScore() {
		return oldScore;
	}

	public void setOldScore(String oldScore) {
		this.oldScore = oldScore;
	}
	
	@ExcelField(title="调整后积分", align=2, sort=9)
	public String getNewScore() {
		return newScore;
	}

	public void setNewScore(String newScore) {
		this.newScore = newScore;
	}
	
}