/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.signuponline.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 在线报名Entity
 * @author yangyang
 * @version 2019-05-16
 */
public class SignUpOnLine extends DataEntity<SignUpOnLine> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String old;		// 年龄
	private String sex;		// 性别
	private String phone;		// 联系电话
	private String address;		// 地址
	
	public SignUpOnLine() {
		super();
	}

	public SignUpOnLine(String id){
		super(id);
	}

	@ExcelField(title="姓名", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="年龄", align=2, sort=8)
	public String getOld() {
		return old;
	}

	public void setOld(String old) {
		this.old = old;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=9)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="联系电话", align=2, sort=10)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ExcelField(title="地址", align=2, sort=11)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}