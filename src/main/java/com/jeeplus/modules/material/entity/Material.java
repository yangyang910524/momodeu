/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.material.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 材料管理Entity
 * @author yangyang
 * @version 2019-04-26
 */
public class Material extends DataEntity<Material> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 材料名称
	private String data1;		// 材料1
	private String state;		// 状态
	private String type;		// 材料类型
	private String data2;		// 材料2
	
	public Material() {
		super();
	}

	public Material(String id){
		super(id);
	}

	@ExcelField(title="材料名称", align=2, sort=7)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="材料1", align=2, sort=8)
	public String getData1() {
		return data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}
	
	@ExcelField(title="状态", dictType="bas_release_type", align=2, sort=9)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@ExcelField(title="材料类型", dictType="bas_material_type", align=2, sort=10)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@ExcelField(title="材料2", align=2, sort=11)
	public String getData2() {
		return data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}
	
}