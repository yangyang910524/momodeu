/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.UserHomework;
import com.jeeplus.modules.sys.entity.UserOffice;

/**
 * 用户-作业关联MAPPER接口
 * @author yangyang
 * @version 2019-04-28
 */
@MyBatisMapper
public interface UserHomeworkMapper extends BaseMapper<UserHomework> {

    Integer deleteUserHomeworkByState(UserOffice userOffice);

    Integer cleanUserHomeworkOfficeId(UserOffice userOffice);

    Integer addUserHomeworkByOffice(UserOffice userOffice);

    Integer updateUserHomeworkOffice(UserOffice userOffice);
}