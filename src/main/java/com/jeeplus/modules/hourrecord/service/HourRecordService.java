/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.hourrecord.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.hourrecord.entity.HourRecord;
import com.jeeplus.modules.hourrecord.mapper.HourRecordMapper;

/**
 * 课程调整记录Service
 * @author yangyang
 * @version 2019-07-24
 */
@Service
@Transactional(readOnly = true)
public class HourRecordService extends CrudService<HourRecordMapper, HourRecord> {

	public HourRecord get(String id) {
		return super.get(id);
	}
	
	public List<HourRecord> findList(HourRecord hourRecord) {
		return super.findList(hourRecord);
	}
	
	public Page<HourRecord> findPage(Page<HourRecord> page, HourRecord hourRecord) {
		return super.findPage(page, hourRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(HourRecord hourRecord) {
		super.save(hourRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(HourRecord hourRecord) {
		super.delete(hourRecord);
	}
	
}