/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scoreexchange.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 积分兑换信息Entity
 * @author yangyang
 * @version 2019-04-27
 */
public class ScoreExchange extends DataEntity<ScoreExchange> {
	
	private static final long serialVersionUID = 1L;
	private Integer score;		// 所需分数
	private String photo;		// 照片
	private String name;		// 名称
	
	public ScoreExchange() {
		super();
	}

	public ScoreExchange(String id){
		super(id);
	}

	@ExcelField(title="所需分数", align=2, sort=7)
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	@ExcelField(title="照片", align=2, sort=8)
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	@ExcelField(title="名称", align=2, sort=9)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}