/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.scoreexchange.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.scoreexchange.entity.ScoreExchange;
import com.jeeplus.modules.scoreexchange.mapper.ScoreExchangeMapper;

/**
 * 积分兑换信息Service
 * @author yangyang
 * @version 2019-04-27
 */
@Service
@Transactional(readOnly = true)
public class ScoreExchangeService extends CrudService<ScoreExchangeMapper, ScoreExchange> {

	public ScoreExchange get(String id) {
		return super.get(id);
	}
	
	public List<ScoreExchange> findList(ScoreExchange scoreExchange) {
		return super.findList(scoreExchange);
	}
	
	public Page<ScoreExchange> findPage(Page<ScoreExchange> page, ScoreExchange scoreExchange) {
		return super.findPage(page, scoreExchange);
	}
	
	@Transactional(readOnly = false)
	public void save(ScoreExchange scoreExchange) {
		super.save(scoreExchange);
	}
	
	@Transactional(readOnly = false)
	public void delete(ScoreExchange scoreExchange) {
		super.delete(scoreExchange);
	}
	
}