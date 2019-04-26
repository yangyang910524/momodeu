/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.homework.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.homework.entity.Homework;

import java.util.List;

/**
 * 作业管理MAPPER接口
 * @author yangyang
 * @version 2019-04-26
 */
@MyBatisMapper
public interface HomeworkMapper extends BaseMapper<Homework> {

    List<Homework> findHomework(Homework homework);
}