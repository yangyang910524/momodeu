/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.Classes;
import com.jeeplus.modules.sys.mapper.ClassesMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 班级管理Service
 * @author yangyang
 * @version 2019-04-25
 */
@Service
@Transactional(readOnly = true)
public class ClassesService extends CrudService<ClassesMapper, Classes> {

	@Override
    public Classes get(String id) {
		return super.get(id);
	}

    @Override
	public List<Classes> findList(Classes classes) {
		return super.findList(classes);
	}

    @Override
	public Page<Classes> findPage(Page<Classes> page, Classes classes) {
		return super.findPage(page, classes);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(Classes classes) {
		super.save(classes);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(Classes classes) {
		super.delete(classes);
	}

}