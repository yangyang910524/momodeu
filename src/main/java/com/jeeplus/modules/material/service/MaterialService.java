/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.material.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.material.entity.Material;
import com.jeeplus.modules.material.mapper.MaterialMapper;

/**
 * 材料管理Service
 * @author yangyang
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class MaterialService extends CrudService<MaterialMapper, Material> {

	public Material get(String id) {
		return super.get(id);
	}
	
	public List<Material> findList(Material material) {
		return super.findList(material);
	}
	
	public Page<Material> findPage(Page<Material> page, Material material) {
		return super.findPage(page, material);
	}
	
	@Transactional(readOnly = false)
	public void save(Material material) {
		super.save(material);
	}
	
	@Transactional(readOnly = false)
	public void delete(Material material) {
		super.delete(material);
	}
	
}