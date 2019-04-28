/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.sys.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.sys.entity.UserHomework;
import com.jeeplus.modules.sys.mapper.UserHomeworkMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户-作业关联Service
 * @author yangyang
 * @version 2019-04-28
 */
@Service
@Transactional(readOnly = true)
public class UserHomeworkService extends CrudService<UserHomeworkMapper, UserHomework> {

	@Override
    public UserHomework get(String id) {
		return super.get(id);
	}

    @Override
	public List<UserHomework> findList(UserHomework userHomework) {
		return super.findList(userHomework);
	}

    @Override
	public Page<UserHomework> findPage(Page<UserHomework> page, UserHomework userHomework) {
		return super.findPage(page, userHomework);
	}

    @Override
	@Transactional(readOnly = false)
	public void save(UserHomework userHomework) {
		super.save(userHomework);
	}

    @Override
	@Transactional(readOnly = false)
	public void delete(UserHomework userHomework) {
		super.delete(userHomework);
	}
	
}