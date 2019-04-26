/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.CouresOffice;
import com.jeeplus.modules.sys.mapper.CouresOfficeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程-班级关联Service
 * @author yangyang
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class CouresOfficeService extends CrudService<CouresOfficeMapper, CouresOffice> {

	@Override
    public CouresOffice get(String id) {
		return super.get(id);
	}

    @Override
	public List<CouresOffice> findList(CouresOffice couresOffice) {
		return super.findList(couresOffice);
	}

    @Override
	public Page<CouresOffice> findPage(Page<CouresOffice> page, CouresOffice couresOffice) {
		return super.findPage(page, couresOffice);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(CouresOffice couresOffice) {
		super.save(couresOffice);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(CouresOffice couresOffice) {
		super.delete(couresOffice);
	}
	
}