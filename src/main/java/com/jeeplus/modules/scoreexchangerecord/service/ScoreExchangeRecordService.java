/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scoreexchangerecord.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.scoreexchangerecord.entity.ScoreExchangeRecord;
import com.jeeplus.modules.scoreexchangerecord.mapper.ScoreExchangeRecordMapper;

/**
 * 积分兑换记录Service
 * @author yangyang
 * @version 2019-05-30
 */
@Service
@Transactional(readOnly = true)
public class ScoreExchangeRecordService extends CrudService<ScoreExchangeRecordMapper, ScoreExchangeRecord> {

	public ScoreExchangeRecord get(String id) {
		return super.get(id);
	}
	
	public List<ScoreExchangeRecord> findList(ScoreExchangeRecord scoreExchangeRecord) {
		return super.findList(scoreExchangeRecord);
	}
	
	public Page<ScoreExchangeRecord> findPage(Page<ScoreExchangeRecord> page, ScoreExchangeRecord scoreExchangeRecord) {
		return super.findPage(page, scoreExchangeRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(ScoreExchangeRecord scoreExchangeRecord) {
		super.save(scoreExchangeRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(ScoreExchangeRecord scoreExchangeRecord) {
		super.delete(scoreExchangeRecord);
	}
	
}