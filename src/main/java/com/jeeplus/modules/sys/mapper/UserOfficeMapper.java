/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.UserOffice;

import java.util.List;

/**
 * 用户-班级关联MAPPER接口
 * @author yangyang
 * @version 2019-04-25
 */
@MyBatisMapper
public interface UserOfficeMapper extends BaseMapper<UserOffice> {
}