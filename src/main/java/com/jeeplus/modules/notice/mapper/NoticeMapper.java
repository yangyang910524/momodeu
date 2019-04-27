/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.notice.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.notice.entity.Notice;

/**
 * 消息公告MAPPER接口
 * @author yangyang
 * @version 2019-04-27
 */
@MyBatisMapper
public interface NoticeMapper extends BaseMapper<Notice> {
	
}