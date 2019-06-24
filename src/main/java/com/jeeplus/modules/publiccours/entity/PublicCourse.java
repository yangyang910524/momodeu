/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.publiccours.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 公共课程管理Entity
 * @author yangyang
 * @version 2019-05-05
 */
public class PublicCourse extends DataEntity<PublicCourse> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 课程名称
	private String data;		// 课程内容
	private String type;		// 课程类型
    private String cover;     //封面

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public PublicCourse() {
		super();
	}

	public PublicCourse(String id){
		super(id);
	}

	@ExcelField(title="课程名称", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="课程内容", align=2, sort=8)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@ExcelField(title="课程类型", dictType="bas_public_course_type", align=2, sort=9)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}