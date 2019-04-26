/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.course.entity.CourseInfo;
import com.jeeplus.modules.course.mapper.CourseInfoMapper;

/**
 * 课程管理Service
 * @author yangyang
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class CourseInfoService extends TreeService<CourseInfoMapper, CourseInfo> {

	public CourseInfo get(String id) {
		return super.get(id);
	}
	
	public List<CourseInfo> findList(CourseInfo courseInfo) {
		if (StringUtils.isNotBlank(courseInfo.getParentIds())){
			courseInfo.setParentIds(","+courseInfo.getParentIds()+",");
		}
		return super.findList(courseInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(CourseInfo courseInfo) {
		super.save(courseInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(CourseInfo courseInfo) {
		super.delete(courseInfo);
	}
	
}