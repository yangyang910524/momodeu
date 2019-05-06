/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.publiccours.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.publiccours.entity.PublicCourse;
import com.jeeplus.modules.publiccours.mapper.PublicCourseMapper;

/**
 * 公共课程管理Service
 * @author yangyang
 * @version 2019-05-05
 */
@Service
@Transactional(readOnly = true)
public class PublicCourseService extends CrudService<PublicCourseMapper, PublicCourse> {

	public PublicCourse get(String id) {
		return super.get(id);
	}
	
	public List<PublicCourse> findList(PublicCourse publicCourse) {
		return super.findList(publicCourse);
	}
	
	public Page<PublicCourse> findPage(Page<PublicCourse> page, PublicCourse publicCourse) {
		return super.findPage(page, publicCourse);
	}
	
	@Transactional(readOnly = false)
	public void save(PublicCourse publicCourse) {
		super.save(publicCourse);
	}
	
	@Transactional(readOnly = false)
	public void delete(PublicCourse publicCourse) {
		super.delete(publicCourse);
	}
	
}