/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.homework.entity.Homework;
import com.jeeplus.modules.homework.mapper.HomeworkMapper;

import javax.annotation.Resource;

/**
 * 作业管理Service
 * @author yangyang
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class HomeworkService extends CrudService<HomeworkMapper, Homework> {
	@Resource
	private HomeworkMapper homeworkMapper;

	public Homework get(String id) {
		return super.get(id);
	}
	
	public List<Homework> findList(Homework homework) {
		return super.findList(homework);
	}
	
	public Page<Homework> findPage(Page<Homework> page, Homework homework) {
		return super.findPage(page, homework);
	}
	
	@Transactional(readOnly = false)
	public void save(Homework homework) {
		super.save(homework);
	}
	
	@Transactional(readOnly = false)
	public void delete(Homework homework) {
		super.delete(homework);
	}

    public Page<Homework> findHomework(Page<Homework> page, Homework homework) {
		dataRuleFilter(homework);
		homework.setPage(page);
		page.setList(homeworkMapper.findHomework(homework));
		return page;
    }
}