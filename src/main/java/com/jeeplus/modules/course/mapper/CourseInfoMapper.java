/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.course.mapper;

import com.jeeplus.core.persistence.TreeMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.course.entity.CourseInfo;

/**
 * 课程管理MAPPER接口
 * @author yangyang
 * @version 2019-04-26
 */
@MyBatisMapper
public interface CourseInfoMapper extends TreeMapper<CourseInfo> {
	
}