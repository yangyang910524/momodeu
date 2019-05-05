/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.service;

import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.TreeService;
import com.jeeplus.modules.course.entity.CourseInfo;
import com.jeeplus.modules.course.mapper.CourseInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程管理Service
 * @author yangyang
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class CourseInfoService extends TreeService<CourseInfoMapper, CourseInfo> {
    @Resource
    private CourseInfoMapper courseInfoMapper;

	@Override
    public CourseInfo get(String id) {
		return super.get(id);
	}

    @Override
	public List<CourseInfo> findList(CourseInfo courseInfo) {
		if (StringUtils.isNotBlank(courseInfo.getParentIds())){
			courseInfo.setParentIds(","+courseInfo.getParentIds()+",");
		}
		return super.findList(courseInfo);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(CourseInfo courseInfo) {
		super.save(courseInfo);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(CourseInfo courseInfo) {
		super.delete(courseInfo);
	}


	public Page<CourseInfo> findChapterList(Page<CourseInfo> page, CourseInfo courseInfo) {
		dataRuleFilter(courseInfo);
		// 设置分页参数
		courseInfo.setPage(page);
		// 执行分页查询
		page.setList(courseInfoMapper.findChapterList(courseInfo));
		return page;
	}

	@Transactional(readOnly = false)
	public void release(CourseInfo courseInfo) {
		courseInfoMapper.release(courseInfo);
	}
}