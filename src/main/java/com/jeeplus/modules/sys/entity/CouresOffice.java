/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.entity;


import com.jeeplus.core.persistence.DataEntity;

/**
 * 课程-班级关联Entity
 * @author yangyang
 * @version 2019-04-26
 */
public class CouresOffice extends DataEntity<CouresOffice> {
	
	private static final long serialVersionUID = 1L;
	private String officeid;		// 班级主键
	private String couresid;		// 课程主键
    private String officeName;  //班级名称
    private String couresName;  //课程名称

    public String getOfficeid() {
        return officeid;
    }

    public void setOfficeid(String officeid) {
        this.officeid = officeid;
    }

    public String getCouresid() {
        return couresid;
    }

    public void setCouresid(String couresid) {
        this.couresid = couresid;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getCouresName() {
        return couresName;
    }

    public void setCouresName(String couresName) {
        this.couresName = couresName;
    }
}