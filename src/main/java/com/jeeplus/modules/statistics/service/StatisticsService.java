/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.statistics.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.statistics.entity.Statistics;
import com.jeeplus.modules.statistics.mapper.StatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据统计Service
 * @author yangyang
 * @version 2019-04-29
 */
@Service
@Transactional(readOnly = true)
public class StatisticsService extends CrudService<StatisticsMapper, Statistics> {
	@Resource
	private StatisticsMapper statisticsMapper;
    public Page<Statistics> scoreRankingByUser(Page<Statistics> page, Statistics entity) {
		dataRuleFilter(entity);
		entity.setPage(page);
		page.setList(statisticsMapper.scoreRankingByUser(entity));
		return page;
    }

    public List<Statistics> scoreRankingByUser(Statistics entity) {
        return statisticsMapper.scoreRankingByUser(entity);
    }

    public Page<Statistics> homeworkStatistics(Page<Statistics> page, Statistics entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(statisticsMapper.homeworkStatistics(entity));
        return page;
    }

    public List<Statistics> homeworkStatistics(Statistics entity) {
        return statisticsMapper.homeworkStatistics(entity);
    }
}