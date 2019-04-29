/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.statistics.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.statistics.entity.Statistics;
import com.jeeplus.modules.statistics.service.StatisticsService;

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
	 * 信息列表页面
	 */
	@RequestMapping(value = "scoreRankingByUser")
	public String list(Statistics statistics, Model model) {
		model.addAttribute("statistics", statistics);
		return "modules/statistics/scoreRankingByUser";
	}
	
		/**
	 * 信息列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "scoreRankingByUserData")
	public Map<String, Object> data(Statistics statistics, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Statistics> page = statisticsService.scoreRankingByUser(new Page<Statistics>(request, response), statistics);
		return getBootstrapData(page);
	}

}