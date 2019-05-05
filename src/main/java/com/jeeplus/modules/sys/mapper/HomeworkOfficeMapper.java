/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.HomeworkOffice;

/**
 * 作业-班级关联MAPPER接口
 * @author yangyang
 * @version 2019-04-26
 */
@MyBatisMapper
public interface HomeworkOfficeMapper extends BaseMapper<HomeworkOffice> {

    void insertStudentHomework(HomeworkOffice homeworkOffice);

    void deleteStudentHomework(HomeworkOffice homeworkOffice);
}