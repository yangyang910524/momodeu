/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.core.persistence.DataEntity;

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
    private String officeName;//班级名称
    private String userName;//姓名
    private String photo;	// 头像
    private String loginName;// 登录名
    private Integer score ; // 分数情况
    private String englishName ; // 分数情况
    private String mobile;	// 手机


    public String getOfficeid() {
        return officeid;
    }

    public void setOfficeid(String officeid) {
        this.officeid = officeid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}