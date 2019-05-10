/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.statistics.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.statistics.entity.Statistics;

import java.util.List;

/**
 * 数据统计MAPPER接口
 * @author yangyang
 * @version 2019-04-29
 */
@MyBatisMapper
public interface StatisticsMapper extends BaseMapper<Statistics> {

    List<Statistics> scoreRankingByUser(Statistics entity);

    List<Statistics> worksRanking(Statistics entity);

    List<Statistics> homeworkStatistics(Statistics entity);
}