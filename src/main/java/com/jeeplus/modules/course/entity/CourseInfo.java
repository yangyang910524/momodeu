/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.entity;

import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.TreeEntity;
import com.jeeplus.modules.sys.entity.Office;

import java.util.List;

/**
 * 课程管理Entity
 * @author yangyang
 * @version 2019-04-26
 */
public class CourseInfo extends TreeEntity<CourseInfo> {
	
	private static final long serialVersionUID = 1L;
	
	private List<CourseData> courseDataList = Lists.newArrayList();		// 子表列表
    private Office office;
    private String cover;
    private String titleType;
    private String level;
    private String state;
	
	public CourseInfo() {
		super();
	}

	public CourseInfo(String id){
		super(id);
	}

	@Override
    public  CourseInfo getParent() {
			return parent;
	}

	@Override
	public void setParent(CourseInfo parent) {
		this.parent = parent;
		
	}
	
	public List<CourseData> getCourseDataList() {
		return courseDataList;
	}

	public void setCourseDataList(List<CourseData> courseDataList) {
		this.courseDataList = courseDataList;
	}
	@Override
    public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}