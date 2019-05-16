/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.coursedataplayrecord.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.coursedataplayrecord.entity.CourseDataPlayRecord;
import com.jeeplus.modules.coursedataplayrecord.mapper.CourseDataPlayRecordMapper;

/**
 * 课程内容播放记录Service
 * @author yangyang
 * @version 2019-05-16
 */
@Service
@Transactional(readOnly = true)
public class CourseDataPlayRecordService extends CrudService<CourseDataPlayRecordMapper, CourseDataPlayRecord> {

	public CourseDataPlayRecord get(String id) {
		return super.get(id);
	}
	
	public List<CourseDataPlayRecord> findList(CourseDataPlayRecord courseDataPlayRecord) {
		return super.findList(courseDataPlayRecord);
	}
	
	public Page<CourseDataPlayRecord> findPage(Page<CourseDataPlayRecord> page, CourseDataPlayRecord courseDataPlayRecord) {
		return super.findPage(page, courseDataPlayRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(CourseDataPlayRecord courseDataPlayRecord) {
		super.save(courseDataPlayRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(CourseDataPlayRecord courseDataPlayRecord) {
		super.delete(courseDataPlayRecord);
	}
	
}