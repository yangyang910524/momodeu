/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.advertisement.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.advertisement.entity.Advertisement;
import com.jeeplus.modules.advertisement.mapper.AdvertisementMapper;

/**
 * 广告信息Service
 * @author yangyang
 * @version 2019-04-27
 */
@Service
@Transactional(readOnly = true)
public class AdvertisementService extends CrudService<AdvertisementMapper, Advertisement> {

	public Advertisement get(String id) {
		return super.get(id);
	}
	
	public List<Advertisement> findList(Advertisement advertisement) {
		return super.findList(advertisement);
	}
	
	public Page<Advertisement> findPage(Page<Advertisement> page, Advertisement advertisement) {
		return super.findPage(page, advertisement);
	}
	
	@Transactional(readOnly = false)
	public void save(Advertisement advertisement) {
		super.save(advertisement);
	}
	
	@Transactional(readOnly = false)
	public void delete(Advertisement advertisement) {
		super.delete(advertisement);
	}
	
}