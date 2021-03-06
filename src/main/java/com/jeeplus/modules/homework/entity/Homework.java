/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.Office;

/**
 * 作业管理Entity
 * @author yangyang
 * @version 2019-04-26
 */
public class Homework extends DataEntity<Homework> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 名称
	private String data1;		// 材料1
	private String data2;		// 材料2
    private String silentVideo;//无声视频
	private String cover;		// 封面
	private Office office;//班级
    private String courseLevel;		// 课程级别
    private String type;		// 作业类型
    private String state;//发布状态

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Homework() {
		super();
	}

	public Homework(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="类型", dictType="bae_homework_type", align=2, sort=8)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="材料1", align=2, sort=9)
	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}
	
	@ExcelField(title="材料2", align=2, sort=10)
	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}
	
	@ExcelField(title="封面", align=2, sort=11)
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getSilentVideo() {
        return silentVideo;
    }

    public void setSilentVideo(String silentVideo) {
        this.silentVideo = silentVideo;
    }
}