/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 用户-班级关联Entity
 * @author yangyang
 * @version 2019-04-25
 */
public class UserOffice extends DataEntity<UserOffice> {
	
	private static final long serialVersionUID = 1L;
	private String officeid;		// 班级id
	private String userid;		// 用户id
	private String userType;		// 用户类型详,见bas_user_type
	
	public UserOffice() {
		super();
	}

	public UserOffice(String id){
		super(id);
	}

	@ExcelField(title="班级id", align=2, sort=0)
	public String getOfficeid() {
		return officeid;
	}

	public void setOfficeid(String officeid) {
		this.officeid = officeid;
	}
	
	@ExcelField(title="用户id", align=2, sort=1)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@ExcelField(title="用户类型详,见bas_user_type", dictType="bas_user_type", align=2, sort=2)
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
}