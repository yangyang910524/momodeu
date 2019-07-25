/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.hourrecord.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.hourrecord.entity.HourRecord;

/**
 * 课程调整记录MAPPER接口
 * @author yangyang
 * @version 2019-07-24
 */
@MyBatisMapper
public interface HourRecordMapper extends BaseMapper<HourRecord> {
	
}