/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.information.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.information.entity.Information;
import com.jeeplus.modules.information.mapper.InformationMapper;

/**
 * 图文信息Service
 * @author yangyang
 * @version 2019-04-27
 */
@Service
@Transactional(readOnly = true)
public class InformationService extends CrudService<InformationMapper, Information> {

	public Information get(String id) {
		return super.get(id);
	}
	
	public List<Information> findList(Information information) {
		return super.findList(information);
	}
	
	public Page<Information> findPage(Page<Information> page, Information information) {
		return super.findPage(page, information);
	}
	
	@Transactional(readOnly = false)
	public void save(Information information) {
		super.save(information);
	}
	
	@Transactional(readOnly = false)
	public void delete(Information information) {
		super.delete(information);
	}
	
}