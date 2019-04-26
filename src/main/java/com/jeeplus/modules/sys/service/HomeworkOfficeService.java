/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import java.util.List;

import com.jeeplus.modules.sys.entity.HomeworkOffice;
import com.jeeplus.modules.sys.mapper.HomeworkOfficeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;

/**
 * 作业-班级关联Service
 * @author yangyang
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class HomeworkOfficeService extends CrudService<HomeworkOfficeMapper, HomeworkOffice> {

	public HomeworkOffice get(String id) {
		return super.get(id);
	}
	
	public List<HomeworkOffice> findList(HomeworkOffice homeworkOffice) {
		return super.findList(homeworkOffice);
	}
	
	public Page<HomeworkOffice> findPage(Page<HomeworkOffice> page, HomeworkOffice homeworkOffice) {
		return super.findPage(page, homeworkOffice);
	}
	
	@Transactional(readOnly = false)
	public void save(HomeworkOffice homeworkOffice) {
		super.save(homeworkOffice);
	}
	
	@Transactional(readOnly = false)
	public void delete(HomeworkOffice homeworkOffice) {
		super.delete(homeworkOffice);
	}
	
}