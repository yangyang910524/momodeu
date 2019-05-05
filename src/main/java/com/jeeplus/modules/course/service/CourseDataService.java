/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.course.entity.CourseData;
import com.jeeplus.modules.course.mapper.CourseDataMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程内容Service
 * @author yangyang
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class CourseDataService extends CrudService<CourseDataMapper, CourseData> {
    @Resource
    private CourseDataMapper courseDataMapper;

	public CourseData get(String id) {
		return super.get(id);
	}
	
	public List<CourseData> findList(CourseData courseData) {
		return super.findList(courseData);
	}
	
	public Page<CourseData> findPage(Page<CourseData> page, CourseData courseData) {
		return super.findPage(page, courseData);
	}
	
	@Transactional(readOnly = false)
	public void save(CourseData courseData) {
		super.save(courseData);
	}
	
	@Transactional(readOnly = false)
	public void delete(CourseData courseData) {
		super.delete(courseData);
	}

    public Page<CourseData> findCourseList(Page<CourseData> page, CourseData entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(courseDataMapper.findCourseList(entity));
        return page;
    }
}