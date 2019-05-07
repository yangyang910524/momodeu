/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.statistics.web;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.statistics.entity.Statistics;
import com.jeeplus.modules.statistics.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 数据统计Controller
 * @author yangyang
 * @version 2019-04-29
 */
@Controller
@RequestMapping(value = "${adminPath}/statistics/statistics")
public class StatisticsController extends BaseController {

	@Autowired
	private StatisticsService statisticsService;

	/**
	 * 积分排行
	 */
	@RequestMapping(value = "scoreRankingByUser")
	public String list(Statistics statistics, Model model) {
		model.addAttribute("statistics", statistics);
		return "modules/statistics/scoreRankingByUser";
	}
	
    /**
	 * 积分排行数据
	 */
	@ResponseBody
	@RequestMapping(value = "scoreRankingByUserData")
	public Map<String, Object> data(Statistics statistics, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Statistics> page = statisticsService.scoreRankingByUser(new Page<Statistics>(request, response), statistics);
		return getBootstrapData(page);
	}

    /**
     * 作品排行
     */
    @RequestMapping(value = "worksRanking")
    public String worksRanking(Statistics statistics, Model model) {
        model.addAttribute("statistics", statistics);
        return "modules/statistics/worksRanking";
    }

    /**
     * 作品排行
     */
    @ResponseBody
    @RequestMapping(value = "worksRankingData")
    public Map<String, Object> worksRankingData(Statistics statistics, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Statistics> page = statisticsService.scoreRankingByUser(new Page<Statistics>(request, response), statistics);
        return getBootstrapData(page);
    }
}