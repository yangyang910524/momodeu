/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scorerecord.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.scorerecord.entity.ScoreRecord;
import com.jeeplus.modules.scorerecord.mapper.ScoreRecordMapper;

/**
 * 积分调整记录Service
 * @author yangyang
 * @version 2019-05-01
 */
@Service
@Transactional(readOnly = true)
public class ScoreRecordService extends CrudService<ScoreRecordMapper, ScoreRecord> {

	public ScoreRecord get(String id) {
		return super.get(id);
	}
	
	public List<ScoreRecord> findList(ScoreRecord scoreRecord) {
		return super.findList(scoreRecord);
	}
	
	public Page<ScoreRecord> findPage(Page<ScoreRecord> page, ScoreRecord scoreRecord) {
		return super.findPage(page, scoreRecord);
	}
	
	@Transactional(readOnly = false)
	public void save(ScoreRecord scoreRecord) {
		super.save(scoreRecord);
	}
	
	@Transactional(readOnly = false)
	public void delete(ScoreRecord scoreRecord) {
		super.delete(scoreRecord);
	}
	
}